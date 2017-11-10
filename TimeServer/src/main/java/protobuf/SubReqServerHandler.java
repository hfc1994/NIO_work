package protobuf;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by user-hfc on 2017/11/9.
 */
@ChannelHandler.Sharable
public class SubReqServerHandler extends ChannelHandlerAdapter
{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        //System.out.println("channelRead");
        SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq) msg;

        if ("hfc".equalsIgnoreCase(req.getUserName()))
        {
            System.out.println("Service accept client subscribe req : [" + req.toString() + "]");
            ctx.writeAndFlush(resp(req.getSubReqID()));
        }
    }

    private SubscribeRespProto.SubscribeResp resp(int subReqID)
    {
        SubscribeRespProto.SubscribeResp.Builder builder = SubscribeRespProto.SubscribeResp.newBuilder();
        builder.setSubReqID(subReqID);
        builder.setRespCode(0);
        builder.setDest("Netty book order succeed, 3 day later, sent to the address");
        return builder.build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
