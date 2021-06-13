package com.suyh01.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SomeServerDemo01 {
    public static void main(String[] args) {
        // 用于处理客户端连接请求，将请求发送给ChildGroup中的eventLoop
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        // 用于处理客户端请求
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup)    // 指定eventLoopGroup
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .attr(AttributeKey.valueOf("depart"), "市场部")
                    .childAttr(AttributeKey.valueOf("number"), 50)
                    .channel(NioServerSocketChannel.class)  // 指定使用NIO 进行通信
                    .childHandler(new SomeChannelInitializer());

            /*
             * 指定当前服务端所监听的端口号
             * bind() 方法的执行是异步的
             * sync() 方法会使bind() 操作与后续的代码的执行由异步变为同步
             */
            ChannelFuture future = bootstrap.bind(8888).sync();
            log.info("服务器启动成功，监听端口是：8888");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            // 优雅的关闭
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
