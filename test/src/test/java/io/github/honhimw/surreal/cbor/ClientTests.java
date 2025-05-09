package io.github.honhimw.surreal.cbor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import io.github.honhimw.surreal.SurrealClient;
import io.github.honhimw.surreal.TypedSurrealClient;
import io.github.honhimw.surreal.UpsertKind;
import io.github.honhimw.surreal.model.RecordId;
import io.github.honhimw.surreal.model.Response;
import io.github.honhimw.surreal.model.Result;
import io.github.honhimw.surreal.util.JsonUtils;
import io.github.honhimw.surreal.util.ReactiveHttpUtils;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-05-08
 */

@Slf4j
public class ClientTests {

    private static CBORMapper mapper;

    private static SurrealClient client;

    @BeforeAll
    static void init() {
        mapper = CBORMapper.builder()
//            .findAndAddModules()
            .serializerFactory(JsonUtils.mapper().getSerializerFactory())
            .addModule(JsonUtils.timeModule())
            .build();
        client = SurrealClient.builder()
            .host("10.37.1.132")
            .namespace("surrealdb")
            .database("surrealdb")
            .username("root")
            .password("root")
            .http(ReactiveHttpUtils.getInstance(builder -> builder.filters(chainBuilder -> chainBuilder.addFilterBefore(ReactiveHttpUtils.Stage.EXECUTE, (chain, ctx) -> chain.doFilter(ctx)
                .doOnNext(httpResult -> {
                    Duration elapsed = ctx.get("elapsed");
                    log.info("{}", elapsed);
                })))))
            .blocking();
    }

    @Test
    @SneakyThrows
    void ping() {
        client.ping();
    }

    @Test
    @SneakyThrows
    void getById() {
        UUID e3 = UUID.randomUUID();
        List<Serializable> ids = List.of(1L, "2", e3, RecordId.of("person", "foo"));
        for (Serializable id : ids) {
            client.sql("UPSERT person CONTENT $content", Map.of(
                "content", Map.of(
                    "id", id,
                    "age", 19,
                    "gender", false
                )
            ));
            client.sql("UPSERT person2 CONTENT $content", Map.of(
                "content", Map.of(
                    "id", id,
                    "age", 19,
                    "gender", false
                )
            ));
        }

        {
            TypedSurrealClient<PersonLong, Long> person = client.i64("person", PersonLong.class);
            Optional<PersonLong> p = person.getById(1L);
            assert p.isPresent();
            assert p.get().gender == false;
        }
        {
            TypedSurrealClient<PersonString, String> person = client.string("person", PersonString.class);
            Optional<PersonString> p = person.getById("2");
            assert p.isPresent();
            assert p.get().gender == false;
        }
        {
            TypedSurrealClient<PersonUUID, UUID> person = client.uuid("person", PersonUUID.class);
            Optional<PersonUUID> p = person.getById(e3);
            assert p.isPresent();
            assert p.get().gender == false;
        }
        {
            TypedSurrealClient<PersonRecordId, RecordId> person = client.record("person", PersonRecordId.class);
            Optional<PersonRecordId> p = person.getById(RecordId.of("person", "foo"));
            assert p.isPresent();
            assert p.get().gender == false;
        }
        {
            TypedSurrealClient<PersonRecordId, RecordId> person = client.record("person", PersonRecordId.class);
            List<PersonRecordId> p = person.getByIds(List.of(
                RecordId.of("person", 1),
                RecordId.of("person", e3)
            ));
            log.info("{}", JsonUtils.toPrettyJson(p));
        }
    }

    @Test
    @SneakyThrows
    void upsert() {
        TypedSurrealClient<PersonLong, Long> longClient = client.i64("person", PersonLong.class);
        PersonLong entity = new PersonLong();
        entity.id = 1L;
        entity.age = 20;
//        entity.gender = true;
        entity.person = new PersonLong();
        entity.person.gender = false;
        PersonLong upsert = longClient.upsert(entity);
        log.info("{}", upsert);
    }

    @Test
    @SneakyThrows
    void error() {
        Response sql = client.sql("NONE.map();");
        assert sql.hasError();
        Result last = sql.last();
        assert last.isError();
    }

    @Test
    @SneakyThrows
    void schemaless() {
        {
            TypedSurrealClient<Map, Long> person = client.i64("person", Map.class);
            Map block = person.upsert(Map.of(
                "id", 1,
                "name", "foo"
            ));
            log.info("{}", block);
        }
        {
            ObjectNode objectNode = JsonUtils.mapper().createObjectNode();
            TypedSurrealClient<JsonNode, Long> person = client.i64("person", JsonNode.class);
            JsonNode block = person.upsert(objectNode
                .put("id", 1)
                .put("name", "bar")
            );
            log.info("{}", block);
        }
    }

    @Test
    @SneakyThrows
    void create() {
        TypedSurrealClient<PersonLong, Long> longClient = client.i64("person", PersonLong.class);
        PersonLong entity = new PersonLong();
        entity.id = 1L;
        entity.age = 20;
        entity.gender = true;
        Assertions.assertThrows(IllegalStateException.class, () -> longClient.create(entity));
    }

    @Test
    @SneakyThrows
    void delete() {
        TypedSurrealClient<PersonLong, Long> longClient = client.i64("person", PersonLong.class);
        longClient.delete(1L);
    }

    @Test
    @SneakyThrows
    void insert() {
        TypedSurrealClient<PersonLong, Long> longClient = client.i64("person", PersonLong.class);
        PersonLong entity = new PersonLong();
        entity.id = 12L;
        PersonLong entity2 = new PersonLong();
        entity2.id = 13L;
        List<PersonLong> insert = longClient.insert(entity, entity2);
        log.info("{}", insert);
    }

    @Test
    @SneakyThrows
    void update() {
        TypedSurrealClient<PersonString, String> longClient = client.string("person", PersonString.class);
        PersonString entity = new PersonString();
        entity.id = "jaime";
        entity.gender = false;
        PersonString insert = longClient.update(entity, UpsertKind.MERGE);
        log.info("{}", insert);
    }

    @ToString
    public static class PersonLong {
        public Long id;
        public Integer age;
        public Boolean gender;
        public PersonLong person;
    }

    @ToString
    public static class PersonString {
        public String id;
        public Integer age;
        public Boolean gender;
        public String name;
        public String surname;
    }

    @ToString
    public static class PersonUUID {
        public UUID id;
        public Integer age;
        public Boolean gender;
    }

    @ToString
    public static class PersonRecordId {
        public RecordId id;
        public Integer age;
        public Boolean gender;
    }

}
