package io.github.honhimw.surreal;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author honhimW
 * @since 2025-05-07
 */

public interface QueryIdGenerator {

    int generate();

    class Default implements QueryIdGenerator {

        private final AtomicInteger counter = new AtomicInteger(0);

        @Override
        public int generate() {
            return counter.getAndIncrement();
        }
    }

}
