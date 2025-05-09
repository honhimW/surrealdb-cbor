package io.github.honhimw.surreal.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;
import io.github.honhimw.surreal.ReactiveTypedSurrealClient;
import io.github.honhimw.surreal.UpsertKind;
import io.github.honhimw.surreal.model.Id;
import io.github.honhimw.surreal.model.RecordId;
import io.github.honhimw.surreal.model.Result;
import io.github.honhimw.surreal.model.Table;
import io.github.honhimw.surreal.util.CborUtils;
import io.github.honhimw.surreal.util.Helpers;
import io.github.honhimw.surreal.util.JsonUtils;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author honhimW
 * @since 2025-05-08
 */

@Slf4j
class ReactiveTypedSurrealClientImpl<T, ID> implements ReactiveTypedSurrealClient<T, ID> {

    private final ReactiveSurrealClientImpl client;

    private final Class<T> type;

    private final Table table;

    private final Id.Kind idKind;

    ReactiveTypedSurrealClientImpl(ReactiveSurrealClientImpl client, Class<T> type, Table table, @Nullable Id.Kind idKind) {
        this.client = client;
        this.type = type;
        this.table = table;
        this.idKind = idKind;
    }

    @Override
    public String table() {
        return table.name;
    }

    @Override
    public Class<T> type() {
        return type;
    }

    @Override
    public Mono<T> getById(ID id) {
        RecordId recordId = toRecordId(id);
        Map<String, Object> body = new LinkedHashMap<>(3);
        body.put("id", client.idGenerator.generate());
        body.put("method", "select");
        body.put("params", Collections.singletonList(recordId));
        return client.httpOps.execute(configurer -> client.configure(configurer, body))
            .handle(client::handleResult)
            .map(bytes -> client.decodeAndThrowIfError(bytes, JsonNode.class))
            .flatMap(jsonNode -> {
                JsonNode result = jsonNode.at("/result");
                if (!Helpers.exists(result)) {
                    return Mono.empty();
                }
                convertId(result.require());
                return Mono.just(JsonUtils.mapper().convertValue(result, type));
            });
    }

    @Override
    public Flux<T> getByIds(Collection<ID> ids) {
        if (Helpers.isEmpty(ids)) {
            return Flux.empty();
        }
        List<RecordId> recordIds = ids.stream()
            .map(this::toRecordId)
            .collect(Collectors.toList());

        Map<String, Object> params = new LinkedHashMap<>(1);
        String paramName = table.name + "_ids";
        String sql = String.format("SELECT * FROM %s WHERE id IN $%s;", table.name, paramName);
        params.put(paramName, recordIds);

        return client.sqlBytes(sql, params)
            .map(bytes -> client.decodeAndThrowIfError(bytes, JsonNode.class))
            .flatMapMany(jsonNode -> {
                JsonNode status = jsonNode.at("/result/0/status");
                if (!Helpers.exists(status)) {
                    return Mono.error(new IllegalStateException("Unexpected result without statement result"));
                }
                List<T> results = new ArrayList<>(ids.size());
                JsonNode result = jsonNode.at("/result/0/result");
                ArrayNode arrayNode = result.require();
                for (JsonNode node : arrayNode) {
                    convertId(node.require());
                    results.add(JsonUtils.mapper().convertValue(node, type));
                }
                return Flux.fromIterable(results);
            });

    }

