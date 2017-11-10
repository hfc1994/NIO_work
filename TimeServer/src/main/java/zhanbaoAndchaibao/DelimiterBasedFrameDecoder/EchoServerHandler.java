package zhanbaoAndchaibao.DelimiterBasedFrameDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by user-hfc on 2017/11/5.
 */
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelHandlerAdapter
{
    int counter = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        String body = (String) msg;
        System.out.println("This is " + ++counter + " times receives client : [" + body + "]");
        body += "$_";
        ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
        ctx.writeAndFlush(echo);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
