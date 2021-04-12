package com.suyh02.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SomeServerDemo02 {
    public static void main(String[] args) {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)  // 指定监听Channel
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 获取到连接channel
                            ChannelPipeline pipeline = ch.pipeline();
                            // 为连接channel 指定解析处理类
                            // 为连接channel 添加字符串的解码器与编码器
                            // 它们能将Channel 中的ByteBuf 与String 进行互转
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            // 为连接Channel 指定业务处理类
                            pipeline.addLast(new SomeServerHandler());
                        }
                    });
            // 使用sync() 将异步的bind() 同步等待。
            ChannelFuture future = bootstrap.bind(8888).sync();
            log.info("服务端启动成功，监听端口：{}", 8888);

            // 监听成功之后再做关闭标记
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            // 优雅关闭
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