    @Override
    public Mono<T> upsert(T entity, UpsertKind kind) {
        JsonNode entityNode = CborUtils.mapper().valueToTree(entity);
        RecordId recordId = takeRecordId(entityNode);

        if (kind == UpsertKind.MERGE) {
            Helpers.removeNullEntries(entityNode);
        } else if (kind == UpsertKind.PATCH) {
            throw new IllegalArgumentException("`PATCH` upsert not supported by current method");
        }

        Map<String, Object> content = new LinkedHashMap<>(1);
        content.put("content", entityNode);

        return client.sqlBytes("UPSERT ONLY " + recordId.table.name + " " + kind.name() + " $content;", content)
            .map(bytes -> client.decodeAndThrowIfError(bytes, JsonNode.class))
            .flatMap(jsonNode -> {
                JsonNode status = jsonNode.at("/result/0/status");
                if (!Helpers.exists(status)) {
                    return Mono.error(new IllegalStateException("Unexpected result without statement result"));
                }
                JsonNode result = jsonNode.at("/result/0/result");
                if (!Result.Status.OK.validate(status.textValue())) {
                    return Mono.error(new IllegalStateException("Upsert failed: " + result.asText()));
                }
                if (!Helpers.exists(result)) {
                    return Mono.empty();
                }
                convertId(result.require());
                return Mono.just(JsonUtils.mapper().convertValue(result, type));
            });
    }

    @Override
    public Mono<T> create(T entity) {
        JsonNode entityNode = CborUtils.mapper().valueToTree(entity);
        Map<String, Object> params = new LinkedHashMap<>(1);
        params.put("content", entityNode);
        return client.sqlBytes("CREATE ONLY " + table.name + " CONTENT $content;", params)
            .map(bytes -> client.decodeAndThrowIfError(bytes, JsonNode.class))
            .flatMap(jsonNode -> {
                JsonNode status = jsonNode.at("/result/0/status");
                if (!Helpers.exists(status)) {
                    return Mono.error(new IllegalStateException("Unexpected result without statement result"));
                }
                JsonNode result = jsonNode.at("/result/0/result");
                if (!Result.Status.OK.validate(status.textValue())) {
                    return Mono.error(new IllegalStateException("Create failed: " + result.asText()));
                }
                if (!Helpers.exists(result)) {
                    return Mono.empty();
                }
                convertId(result.require());
                return Mono.just(JsonUtils.mapper().convertValue(result, type));
            });
    }

    @Override
    public Mono<Void> delete(ID id) {
        RecordId recordId = toRecordId(id);
        // Prevent from delete all records from table
        Helpers.state(Helpers.isNotBlank(recordId.table.name), "Table name should not be blank");
        Helpers.state(recordId.id.value != null, "RecordId.id.value should not be null");
        Map<String, Object> params = new LinkedHashMap<>(1);
        params.put("record_id", recordId);
        return client.sqlBytes("(DELETE $record_id RETURN BEFORE).map(|$record| $record.id);", params)
            .map(bytes -> client.decodeAndThrowIfError(bytes, JsonNode.class))
            .flatMap(jsonNode -> {
                JsonNode status = jsonNode.at("/result/0/status");
                if (!Helpers.exists(status)) {
                    return Mono.error(new IllegalStateException("Unexpected result without statement result"));
                }
                if (!Result.Status.OK.validate(status.textValue())) {
                    JsonNode result = jsonNode.at("/result/0/result");
                    return Mono.error(new IllegalStateException("Delete failed: " + result.asText()));
                }
                return Mono.empty();
            });
    }

    @Override
    public Flux<T> insert(Collection<T> entities) {
        if (Helpers.isEmpty(entities)) {
            return Flux.empty();
        }

        Map<String, Object> params = new LinkedHashMap<>(1);
        params.put("entities", entities);
        return client.sqlBytes("INSERT IGNORE INTO " + table.name + "$entities;", params)
            .map(bytes -> client.decodeAndThrowIfError(bytes, JsonNode.class))
            .flatMapMany(jsonNode -> {
                JsonNode status = jsonNode.at("/result/0/status");
                if (!Helpers.exists(status)) {
                    return Flux.error(new IllegalStateException("Unexpected result without statement result"));
                }
                JsonNode result = jsonNode.at("/result/0/result");
                if (!Result.Status.OK.validate(status.textValue())) {
                    return Flux.error(new IllegalStateException("Insert failed: " + result.asText()));
                }
                List<T> results = new ArrayList<>(entities.size());
                ArrayNode arrayNode = result.require();
                for (JsonNode node : arrayNode) {
                    convertId(node.require());
                    results.add(JsonUtils.mapper().convertValue(node, type));
                }
                return Flux.fromIterable(results);
            });
    }

