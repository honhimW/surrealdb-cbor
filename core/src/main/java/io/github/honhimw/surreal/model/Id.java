package io.github.honhimw.surreal.model;

import jakarta.annotation.Nonnull;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-04-28
 */

public abstract class Id implements Serializable {

    public static Id of(String id) {
        return new StringId(id);
    }

    public static Id of(long id) {
        return new LongId(id);
    }

    public static Id of(UUID id) {
        return new UuidId(id);
    }

    public boolean isLong() {
        return false;
    }

    public boolean isString() {
        return false;
    }

    public boolean isUuid() {
        return false;
    }

    @Nonnull
    public Kind kind() {
        return Kind.UNKNOWN;
    }

    public enum Kind {
        LONG, STRING, UUID, UNKNOWN
    }

    static class StringId extends Id {

        private final String id;

        StringId(String id) {
            this.id = id;
        }

        @Override
        public boolean isString() {
            return true;
        }

        @Nonnull
        @Override
        public Kind kind() {
            return Kind.STRING;
        }
    }

    static class LongId extends Id {

        private final long id;

        LongId(long id) {
            this.id = id;
        }

        @Override
        public boolean isLong() {
            return true;
        }

        @Nonnull
        @Override
        public Kind kind() {
            return Kind.LONG;
        }
    }

    static class UuidId extends Id {

        private final UUID id;

        UuidId(UUID id) {
            this.id = id;
        }

        @Override
        public boolean isUuid() {
            return true;
        }
    }

}
