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
                    byte[] buffer = new byte[1024];
                    ByteBuffer dst = ByteBuffer.allocateDirect(1024);
                    int bytesRead = channel.read(dst);
                    if (bytesRead == -1) {
                        sink.complete();
                    } else {
                        sink.next(Unpooled.wrappedBuffer(dst));
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
