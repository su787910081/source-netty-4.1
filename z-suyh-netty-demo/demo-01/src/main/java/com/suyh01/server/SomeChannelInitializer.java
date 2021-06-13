package com.suyh01.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * 管道初始化
 * 当前类的实例在pipeline初始化完毕后就会被GC
 */
public class SomeChannelInitializer extends ChannelInitializer<SocketChannel> {
    /**
     * 当Channel 初始创建完毕后就会触发该方法的执行，用于初始化Channel
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 从Channel 中获取 pipeline
        ChannelPipeline pipeline = ch.pipeline();

        /*
         * 将HttpServerCodec 处理器放入到pipeline 的最后
         * HttpServerCodec 是什么？是HttpRequestDecoder与HttpResponseEncoder 的复合体
         * HttpRequestDecoder: http请求解码器，将Channel 中的ByteBuf数据解码为HttpRequest 对象
         * HttpResponseDecoder: http响应编码器，将HttpResponse 对象编码为将要在Channel 中发送的ByteBuf 数据
         */
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new SomeServerHandler());

        Attribute<Object> number = ch.attr(AttributeKey.valueOf("number"));
        System.out.println("number === " + number);
    }
}
