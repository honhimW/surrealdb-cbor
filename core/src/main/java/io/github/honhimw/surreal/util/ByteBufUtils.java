package io.github.honhimw.surreal.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * @author honhimW
 * @since 2025-04-27
 */

public class ByteBufUtils {


    public static Flux<ByteBuf> readInputStream(InputStream in) {
        ReadableByteChannel readableByteChannel = Channels.newChannel(in);
        return Flux.using(() -> readableByteChannel,
            channel -> Flux.generate(sink -> {
                try {
                    ByteBuffer bb = ByteBuffer.allocateDirect(1024);
                    int bytesRead = channel.read(bb);
                    if (bytesRead == -1) {
                        sink.complete();
                    } else {
                        sink.next(Unpooled.wrappedBuffer(bb));
                    }
                } catch (IOException ignored) {
                }
            }),
            channel -> {
                try {
                    channel.close();
                } catch (IOException ignored) {
                }
            });
    }

}
