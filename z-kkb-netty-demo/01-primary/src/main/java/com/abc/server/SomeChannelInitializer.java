package com.abc.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

// 管道初始化器
// 当前类的实例在pipeline初始化完毕后就会被GC
public class SomeChannelInitializer extends ChannelInitializer<SocketChannel> {

    // 当Channel初始创建完毕后就会触发该方法的执行，用于初始化Channel
    // suyh - 当客户端连接上来之后就会调用该方法，该方法需要指定通信解析方式。即：通信处理协议。
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // suyh - 这里跑出来过，这个ch 的类型是 NioSocketChannel.class
        // 从Channel中获取pipeline
        ChannelPipeline pipeline = ch.pipeline();
        // 将HttpServerCodec处理器放入到pipeline的最后
        // HttpServerCodec是什么？是HttpRequestDecoder与HttpResponseEncoder的复合体
        // HttpRequestDecoder：http请求解码器，将Channel中的ByteBuf数据解码为HttpRequest对象
        // HttpResponseEncoder：http响应编码器，将HttpResponse对象编码为将要在Channel中发送的ByteBuf数据
        // suyh - 解码：二进制数据(ByteBuf)  -->  实体对象(HttpRequest)
        // suyh - 编码：实体对象(httpResponse)  -->  二进制数据(ByteBuf)
        pipeline.addLast(new HttpServerCodec());
        // suyh - 将一个解码器放到pipeline 的最后，并为其指定一个名称。
        // pipeline.addLast("new", new HttpServerCodec());
        // 将自定义的处理器放入到Pipeline的最后
        pipeline.addLast(new SomeServerHandler());
    }
}
