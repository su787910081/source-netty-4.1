package com.abc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

// 服务器启动类
public class SomeServer {
    public static void main(String[] args) throws InterruptedException {

        // 用于处理客户端连接请求，将请求发送给childGroup中的eventLoop
        // suyh - 理解为监听socket的封装
        // suyh - NioEventLoopGroup 基于Nio 的selector
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        // 用于处理客户端请求
        // suyh - 理解为连接成功的socket 的封装
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            // 用于启动ServerChannel，粘合剂
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup)  // 指定eventLoopGroup
                    // suyh - 该方法将创建一个Nio channel 实例，Server Socket
                    .channel(NioServerSocketChannel.class)  // 指定使用NIO进行通信
                    // suyh - 当有客户端连接上来时，将会调用ChannelHandler 中的某一个方法
                    // suyh - 这里表现出来的就是调用ChannelInitializer.initChannel() 方法进行初始化
                    // suyh - 在这个初始化中，需要指定通信的数据解析方式。即：通信协议
                    // suyh - 因为SomeChannelInitializer 这个对象在创建出来之后就被 GC 掉了，所以一般不这样new
                    // suyh - 更多的是像01-primary2 中的那样实现一个匿名内部类在这里。
                    .childHandler(new SomeChannelInitializer());   // 指定childGroup中的eventLoop所绑定的线程所要处理的处理器

            // 指定当前服务器所监听的端口号
            // bind()方法的执行是异步的
            // sync()方法会使bind()操作与后续的代码的执行由异步变为了同步
            ChannelFuture future = bootstrap.bind(8888).sync();
            System.out.println("服务器启动成功。监听的端口号为：8888");
            // 关闭Channel
            // closeFuture()的执行是异步的。
            // 当Channel调用了close()方法并关闭成功后才会触发closeFuture()方法的执行
            future.channel().closeFuture().sync();
            System.out.println("已关闭！！！");
        } finally {
            // 优雅关闭
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
