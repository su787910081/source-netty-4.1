## 长度域预处理器：`LengthFieldPrepender`

> ```java
> // lengthFieldLength: 指定长度域的字节长度，可选值为: 1 2 4 8
> LengthFieldPrepender(int lengthFieldLength);
> 
> 
> ```
>
> - `lengthIncludesLengthFieldLength` 标志
>
>   > 长度中是否包含长度域的长度标志
>   >
>   > 就是指长度域中存放的值，是否需要将长度域的字节长度也加上去。



## `LengthFieldBasedFrameDecoder`

> ```java
>     /**
>      * Creates a new instance.
>      * 我们要知道解码之后的body 的起始计算位置有两种情况。
>      * 如果长度域被剥离掉了，那么body 将会从长度域之后开始计算。
>      *
>      * @param maxFrameLength      一个frame 的最大长度。
>      * @param lengthFieldOffset   长度域的偏移量。也就是说我们可以不将长度域放在最开头，
>      *                            我们可以在最开头添加一些乱七八糟的值，以防止被别人破解。
>      * @param lengthFieldLength   长度域的长度
>      * @param lengthAdjustment    长度矫正值。
>      *                            为什么要矫正？
>      *                            因为这个类是做解码工作的，解码之后与解码之前的数据包发生了变化。
>      *                            最终得到的解码之后的数据包的长度是多少是需要重新计算的。
>      *                            那么这个解码之后的数据包的长度的计算就是长度域中的值与该矫正值相加得到。
>      * @param initialBytesToStrip 从解码帧中要剥去的前面字节。
>      *                            就是将数据包中的前面的几个字节丢弃掉。
>      */
>     public LengthFieldBasedFrameDecoder(
>             int maxFrameLength,
>             int lengthFieldOffset, int lengthFieldLength,
>             int lengthAdjustment, int initialBytesToStrip);
> ```

`SelectorProvider`

> ```java
> /**
>  * 线程安全的
>  * 提供Channel 和selector
>  */
> public abstract class SelectorProvider;
> ```

`Selector`

> ```java
> /**
>  * 将一个Channel 注册到一个Selector 中的时候，是以SelectorKey 对象的方式注册的。
>  * 三个集合
>  * key set: 所有注册的key
>  * selected-key set: 已经就绪的key
>  * cancelled-key set: 调用SelectorKey.cancel() 或者Channel.close() 方法的时候会被添加到这个集合中。
>  *                    将要被删除的key，这些key 将会在下一个调用select 方法的时候从key set 中删除。
>  */
> public abstract class Selector implements Closeable;
> ```

`SelectionKey`

> ```java
> /**
>  * 每一个Channel 被注册之后都会有一个SelectionKey 被返回。
>  */
> public abstract class SelectionKey ;
> public static final int OP_READ = 1 << 0;
> public static final int OP_WRITE = 1 << 2;
> public static final int OP_CONNECT = 1 << 3;
> public static final int OP_ACCEPT = 1 << 4;
> ```

## I/O模型

<img src="md-png\IO模型.png"  />

- I/O调用线程

  > 用户调用的线程

- I/O执行线程

  > 大概可以理解为调用内核

- 同步/异步调用

  > 指的是I/O调用线程与I/O执行线程之间的同步与异步调用。

- 阻塞

  > 指的是`I/O调用线程`要等待`I/O执行线程`返回结果之后再执行后续操作。

- 非阻塞

  > 指的是`I/O调用线程`不需要等待`I/O执行线程`返回结果之后再执行后续操作。

- 异步阻塞IO模型(在实际生活中是没有实际意义的)

![](md-png\IO模型-同步非阻塞.png)

![](md-png\IO模型-同步非阻塞2.png)

![](md-png\Reactor模型的IO.png)



![](md-png\Proactor模型的IO.png)

![](md-png\NettyEpoll.png)



![](md-png\NettyProactor.png)