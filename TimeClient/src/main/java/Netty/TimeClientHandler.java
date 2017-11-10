package Netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by user-hfc on 2017/11/1.
 */
public class TimeClientHandler extends ChannelHandlerAdapter
{
    private final ByteBuf firstMessage;

    public TimeClientHandler()
    {
        System.out.println("TimeClientHandler");
        byte[] req = "QUERY TIME ORDER".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
    }

    public void channelActive(ChannelHandlerContext ctx)
    {
        System.out.println("channelActive");
        ctx.writeAndFlush(firstMessage);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        System.out.println("channelRead");
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "utf-8");
        System.out.println("Now is : " + body);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        System.out.println("exceptionCaught");
        //释放资源
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
