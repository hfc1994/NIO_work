package MessagePack;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by user-hfc on 2017/11/5.
 */
public class EchoServerHandler extends ChannelHandlerAdapter
{
    @Override
    public void channelActive(ChannelHandlerContext ctx)
    {
        System.out.println("channelActive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        System.out.println("Server receive the mspack message : " + msg);
        ctx.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
