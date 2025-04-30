package io.github.honhimw.surreal.cbor;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.cbor.CBORParser;
import io.github.honhimw.surreal.util.Err;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;

/**
 * @author honhimW
 * @since 2025-04-28
 */

public class CustomTagsCborParser extends CborParserDelegate {

    public CustomTagsCborParser(CBORParser d) {
        super(d);
        try {
            this.method = ParserMinimalBase.class.getDeclaredMethod("_updateToken", JsonToken.class);
            this.method.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final Method method;
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
            return Err.call(() -> (JsonToken) method.invoke(delegate, JsonToken.VALUE_EMBEDDED_OBJECT));
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

    private Object mapValue(int tag, TreeNode treeNode) {
        SurrealCustomTag customTag = SurrealCustomTag.of(tag);
        switch (customTag) {
            case TAG_12: {
                _assert(treeNode.isArray(), "TAG_12 should be an array with two items.");
                ArrayNode arrayNode = (ArrayNode) treeNode;
                long seconds = arrayNode.get(0).asLong(0);
                long nanoSeconds = arrayNode.get(1).asLong(0);
                return Instant.ofEpochSecond(seconds, nanoSeconds);
            }
            case TAG_14: {
                _assert(treeNode.isArray(), "TAG_14 should be an array with two items.");
                ArrayNode arrayNode = (ArrayNode) treeNode;
                long seconds = arrayNode.at("/0").asLong(0);
                long nanoSeconds = arrayNode.at("/1").asLong(0);
                return Duration.ofSeconds(seconds, nanoSeconds);
            }
            default:
                return treeNode;
        }
    }

    private void _assert(boolean state, String message) {
        if (!state) {
            throw new IllegalStateException(message);
        }
    }

}
