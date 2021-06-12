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