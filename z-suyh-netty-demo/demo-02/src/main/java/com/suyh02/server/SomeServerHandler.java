package com.suyh02.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SomeServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("from client msg: {}", msg);
        TimeUnit.MILLISECONDS.sleep(500);
        // 给客户端发送一个随机串
        ctx.channel().writeAndFlush(UUID.randomUUID().toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("数据处理异常，关闭channel", cause);
        ctx.close();
    }
}
