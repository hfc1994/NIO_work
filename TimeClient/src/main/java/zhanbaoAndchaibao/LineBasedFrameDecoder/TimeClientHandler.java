package zhanbaoAndchaibao.LineBasedFrameDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

/**
 * Created by user-hfc on 2017/11/5.
 */
public class TimeClientHandler extends ChannelHandlerAdapter
{
    private int counter;

    private byte[] req;

    public TimeClientHandler()
    {
        req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)
    {
        ByteBuf message = null;
        for (int i = 0; i < 50; i++)
        {
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException
    {
//        ByteBuf buf = (ByteBuf) msg;
//        byte[] req = new byte[buf.readableBytes()];
//        buf.readBytes(req);
//        String body = new String(req, "utf-8");

        //加解码器之后
        String body = (String) msg;

        System.out.println("Now is : " + body + " ; the counter is : " + ++counter);;
    }

    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause)
    {
        System.out.println("Unexpected exception from downstream : " + cause.getMessage());;
        ctx.close();
    }
}
