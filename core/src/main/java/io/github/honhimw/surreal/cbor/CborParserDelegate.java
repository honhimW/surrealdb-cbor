package io.github.honhimw.surreal.cbor;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.core.util.JsonParserDelegate;
import com.fasterxml.jackson.dataformat.cbor.CBORParser;

import java.lang.reflect.Method;

/**
 * @author honhimW
 * @since 2025-04-28
 */

public class CborParserDelegate extends JsonParserDelegate {

    private final Method method;

    protected final CBORParser delegate;

    public CborParserDelegate(CBORParser d) {
        super(d);
        this.delegate = d;
        try {
            this.method = ParserMinimalBase.class.getDeclaredMethod("_updateToken", JsonToken.class);
            this.method.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JsonToken updateToken(JsonToken jsonToken) {
        try {
            return (JsonToken) method.invoke(delegate, jsonToken);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public CBORParser delegate() {
        return (CBORParser) super.delegate();
    }

    public int getCurrentTag() {
        return delegate().getCurrentTag();
    }

    public CBORParser.TagList getCurrentTags() {
        return delegate().getCurrentTags();
    }

}
