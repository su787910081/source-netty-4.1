package com.abc.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * 这些都是使用JDK 里面的接口和方法来实现的非阻塞TCP
 */
public class NioServer {
    public static void main(String[] args) throws Exception {
        // 创建一个服务端Channel
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        // 指定channel采用的为非阻塞模式
        serverChannel.configureBlocking(false);
        // 指定要监听的端口
        serverChannel.bind(new InetSocketAddress(8888));
        // 创建一个多路复用器selector
        Selector selector = Selector.open();
        // 将channel注册到selector，并告诉selector让其监听“接收Client连接事件”
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            // select()是一个阻塞方法，若阻塞1秒的时间到了，或在阻塞期间有channel就绪，都会打破阻塞
            if (selector.select(1000) == 0) {
                System.out.println("当前没有找到就绪的channel");
                continue;
            }

            // 代码能走到这里，说明已经有channel就绪
            // 获取所有就绪的channel的key
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            // 遍历所有就绪的key
            for (SelectionKey key : selectionKeys) {
                // 若当前key为OP_ACCEPT，则说明当前channel是可以接收客户端连接的。
                // 那么，这里的代码就是用于接收客户端连接的
                if (key.isAcceptable()) {
                    System.out.println("接收到Client的连接");
                    // 获取连接到Server的客户端channel，其是客户端channel在server端的代表（驻京办）
                    // suyh - 与客户端之间的通信Channel
                    SocketChannel clientChannel = serverChannel.accept();
                    System.out.println("remote host: " + clientChannel.getRemoteAddress());
                    clientChannel.configureBlocking(false);
                    // 将客户端channel注册到selector，并告诉selector让其监听这个channel中是否发生了读事件
                    clientChannel.register(selector, SelectionKey.OP_READ);
                }
                // 若当前key为OP_READ，则说明当前channel中有客户端发送来的数据。
                // 那么，这里的代码就是用于读取channel中的数据的
                // suyh - 在客户端关闭连接时，也是可读状态被触发。
                if (key.isReadable()) {
                    try {
                        // 创建buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        // 根据key获取其对应的channel
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        // 把channel中的数据读取到buffer
                        clientChannel.read(buffer);
                    } catch (Exception e) {
                        // 若在读取过程中发生异常，则直接取消该key，即放弃该channel
                        key.cancel();
                    }
                }

                // 删除当前处理过的key，以免重复处理
                // suyh - 这一步很重要，不能遗漏。
                // suyh - 在Selector 的注释上面写了，要么使用Set#remove(..) 方法，要么使用iterator#remove() 方法，禁止使用其它方法。
                selectionKeys.remove(key);
            } // end-for
            System.out.println("keys size: " + selectionKeys.size());
            // suyh - 这一步很重要，不能遗漏。
            // suyh - 把循环中删除的步骤拿到外面处理
            // selectionKeys.clear();   // suyh - Selector 注释上面注明了，禁止使用其它方法删除。
        }

    }
}
