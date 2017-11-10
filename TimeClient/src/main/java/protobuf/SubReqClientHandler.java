package protobuf;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user-hfc on 2017/11/9.
 */
@ChannelHandler.Sharable
public class SubReqClientHandler extends ChannelHandlerAdapter
{
    public SubReqClientHandler(){}

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

    private SubscribeReqProto.SubscribeReq subReq(int i)
    {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqID(i);
        builder.setUserName("hfc");
        builder.setProductName("Netty Book for protobuf");
        List<String> address = new ArrayList<String>();
        address.add("zhejiang hangzhou");
        address.add("shanghai");
        address.add("zhejiang jiangshan");
        builder.addAllAddress(address);

        return builder.build();
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
