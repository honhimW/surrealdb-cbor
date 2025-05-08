package io.github.honhimw.surreal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-04-28
 */

public class RecordId implements Serializable {
    
    public final Table table;
    
    public final Id id;

    @JsonCreator
    public RecordId(@JsonProperty("table") Table table, @JsonProperty("id") Id id) {
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

    @Override
    public String toString() {
        return table.name + ":" + id.toString();
    }
}
