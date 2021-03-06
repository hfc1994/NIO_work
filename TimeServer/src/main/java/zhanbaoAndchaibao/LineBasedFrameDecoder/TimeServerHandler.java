package zhanbaoAndchaibao.LineBasedFrameDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by user-hfc on 2017/11/5.
 */
public class TimeServerHandler extends ChannelHandlerAdapter
{
    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException
    {
//        ByteBuf buf = (ByteBuf) msg;
//        byte[] req = new byte[buf.readableBytes()];
//        buf.readBytes(req);
//
//        String body = new String(req, "utf-8")
//                .substring(0, req.length - System.getProperty("line.separator").length());

        // 加解码器后
        String body = (String) msg;


        System.out.println("The time server receive order : " + body + " ; the counter is : " + ++counter);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)
                ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
        currentTime = currentTime + System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        ctx.close();
    }
}
