package io.github.honhimw.surreal.cbor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.JsonGeneratorDelegate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import com.fasterxml.jackson.dataformat.cbor.CBORParser;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import io.github.honhimw.surreal.SurrealClient;
import io.github.honhimw.surreal.model.RecordId;
import io.github.honhimw.surreal.model.Response;
import io.github.honhimw.surreal.model.Result;
import io.github.honhimw.surreal.util.CborUtils;
import io.github.honhimw.surreal.util.JsonUtils;
import io.github.honhimw.surreal.util.UUIDUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

/**
 * @author honhimW
 * @since 2025-04-27
 */

public class CborTests {

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
            .host("127.0.0.1")
            .namespace("surrealdb")
            .database("surrealdb")
            .username("root")
            .password("root")
            .blocking();
    }

    @Test
    @SneakyThrows
    void write() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("foo", "bar");
        byte[] x = mapper.writeValueAsBytes(map);
        Map map1 = mapper.readValue(x, Map.class);
        System.out.println(map1);
    }

    @Test
    @SneakyThrows
    void parser() {
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString();
        byte[] uuidBytes = UUIDUtils.toBytes(uuid);
        System.out.println(Base64.getEncoder().encodeToString(uuidBytes));
        byte[] bytes = client.sqlBytes("""
            LET $uid = u'%s';
            UPSERT test SET id = $uid, name = "bar";
            RETURN {
                id: tab:hello,
                foo: "bar",
                now: time::now(),
                uuid_v4: rand::uuid::v4(),
                uuid_v7: rand::uuid::v7(),
                duration: duration::from::millis(2),
                embedding: $__embedding,
                uid: $uid,
                range: <array> [0..3],
                geos: [
                    {
                        type: "MultiPoint",
                        coordinates: [
                            [10.0, 11.2],
                            [10.5, 11.9]
                        ],
                    },
                    {
                        type: "Polygon",
                        coordinates: [[
                            [-0.38314819, 51.37692386], [0.1785278, 51.37692386],
                            [0.1785278, 51.61460570], [-0.38314819, 51.61460570],
                            [-0.38314819, 51.37692386]
                        ]]
                    },
                    {
                        type: "MultiPolygon",
                        coordinates: [
                            [
                                [ [10.0, 11.2], [10.5, 11.9], [10.8, 12.0], [10.0, 11.2] ]
                            ],
                            [
                                [ [9.0, 11.2], [10.5, 11.9], [10.3, 13.0], [9.0, 11.2] ]
                            ]
                        ]
                    }
                ],
            };
            """.formatted(uid), Map.of("__embedding", new float[]{0.123f, 0.234f, 0.321f}));
        JsonNode jsonNode1 = CborUtils.readTree(bytes);
        System.out.println("JsonUtils.toPrettyJson(jsonNode1) = " + JsonUtils.toPrettyJson(jsonNode1));
        byte[] encode = CborUtils.encode(jsonNode1);
        JsonNode jsonNode2 = CborUtils.readTree(encode);
        System.out.println("JsonUtils.toPrettyJson(jsonNode2) = " + JsonUtils.toPrettyJson(jsonNode2));
        Response decode = CborUtils.decode(encode, Response.class);
        System.out.println(decode.size());
    }

    @Test
    @SneakyThrows
    void generator() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CBORGenerator generator = (CBORGenerator) mapper.createGenerator(out);
        JsonGenerator gen = new JsonGeneratorDelegate(generator) {
            @Override
            public void writeEmbeddedObject(Object object) throws IOException {
                super.writeEmbeddedObject(object);
            }
        };
        mapper.writer().writeValue(gen, Duration.ofSeconds(1, 200));
        byte[] byteArray = out.toByteArray();

        CBORParser parser = (CBORParser) mapper.createParser(byteArray);
        CustomTagsCborParser jsonParserDelegate = new CustomTagsCborParser(parser) {
        };
        TreeNode treeNode = mapper.reader().readTree(jsonParserDelegate);
        if (treeNode instanceof JsonNode jsonNode) {
            System.out.println(JsonUtils.toPrettyJson(jsonNode));
        } else {
            System.out.println(treeNode.toString());
        }
    }

    @Test
    @SneakyThrows
    void rpc() {
        Object sql = client.sql("RETURN time::now();");
        System.out.println(sql);
    }

    @Test
    @SneakyThrows
    void content() {
        Response sql = client.sql("""
            UPSERT tmp CONTENT $content;
            """, Map.of("content", Map.of(
                "id", 1,
            "foo", "bar",
            "hello", "world"
        )));
        Result result = sql.last();
        System.out.println(result.toPrettyString());
        List<Content> contents = result.as(new TypeReference<>() {
        });
        System.out.println(contents.toString());
    }

    @Getter
    @Setter
    @ToString
    public static class Content {
        private RecordId id;
        private String foo;
        private String hello;
    }

}
