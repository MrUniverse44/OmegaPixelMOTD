package me.blueslime.pixelmotd.listener.motd.platforms.bungeecord.internal;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import me.blueslime.pixelmotd.utils.ByteBufContainer;

public class CachedBungeeServerPing {
    private ByteBuf buf;

    public CachedBungeeServerPing() {

    }

    private ByteBuf fromProtocol() {
        return buf;
    }

    /**
     * Write packet
     * @param channel of the packet
     */
    private void writePacket(Channel channel) {
        if (buf == null) {
            return;
        }

        channel.write(
                new ByteBufContainer(buf.retainedDuplicate()),
                channel.voidPromise()
        );
    }

    /**
     * Write and flush packet
     * @param channel of the packet
     */
    private void writeAndFlushPacket(Channel channel) {
        if (buf == null) {
            return;
        }
        channel.writeAndFlush(
                new ByteBufContainer(buf.retainedDuplicate()),
                channel.voidPromise()
        );
    }

}
