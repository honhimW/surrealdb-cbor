package io.github.honhimw.surreal.cbor;

import com.surrealdb.Response;
import com.surrealdb.Surreal;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

/**
 * @author honhimW
 * @since 2025-04-27
 */

public class MemoryTests {

    @Test
    @SneakyThrows
    void multiRequest() {
        Surreal surreal = new Surreal();
        surreal.connect("memory");
        surreal.useNs("t");
        surreal.useDb("t");
        Response query = surreal.query("LET $q = 'hello';RETURN $q;");
        System.out.println(query.take(1));

        Response query1 = surreal.query("RETURN $q;");
        System.out.println(query1.take(0));
    }

    @Test
    @SneakyThrows
    void readBytes() {
        byte[] bytes = Files.readAllBytes(new File("E:\\downloads\\surrealdb-cbor-0.0.1-RC.0.jar.asc").toPath());
        byte[] bytes1 = Files.readAllBytes(new File("E:\\projects\\surrealdb-cbor\\surrealdb-cbor\\build\\libs\\surrealdb-cbor-0.0.1-RC.0.jar.asc").toPath());
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != bytes1[i]) {
                System.out.println();
            }
        }
        System.out.println();
    }

}
