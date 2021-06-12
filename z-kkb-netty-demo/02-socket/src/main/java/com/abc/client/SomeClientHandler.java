package com.abc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

// SimpleChannelInboundHandler中的channelRead()方法会自动释放接收到的来自于对方的msg所占有的所有资源。
//
// 若对方没有向自己发送数据，则自定义处理器建议继承自ChannelInboundHandlerAdapter。
// 因为若继承自SimpleChannelInboundHandler需要重写channelRead0()方法。
// 而重写该方法的目的是对来自于对方的数据进行处理。因为对方根本就没有发送数据，所以也就没有必要重写channelRead0()方法。
public class SomeClientHandler extends SimpleChannelInboundHandler<String> {

    // msg的消息类型与类中的泛型类型是一致的
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "，" + msg);
        ctx.channel().writeAndFlush("from client：" + LocalDateTime.now());
        TimeUnit.MILLISECONDS.sleep(500);
    }

    // 当Channel被激活后会触发该方法的执行
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush("from client：begin talking");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
