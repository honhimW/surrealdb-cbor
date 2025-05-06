package io.github.honhimw.surreal.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-04-28
 */

public class RecordId implements Serializable {
    
    public final Table table;
    
    public final Id id;

    public RecordId(Table table, Id id) {
        this.table = table;
        this.id = id;
    }

    public static RecordId of(String table, String id) {
        return new RecordId(Table.of(table), Id.of(id));
    }

    public static RecordId of(String table, long id) {
        return new RecordId(Table.of(table), Id.of(id));
    }

    public static RecordId of(String table, UUID id) {
        return new RecordId(Table.of(table), Id.of(id));
    }

}
