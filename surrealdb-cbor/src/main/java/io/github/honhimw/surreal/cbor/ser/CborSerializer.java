package io.github.honhimw.surreal.cbor.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public abstract class CborSerializer<T> extends JsonSerializer<T> {

    public void serializeNested(Object value, CBORGenerator gen, SerializerProvider serializers) throws IOException {
        serializers.findTypedValueSerializer(value.getClass(), true, null)
            .serialize(value, gen, serializers);
    }

    public abstract void serialize(T value, CBORGenerator gen, SerializerProvider serializers) throws IOException;

    public final CborSerializer<T> replaceDelegatee(CborSerializer<?> delegatee) {
        return (CborSerializer<T>) super.replaceDelegatee(delegatee);
    }

    public void serializeWithType(T value, CBORGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        super.serializeWithType(value, gen, serializers, typeSer);
    }

    @Override
    public CborSerializer<T> unwrappingSerializer(NameTransformer unwrapper) {
        return (CborSerializer<T>) super.unwrappingSerializer(unwrapper);
    }

    @Override
    public CborSerializer<?> withFilterId(Object filterId) {
        return (CborSerializer<?>) super.withFilterId(filterId);
    }

    @Override
    public CborSerializer<?> withIgnoredProperties(Set<String> ignoredProperties) {
        return (CborSerializer<?>) super.withIgnoredProperties(ignoredProperties);
    }

    @Override
    public final CborSerializer<T> replaceDelegatee(JsonSerializer<?> delegatee) {
        return this.replaceDelegatee((CborSerializer<?>) delegatee);
    }

    @Override
    public final void serializeWithType(T value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        this.serializeWithType(value, (CBORGenerator) gen, serializers, typeSer);
    }

    @Override
    public final void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        this.serialize(value, (CBORGenerator) gen, serializers);
    }

    @Override
    public Class<T> handledType() {
        return super.handledType();
    }

    @Override
    public boolean isEmpty(T value) {
        return super.isEmpty(value);
    }

    @Override
    public boolean isEmpty(SerializerProvider serializers, T value) {
        return super.isEmpty(serializers, value);
    }

    @Override
    public boolean usesObjectId() {
        return super.usesObjectId();
    }

    @Override
    public boolean isUnwrappingSerializer() {
        return super.isUnwrappingSerializer();
    }

    @Override
    public CborSerializer<?> getDelegatee() {
        return (CborSerializer<?>) super.getDelegatee();
    }

    @Override
    public Iterator<PropertyWriter> properties() {
        return super.properties();
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType type) throws JsonMappingException {
        super.acceptJsonFormatVisitor(visitor, type);
    }
}
