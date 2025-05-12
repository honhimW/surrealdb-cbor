package io.github.honhimw.surreal.cbor;

import io.github.honhimw.surreal.model.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * <a href="https://surrealdb.com/docs/surrealdb/integration/cbor">CBOR Protocol</a>
 *
 * @author honhimW
 * @since 2025-04-28
 */

public enum SurrealCustomTag {

//    TAG_0(0),
    /**
     * None
     */
    TAG_6(6, Option.class),

    /**
     * Table name
     */
    TAG_7(7, Table.class),

    /**
     * Record ID
     */
    TAG_8(8, RecordId.class),
    //    TAG_9(9, UUID.class),
    TAG_10(10, BigDecimal.class),

    /**
     * Datetime
     */
    TAG_12(12, Instant.class),
//    TAG_13(13),

    /**
     * Duration
     */
    TAG_14(14, Duration.class),

//    TAG_15(15, String.class),

    /**
     * UUID
     */
    TAG_37(37, UUID.class),

    /*
    Range
     */
    TAG_49(49, Range.class),
    TAG_50(50, Range.Included.class),
    TAG_51(51, Range.Excluded.class),

    /*
    Geometry
     */
    TAG_88(88, Geometry.Point.class),
    TAG_89(89, Geometry.Line.class),
    TAG_90(90, Geometry.Polygon.class),
    TAG_91(91, Geometry.MultiPoint.class),
    TAG_92(92, Geometry.MultiLine.class),
    TAG_93(93, Geometry.MultiPolygon.class),
    TAG_94(94, Geometry.Geometries.class),

    ;

    public final int tag;
    public final Class<?> type;

    SurrealCustomTag(int tag, Class<?> type) {
        this.tag = tag;
        this.type = type;
    }

    public static SurrealCustomTag of(int tag) {
        for (SurrealCustomTag customTag : SurrealCustomTag.values()) {
            if (customTag.tag == tag) {
                return customTag;
            }
        }
        return null;
    }

    public static SurrealCustomTag of(Class<?> type) {
        for (SurrealCustomTag customTag : SurrealCustomTag.values()) {
            if (customTag.type == type) {
                return customTag;
            }
        }
        return null;
    }

}
