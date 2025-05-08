package io.github.honhimw.surreal.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;
import io.github.honhimw.surreal.ReactiveTypedSurreal;
import io.github.honhimw.surreal.UpsertKind;
import io.github.honhimw.surreal.model.Id;
import io.github.honhimw.surreal.model.RecordId;
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
class ReactiveTypedSurrealImpl<T, ID> implements ReactiveTypedSurreal<T, ID> {

    private final ReactiveSurrealClientImpl client;

    private final Class<T> type;

    private final Table table;

    private final Id.Kind idKind;

    ReactiveTypedSurrealImpl(ReactiveSurrealClientImpl client, Class<T> type, @Nullable Table table, @Nullable Id.Kind idKind) {
        this.client = client;
        this.type = type;
        this.table = table;
        this.idKind = idKind;
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
        Map<String, List<RecordId>> recordIds = ids.stream()
            .map(this::toRecordId)
            .collect(Collectors.groupingBy(recordId -> recordId.table.name));

        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<String, List<RecordId>>> entries = recordIds.entrySet();
        Map<String, Object> params = new LinkedHashMap<>(entries.size());
        for (Map.Entry<String, List<RecordId>> entry : entries) {
            sb.append("SELECT * FROM ");
            String tableName = entry.getKey();
            sb.append(tableName).append(" WHERE id IN ");
            String paramName = tableName + "_ids";
            sb.append("$").append(paramName).append(";\n");

            List<RecordId> _ids = entry.getValue();
            params.put(paramName, _ids);
        }
        String sql = sb.toString();

        if (log.isDebugEnabled()) {
            log.debug("Select records by ids SQL:\n{}", sql);
        }

        return client.sqlBytes(sql, params)
            .map(bytes -> client.decodeAndThrowIfError(bytes, JsonNode.class))
            .flatMapMany(jsonNode -> {
                JsonNode statementResults = jsonNode.at("/result");
                if (!Helpers.exists(statementResults)) {
                    return Flux.empty();
                }
                ArrayNode statementResultArrayNode = statementResults.require();
                List<T> results = new ArrayList<>(ids.size());
                for (JsonNode resultNode : statementResultArrayNode) {
                    JsonNode result = resultNode.at("/result");
                    ArrayNode arrayNode = result.require();
                    for (JsonNode node : arrayNode) {
                        results.add(JsonUtils.mapper().convertValue(node, type));
                    }
                }
                return Flux.fromIterable(results);
            });

    }

    @Override
    public Mono<T> upsert(T entity, UpsertKind kind) {
        JsonNode entityNode = CborUtils.mapper().valueToTree(entity);
        JsonNode idNode = entityNode.at("/id");
        Helpers.state(Helpers.exists(idNode), "Upsert must specify id");
        RecordId recordId;
        if (table == null) {
            Helpers.state(idNode.isPojo(), "RecordId typed entity should have a RecordId kind `id`");
            recordId = (RecordId) ((POJONode) idNode.require()).getPojo();
        } else if (idNode.isTextual() && idKind == Id.Kind.STRING) {
            recordId = RecordId.of(table.name, idNode.textValue());
        } else if (idNode.isNumber() && idKind == Id.Kind.LONG) {
            recordId = RecordId.of(table.name, idNode.asLong());
        } else if (idNode.isPojo() && idKind == Id.Kind.UUID) {
            recordId = RecordId.of(table.name, (UUID) ((POJONode) entityNode.require()).getPojo());
        } else {
            throw new IllegalArgumentException("Unsupported id kind");
        }

        if (kind == UpsertKind.MERGE) {
            Helpers.removeNullEntries(entityNode);
        }

        Map<String, Object> content = new LinkedHashMap<>(1);
        content.put("content", entityNode);

        return client.sqlBytes("UPSERT ONLY " + recordId.table.name + " " + kind.name() + " $content;", content)
            .map(bytes -> client.decodeAndThrowIfError(bytes, JsonNode.class))
            .flatMap(jsonNode -> {
                JsonNode result = jsonNode.at("/result/0/result");
                if (!Helpers.exists(result)) {
                    return Mono.empty();
                }
                convertId(result.require());
                return Mono.just(JsonUtils.mapper().convertValue(result, type));
            });

    }

    private RecordId toRecordId(ID id) {
        if (this.idKind == null) {
            return (RecordId) id;
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

}
