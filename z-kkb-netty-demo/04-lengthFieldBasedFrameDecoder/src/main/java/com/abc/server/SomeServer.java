package com.abc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

// 定义服务端启动类
public class SomeServer {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        NioEventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 基于长度域的帧解码器，用于对LengthFieldPrepender编码器编码后的数据进行解码的。
                            // 所以，首先要清楚LengthFieldPrepender编码器的编码原理。
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
                            pipeline.addLast(new LengthFieldPrepender(4, true));
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new SomeServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(8888).sync();
            System.out.println("服务器已启动");
            future.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();

        }
    }
//
//    /**
//     * Creates a new instance.
//     * 我们要知道解码之后的body 的起始计算位置有两种情况。
//     * 如果长度域被剥离掉了，那么body 将会从长度域之后开始计算。
//     *
//     * @param maxFrameLength      一个frame 的最大长度。
//     * @param lengthFieldOffset   长度域的偏移量。也就是说我们可以不将长度域放在最开头，
//     *                            我们可以在最开头添加一些乱七八糟的值，以防止被别人破解。
//     * @param lengthFieldLength   长度域的长度
//     * @param lengthAdjustment    长度矫正值。
//     *                            为什么要矫正？
//     *                            因为这个类是做解码工作的，解码之后与解码之前的数据包发生了变化。
//     *                            最终得到的解码之后的数据包的长度是多少是需要重新计算的。
//     *                            那么这个解码之后的数据包的长度的计算就是长度域中的值与该矫正值相加得到。
//     * @param initialBytesToStrip 从解码帧中要剥去的前面字节。
//     *                            就是将数据包中的前面的几个字节丢弃掉。
//     */
//    public LengthFieldBasedFrameDecoder(
//            int maxFrameLength,
//            int lengthFieldOffset, int lengthFieldLength,
//            int lengthAdjustment, int initialBytesToStrip) {
//    }
}
