package com.suyh01.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义服务端处理器
 * 需求：用户提交一个请求后，在浏览器上就会看到 hello netty world
 */
@Slf4j
public class SomeServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 当Channel 中有来自于客户端的数据时就会触发该方法的执行
     *
     * @param ctx 上下文对象
     * @param msg 来自于客户端的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Attribute<Object> number = ctx.channel().attr(AttributeKey.valueOf("number"));
        System.out.println("number ------ " + number);

        log.info("------ {}", ctx.channel());

        log.info("msg class: {}", msg.getClass());
        log.info("客户端地址: {}", ctx.channel().remoteAddress());
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            log.info("请求方式：{}", request.method().name());
            log.info("请求uri: {}", request.uri());

            if ("/favicon.ico".equals(request.uri())) {
                log.info("不处理/favicon.ico 请求");
                return;
            }

            if ("/exception".equals(request.uri())) {
                throw new RuntimeException("throw a exception.");
            }

            // 构造response的响应体
            ByteBuf body = Unpooled.copiedBuffer("hello netty world", CharsetUtil.UTF_8);
            // 生成响应对象
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
            // 获取到response的头部后进行初始化
            HttpHeaders headers = response.headers();
            headers.set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            headers.set(HttpHeaderNames.CONTENT_LENGTH, body.readableBytes());

            // 将响应对象写入到Channel
            // ctx.write(response);
            // ctx.flush();
            // ctx.writeAndFlush(response);
            // ctx.channel().close();
            ChannelFuture future = ctx.writeAndFlush(response);
            // 当写成功之后，添加channel关闭监听器
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 当Channel 中的数据在处理过程中出现异常时会触发该方法执行
     *
     * @param ctx   上下文
     * @param cause 异常对象
     * @throws Exception 可以继续往外抛异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("数据处理时发生了异常。exceptionCaught.", cause);
        // 关闭Channel
        ctx.close();
    }
}
