package io.github.honhimw.surreal;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author honhimW
 * @since 2025-05-07
 */

public interface QueryIdGenerator {

    long generate();

    class Default implements QueryIdGenerator {

        private final AtomicLong counter = new AtomicLong(0);

        @Override
        public long generate() {
            return counter.getAndIncrement();
        }
    }

}
