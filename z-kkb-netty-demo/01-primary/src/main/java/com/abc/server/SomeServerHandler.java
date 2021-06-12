package com.abc.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

// 自定义服务端处理器
// 需求：用户提交一个请求后，在浏览器上就会看到hello netty world
public class SomeServerHandler extends ChannelInboundHandlerAdapter {

    /**
     *  当Channel中有来自于客户端的数据时就会触发该方法的执行
     * @param ctx  上下文对象
     * @param msg   就是来自于客户端的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("-------------- " + ctx.channel());


        System.out.println("msg = " + msg.getClass());
        System.out.println("客户端地址 = " + ctx.channel().remoteAddress());

        if(msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            System.out.println("请求方式：" + request.method().name());
            System.out.println("请求URI：" + request.uri());

            if("/favicon.ico".equals(request.uri())) {
                System.out.println("不处理/favicon.ico请求");
                return;
            }

            // 构造response的响应体
            ByteBuf body = Unpooled.copiedBuffer("hello netty world", CharsetUtil.UTF_8);
            // 生成响应对象
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
            // 获取到response的头部后进行初始化
            HttpHeaders headers = response.headers();
            headers.set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            headers.set(HttpHeaderNames.CONTENT_LENGTH, body.readableBytes());

            // 将响应对象写入到Channel
            // ctx.write(response);
            // ctx.flush();
            // ctx.writeAndFlush(response);
            // ctx.channel().close();
            ChannelFuture channelFuture = ctx.writeAndFlush(response);
            // 添加channel关闭监听器
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     *  当Channel中的数据在处理过程中出现异常时会触发该方法的执行
     * @param ctx  上下文
     * @param cause  发生的异常对象
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 关闭Channel
        // suyh - 这里的异常是服务端与客户端之间的一个连接的异常，这个异常只能管这个连接。
        // suyh - 所以这里的关闭也只是关闭这一个连接而以，并不会将这个服务器的监听socket 也给关闭掉。
        ctx.close();
    }
}
