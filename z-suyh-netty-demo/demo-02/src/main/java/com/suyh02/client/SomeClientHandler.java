package com.suyh02.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SomeClientHandler extends SimpleChannelInboundHandler<String> {
    // msg 的消息类型与类中的泛型类型是一致的
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("from server msg: {}", msg);
        TimeUnit.MILLISECONDS.sleep(500);
        ctx.channel().writeAndFlush(LocalDateTime.now().toString());
    }

    // 当Channel 被激活后会触发该方法的执行
    // 其实就是连接成功，可以进行通信了。
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush("fist msg");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("数据读取异常，关闭Channel", cause);
        ctx.close();
    }
}
