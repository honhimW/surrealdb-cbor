package io.github.honhimw.surreal.cbor;

import com.surrealdb.Response;
import com.surrealdb.Surreal;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

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

}
