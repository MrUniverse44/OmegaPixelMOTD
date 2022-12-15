package me.blueslime.pixelmotd.utils;

import io.netty.buffer.ByteBuf;

public class ByteBufContainer {
    private ByteBuf byteBuf;

    public ByteBufContainer(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public void setByteBuf(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public ByteBuf getByteBuf() {
        return byteBuf;
    }
}
