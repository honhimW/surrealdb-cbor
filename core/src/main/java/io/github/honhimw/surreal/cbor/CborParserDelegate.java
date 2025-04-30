package io.github.honhimw.surreal.cbor;

import com.fasterxml.jackson.core.util.JsonParserDelegate;
import com.fasterxml.jackson.dataformat.cbor.CBORParser;

/**
 * @author honhimW
 * @since 2025-04-28
 */

public class CborParserDelegate extends JsonParserDelegate {

    protected final CBORParser delegate;

    public CborParserDelegate(CBORParser d) {
        super(d);
        this.delegate = d;
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
