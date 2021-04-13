package com.suyh03.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SomeClientHandler extends SimpleChannelInboundHandler<String> {
    // msg 的类型与泛型一致
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
        log.info("服务器连接成功并激活，开始通信...");
        ctx.channel().writeAndFlush("first msg from client");
    }
}
