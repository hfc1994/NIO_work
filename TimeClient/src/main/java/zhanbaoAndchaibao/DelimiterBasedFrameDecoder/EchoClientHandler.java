package zhanbaoAndchaibao.DelimiterBasedFrameDecoder;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by user-hfc on 2017/11/5.
 */
public class EchoClientHandler extends ChannelHandlerAdapter
{
    private int counter;
    static final String ECHO_REQ = "Hi, HuangFengChun.Welcome to Netty.$_";

    @Override
    public void channelActive(ChannelHandlerContext ctx)
    {
        for (int i=0; i<50;i++)
        {
            ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        System.out.println("This is " + ++counter + " times receive server : [" + msg + "]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
