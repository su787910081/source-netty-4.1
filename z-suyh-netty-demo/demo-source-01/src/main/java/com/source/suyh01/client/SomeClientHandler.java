package com.source.suyh01.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SomeClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("from server msg: {}", msg);
        TimeUnit.MILLISECONDS.sleep(500);
        ctx.channel().writeAndFlush(LocalDateTime.now().toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("读写异常，关闭Channel", cause);
        ctx.channel().close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Channel 连接成功，开始通信.");
        ctx.channel().writeAndFlush("first message");

        for (int i = 0; i < 100; i++) {
            TimeUnit.MILLISECONDS.sleep(i);
            ctx.channel().writeAndFlush("no: " + i);
        }
    }
}
