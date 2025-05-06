package io.github.honhimw.surreal.util;

/**
 * @author honhimW
 * @since 2025-04-30
 */

public class Try {

    public static <T> T call(Call<T> call) {
        try {
            return call.call();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void run(Run run) {
        try {
            run.run();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    @FunctionalInterface
    public interface Call<T> {
        T call() throws Throwable;
    }

    @FunctionalInterface
    public interface Run {
        void run() throws Throwable;
    }

}
