package io.github.honhimw.surreal.client;

/**
 * @author honhimW
 * @since 2025-04-27
 */
public enum Protocol {

    HTTP("http"),

    HTTPS("https"),

    WS("ws"),

    WSS("wss"),

    /**
     * Requires: com.surrealdb:surrealdb-java
     */
    MEMORY("memory"),
    ;

    private final String raw;

    Protocol(String raw) {
        this.raw = raw;
    }

    @Override
    public String toString() {
        return raw;
    }
}
