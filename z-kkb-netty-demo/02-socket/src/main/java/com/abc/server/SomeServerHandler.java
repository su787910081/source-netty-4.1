package com.abc.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

// ChannelInboundHandlerAdapter中的channelRead()方法不会自动释放接收到的来自于对方的msg
//
// 若对方向自己发送了数据，而自己又需要将该数据再发送给对方，则自定义处理器建议继承自ChannelInboundHandlerAdapter。
// 因为write()方法的执行是异步的，且SimpleChannelInboundHandler中的channelRead()方法会自动释放掉来自于对方的msg。
// 若write()方法中正在处理msg，而此时SimpleChannelInboundHandler中的channelRead()方法执行完毕了，将msg给释放了。
// 此时就会报错。
public class SomeServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("channel = " + ctx.channel());

        // 将来自于客户端的数据显示在服务端控制台
        System.out.println(ctx.channel().remoteAddress() + "，" + msg);
        // 向客户端发送数据
        ctx.channel().writeAndFlush("from server：" + UUID.randomUUID());
        TimeUnit.MILLISECONDS.sleep(500);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
