package io.github.honhimw.surreal.cbor;

import com.surrealdb.Response;
import com.surrealdb.Surreal;
import com.surrealdb.signin.Root;
import io.github.honhimw.surreal.SurrealClient;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author honhimW
 * @since 2025-05-09
 */

public class BenchMarkTests {

    @Test
    @SneakyThrows
    void run() {
        Options options = new OptionsBuilder()
            .include(This.class.getSimpleName())
            .include(OfficialSdkWs.class.getSimpleName())
            .include(OfficialSdkHttp.class.getSimpleName())
            .forks(1)
            .resultFormat(ResultFormatType.LATEX)
            .build();

        new Runner(options).run();
    }

    @State(Scope.Benchmark)
    public static abstract class BenchMark {

        @Param({"512"})
//        @Param({"512", "1024", "2048"})
        public int dataSize;

        @Param({"10.37.1.132"})
        public String host;

        @Param({"8000"})
        public int port;

        @Param({"surrealsdk"})
        public String namespace;

        @Param({"surrealsdk"})
        public String database;

        @Param({"root"})
        public String username;

        @Param({"root"})
        public String password;

        String text;

        List<Float> embedding;

        @SneakyThrows
        @Setup(Level.Trial)
        public void setup() {
            text = RandomStringUtils.insecure().nextAlphanumeric(dataSize);
            RandomUtils insecure = RandomUtils.insecure();
            embedding = new ArrayList<>(dataSize);
            for (int i = 0; i < dataSize; i++) {
                float v = insecure.randomFloat(0, 10);
                embedding.add(i, v);
            }
        }

        @TearDown(Level.Trial)
        public void tearDown() {

        }

        @Benchmark
        @BenchmarkMode(Mode.AverageTime)
        @OutputTimeUnit(TimeUnit.MILLISECONDS)
        @Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
        @Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
        @Threads(4)
        @Fork(2)
        public void ping() {
            Sql sql = new Sql("""
                RETURN {
                  content: $content,
                  embedding: $embedding,
                };
                """, Map.of(
                "content", text,
                "embedding", embedding
            ));
            execute(sql);
        }

        public void execute(Sql sql) {

        }
    }

    public record Sql(String sql, Map<String, Object> param) {

    }

    public static class This extends BenchMark {

        SurrealClient client;

        @Override
        public void setup() {
            super.setup();
            client = SurrealClient.builder()
                .host(host)
                .port(port)
                .namespace(namespace)
                .database(database)
                .username(username)
                .password(password)
                .blocking();
        }

        @Override
        public void execute(Sql sql) {
            io.github.honhimw.surreal.model.Response response = client.sql(sql.sql, sql.param);
            response.last();
        }
    }

    public static class OfficialSdkWs extends BenchMark {

        Surreal surreal;

        @Override
        public void setup() {
            super.setup();
            surreal = new Surreal();
            surreal.connect("ws://%s:%d".formatted(host, port));
            surreal.useNs(namespace);
            surreal.useDb(database);
            surreal.signin(new Root(username, password));
        }

        @Override
        public void execute(Sql sql) {
            Response query = surreal.queryBind(sql.sql, sql.param);
            query.take(0);
        }
    }

    public static class OfficialSdkHttp extends BenchMark {

        Surreal surreal;

        @Override
        public void setup() {
            super.setup();
            surreal = new Surreal();
            surreal.connect("http://%s:%d".formatted(host, port));
            surreal.useNs(namespace);
            surreal.useDb(database);
            surreal.signin(new Root(username, password));
        }

        @Override
        public void execute(Sql sql) {
            Response query = surreal.queryBind(sql.sql, sql.param);
            query.take(0);
        }
    }

}
