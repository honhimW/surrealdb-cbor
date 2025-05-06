package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.honhimw.surreal.cbor.SurrealCustomTag;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Converters {

    public static Object convert(int tag, JsonNode node) {
        SurrealCustomTag surrealCustomTag = SurrealCustomTag.of(tag);
        if (surrealCustomTag != null) {
            switch (surrealCustomTag) {
                case TAG_6:
                    return Tag6Converter.INSTANCE.convert(node);
                case TAG_7:
                    return Tag7Converter.INSTANCE.convert(node);
                case TAG_8:
                    return Tag8Converter.INSTANCE.convert(node);
                case TAG_10:
                    return Tag10Converter.INSTANCE.convert(node);
                case TAG_12:
                    return Tag12Converter.INSTANCE.convert(node);
                case TAG_14:
                    return Tag14Converter.INSTANCE.convert(node);
                case TAG_37:
                    return Tag37Converter.INSTANCE.convert(node);
                case TAG_49:
                    return Tag49Converter.INSTANCE.convert(node);
                case TAG_50:
                    return Tag50Converter.INSTANCE.convert(node);
                case TAG_51:
                    return Tag51Converter.INSTANCE.convert(node);
                case TAG_88:
                    return Tag88Converter.INSTANCE.convert(node);
                case TAG_89:
                    return Tag89Converter.INSTANCE.convert(node);
                case TAG_90:
                    return Tag90Converter.INSTANCE.convert(node);
                case TAG_91:
                    return Tag91Converter.INSTANCE.convert(node);
                case TAG_92:
                    return Tag92Converter.INSTANCE.convert(node);
                case TAG_93:
                    return Tag93Converter.INSTANCE.convert(node);
                case TAG_94:
                    return Tag94Converter.INSTANCE.convert(node);
            }
        }
        return node;
    }

}