    @Override
    public Mono<T> update(T entity, UpsertKind kind) {
        JsonNode entityNode = CborUtils.mapper().valueToTree(entity);
        RecordId recordId = takeRecordId(entityNode);

        if (kind == UpsertKind.MERGE) {
            Helpers.removeNullEntries(entityNode);
        } else if (kind == UpsertKind.PATCH) {
            throw new IllegalArgumentException("`PATCH` update not supported by current method");
        }

        Map<String, Object> content = new LinkedHashMap<>(2);
        content.put("record_id", recordId);
        content.put("content", entityNode);

        String sql = String.format("UPDATE ONLY $record_id %s $content;", kind.name());
        return client.sqlBytes(sql, content)
            .map(bytes -> client.decodeAndThrowIfError(bytes, JsonNode.class))
            .flatMap(jsonNode -> {
                JsonNode status = jsonNode.at("/result/0/status");
                if (!Helpers.exists(status)) {
                    return Mono.error(new IllegalStateException("Unexpected result without statement result"));
                }
                JsonNode result = jsonNode.at("/result/0/result");
                if (!Result.Status.OK.validate(status.textValue())) {
                    return Mono.error(new IllegalStateException("Update failed: " + result.asText()));
                }
                if (!Helpers.exists(result)) {
                    return Mono.empty();
                }
                convertId(result.require());
                return Mono.just(JsonUtils.mapper().convertValue(result, type));
            });
    }

    private RecordId toRecordId(ID id) {
        if (this.idKind == null) {
            RecordId recordId = (RecordId) id;
            validateRecordId(recordId);
            return recordId;
        }
        switch (this.idKind) {
            case STRING:
                return new RecordId(this.table, Id.of((String) id));
            case LONG:
                return new RecordId(this.table, Id.of((Long) id));
            case UUID:
                return new RecordId(this.table, Id.of((UUID) id));
            default:
                throw new UnsupportedOperationException("Should never happen.");
        }
    }

    @SuppressWarnings("unchecked")
    private ID fromRecordId(RecordId id) {
        if (this.idKind == null) {
            return (ID) id;
        }
        return (ID) id.id.value;
    }

    private void convertId(ObjectNode objectNode) {
        JsonNode idNode = objectNode.at("/id");
        if (idNode.isPojo()) {
            POJONode idPojo = idNode.require();
            Object pojo = idPojo.getPojo();
            if (pojo instanceof RecordId) {
                ID id = fromRecordId(((RecordId) pojo));
                objectNode.putPOJO("id", id);
            }
        }
    }

    private RecordId takeRecordId(JsonNode entityNode) {
        JsonNode idNode = entityNode.at("/id");
        Helpers.state(Helpers.exists(idNode), "Upsert must specify id");
        RecordId recordId;
        if (idKind == null) {
            Helpers.state(idNode.isPojo(), "RecordId typed entity should have a RecordId kind `id`");
            recordId = (RecordId) ((POJONode) idNode.require()).getPojo();
            validateRecordId(recordId);
        } else if (idNode.isTextual() && idKind == Id.Kind.STRING) {
            recordId = RecordId.of(table.name, idNode.textValue());
        } else if (idNode.isNumber() && idKind == Id.Kind.LONG) {
            recordId = RecordId.of(table.name, idNode.asLong());
        } else if (idNode.isPojo() && idKind == Id.Kind.UUID) {
            recordId = RecordId.of(table.name, (UUID) ((POJONode) entityNode.require()).getPojo());
        } else {
            throw new IllegalArgumentException(String.format("Unsupported id kind, Kind: %s, IdType: %s", idKind, idNode.getNodeType().name()));
        }
        return recordId;
    }

    private void validateRecordId(RecordId recordId) {
        if (!this.table.name.equals(recordId.table.name)) {
            throw new IllegalArgumentException(String.format("RecordId table name not match, typed-client: `%s`, entity: `%s`", table.name, recordId.table.name));
        }
    }

}
