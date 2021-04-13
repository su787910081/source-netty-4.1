package com.source.suyh01.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SomeServerHandler extends SimpleChannelInboundHandler<String> {
    private int counter = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("from client msg, count: {}", ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("读写异常，关闭Channel.", cause);
        ctx.channel().close();
    }
}
