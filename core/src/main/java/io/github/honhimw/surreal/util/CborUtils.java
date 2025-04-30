package io.github.honhimw.surreal.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.cbor.CBORParser;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.honhimw.surreal.cbor.CustomTagsCborParser;
import io.github.honhimw.surreal.cbor.ser.CustomTagDurationSerializer;
import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author honhimW
 * @since 2025-04-30
 */

public class CborUtils {


    private static final CBORMapper MAPPER = newMapper();

    public static CBORMapper mapper() {
        return MAPPER;
    }

    public static CBORMapper newMapper() {
        return defaultBuilder().build();
    }

    public static CBORMapper.Builder defaultBuilder() {
        CBORMapper.Builder builder = CBORMapper.builder();

        builder
            .addModules(timeModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
            .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
            .defaultTimeZone(TimeZone.getTimeZone(DateTimeUtils.DEFAULT_ZONE_OFFSET))
        ;
        return builder;
    }

    public static JavaTimeModule timeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Duration.class, CustomTagDurationSerializer.INSTANCE);
        return javaTimeModule;
    }

    public static byte[] encode(Object obj) {
        try {
            return MAPPER.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("can't serialize as cbor bytes.", e);
        }
    }

    @Nonnull
    public static <T> T decode(byte[] bytes, Class<T> clazz) {
        return decode(bytes, MAPPER.constructType(clazz));
    }

    @Nonnull
    public static <T> T decode(byte[] bytes, Type type) {
        return decode(bytes, MAPPER.constructType(type));
    }

    @Nonnull
    public static <T> T decode(byte[] bytes, TypeReference<T> type) {
        return decode(bytes, MAPPER.constructType(type));
    }

    @Nonnull
    public static <T> T decode(byte[] bytes, JavaType javaType) {
        try (CBORParser parser = (CBORParser) MAPPER.createParser(bytes)) {
            CustomTagsCborParser delegate = new CustomTagsCborParser(parser);
            return MAPPER.readerFor(javaType).readValue(delegate);
        } catch (IOException e) {
            throw new IllegalArgumentException("can't deserialize from json.", e);
        }
    }

    @Nonnull
    public static Map<String, Object> readAsMap(byte[] bytes) {
        if (bytes != null) {
            try (CBORParser parser = (CBORParser) MAPPER.createParser(bytes)) {
                CustomTagsCborParser delegate = new CustomTagsCborParser(parser);
                return MAPPER.readerForMapOf(Object.class).readValue(delegate);
            } catch (IOException e) {
                throw new IllegalArgumentException("can't deserialize from json", e);
            }
        }
        return MAPPER.nullNode().require();
    }

    @Nonnull
    public static <T extends JsonNode> T readTree(byte[] bytes) {
        if (bytes != null) {
            try (CBORParser parser = (CBORParser) MAPPER.createParser(bytes)) {
                CustomTagsCborParser delegate = new CustomTagsCborParser(parser) {
                    @Override
                    public void handleCustomTag(int tag) throws IOException {
                        System.out.println(getParsingContext().pathAsPointer() + ": " + tag);
                        super.handleCustomTag(tag);
                    }
                };
                JsonNode jsonNode = MAPPER.readTree(delegate);
                return jsonNode.require();
            } catch (IOException e) {
                throw new IllegalArgumentException("can't deserialize from json", e);
            }
        }
        return MAPPER.nullNode().require();
    }

}
