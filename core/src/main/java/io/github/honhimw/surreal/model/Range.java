package io.github.honhimw.surreal.model;

import java.io.Serializable;

/**
 * @author honhimW
 * @since 2025-04-28
 */

public class Range implements Serializable {

    private final Value lowerBound;

    private final Value upperBound;

    public Range(Value lowerBound, Value upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public static class Value implements Serializable {

        private final int value;

        public Value(int value) {
            this.value = value;
        }

        public boolean isIncluded() {
            return false;
        }

        public boolean isExcluded() {
            return false;
        }

    }

    public static class Included extends Value {

        public Included(int value) {
            super(value);
        }

        public boolean isIncluded() {
            return true;
        }

    }

    public static class Excluded extends Value {

        public Excluded(int value) {
            super(value);
        }

        public boolean isExcluded() {
            return true;
        }

    }

}
