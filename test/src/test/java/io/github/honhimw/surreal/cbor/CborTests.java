package io.github.honhimw.surreal.cbor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.util.JsonGeneratorDelegate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import com.fasterxml.jackson.dataformat.cbor.CBORParser;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import io.github.honhimw.surreal.SurrealClient;
import io.github.honhimw.surreal.util.CborUtils;
import io.github.honhimw.surreal.util.JsonUtils;
import io.github.honhimw.surreal.util.UUIDUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
            .host("10.37.1.132")
            .build();
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
                embedding: [0.1, 0.2, 0.3],
                uid: $uid,
                range: <array> [0..3],
                coordinates: {
                    type: "MultiPoint",
                    coordinates: [
                        [10.0, 11.2],
                        [10.5, 11.9]
                    ],
                 },
            };
            """.formatted(uid));
        JsonNode jsonNode1 = CborUtils.readTree(bytes);
        System.out.println("JsonUtils.toPrettyJson(jsonNode1) = " + JsonUtils.toPrettyJson(jsonNode1));
        byte[] encode = CborUtils.encode(jsonNode1);
        JsonNode jsonNode2 = CborUtils.readTree(encode);
        System.out.println("JsonUtils.toPrettyJson(jsonNode2) = " + JsonUtils.toPrettyJson(jsonNode2));
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

}
