package Marshalling;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by user-hfc on 2017/11/9.
 */
@ChannelHandler.Sharable
public class SubReqClientHandler extends ChannelHandlerAdapter
{
    @Override
    public void channelActive(ChannelHandlerContext ctx)
    {
        System.out.println("channelActive");
        for (int i=0; i<10; i++)
        {
            ctx.write(subReq(i));
        }
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        System.out.println("Receive server response : [" + msg + "]");
    }

    private SubscribeReq subReq(int i)
    {
        SubscribeReq req = new SubscribeReq();
        req.setAddress("ZheJiang HangZhou");
        req.setPhoneNumber("157********");
        req.setProductName("Netty Book For Marshalling");
        req.setSubReqID(i);
        req.setUserName("hfc");

        return req;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("channelReadComplete");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
