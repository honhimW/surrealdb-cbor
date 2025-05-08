package io.github.honhimw.surreal.cbor;

import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import io.github.honhimw.surreal.ReactiveSurrealClient;
import io.github.honhimw.surreal.ReactiveTypedSurreal;
import io.github.honhimw.surreal.SurrealClient;
import io.github.honhimw.surreal.model.RecordId;
import io.github.honhimw.surreal.model.Response;
import io.github.honhimw.surreal.util.JsonUtils;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
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
            .blocking();
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

        ReactiveSurrealClient reactive = client.reactive();
        {
            ReactiveTypedSurreal<PersonLong, Long> person = reactive.i64("person", PersonLong.class);
            Mono<PersonLong> p = person.getById(1L);
            assert p.block().gender == false;
        }
        {
            ReactiveTypedSurreal<PersonString, String> person = reactive.string("person", PersonString.class);
            Mono<PersonString> p = person.getById("2");
            assert p.block().gender == false;
        }
        {
            ReactiveTypedSurreal<PersonUUID, UUID> person = reactive.uuid("person", PersonUUID.class);
            Mono<PersonUUID> p = person.getById(e3);
            assert p.block().gender == false;
        }
        {
            ReactiveTypedSurreal<PersonRecordId, RecordId> person = reactive.record(PersonRecordId.class);
            Mono<PersonRecordId> p = person.getById(RecordId.of("person", "foo"));
            assert p.block().gender == false;
        }
        {
            ReactiveTypedSurreal<PersonRecordId, RecordId> person = reactive.record(PersonRecordId.class);
            Flux<PersonRecordId> p = person.getByIds(List.of(
                RecordId.of("person", 1),
                RecordId.of("person2", "2"),
                RecordId.of("person", e3),
                RecordId.of("person2", "foo")
            ));
            List<PersonRecordId> block = p.collectList().block();
            log.info("{}", JsonUtils.toPrettyJson(block));
        }
    }

    @Test
    @SneakyThrows
    void upsert() {
        ReactiveTypedSurreal<PersonLong, Long> longClient = client.reactive().i64("person", PersonLong.class);
        PersonLong entity = new PersonLong();
        entity.id = 1L;
        entity.age = 20;
//        entity.gender = true;
        entity.person = new PersonLong();
        entity.person.gender = false;
        Mono<PersonLong> upsert = longClient.upsert(entity);
        log.info("{}", upsert.block());
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
