package io.github.honhimw.surreal.cbor;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.cbor.CBORParser;
import io.github.honhimw.surreal.cbor.deser.Converters;

import java.io.IOException;

/**
 * @author honhimW
 * @since 2025-04-28
 */

public class CustomTagsCborParser extends CborParserDelegate {

    public CustomTagsCborParser(CBORParser d) {
        super(d);
    }

    private Object customValue;

    public void handleCustomTag(int tag) throws IOException {
        ObjectCodec codec = getCodec();
        this.customValue = mapValue(tag, codec.readTree(this));
    }

    @Override
    public JsonToken nextToken() throws IOException {
        JsonToken jsonToken = super.nextToken();
        int currentTag = delegate.getCurrentTag();
        if (currentTag != -1) {
            handleCustomTag(currentTag);
            return updateToken(JsonToken.VALUE_EMBEDDED_OBJECT);
        }
        return jsonToken;
    }

    @Override
    public Object getEmbeddedObject() throws IOException {
        if (this.customValue != null) {
            Object v = this.customValue;
            this.customValue = null;
            return v;
        }
        return super.getEmbeddedObject();
    }

    private Object mapValue(int tag, JsonNode node) {
        return Converters.convert(tag, node);
    }

    private void _assert(boolean state, String message) {
        if (!state) {
            throw new IllegalStateException(message);
        }
    }

}
