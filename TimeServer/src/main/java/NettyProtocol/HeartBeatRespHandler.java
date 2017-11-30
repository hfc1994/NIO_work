package NettyProtocol;

import NettyProtocol.struct.Header;
import NettyProtocol.struct.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by user-hfc on 2017/11/19.
 */
public class HeartBeatRespHandler extends ChannelHandlerAdapter
{
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("heart channel active");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        System.out.println("heart channelRead");

        NettyMessage message = (NettyMessage) msg;

        //返回心跳应答信息
        if (null != message.getHeader() && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value())
        {
            System.out.println("Receive client heart beat message : ---> " + message);
            NettyMessage heartBeat = buildHeartBeat();
            System.out.println("Send heart beat response message to client : ---> " + heartBeat);
            ctx.writeAndFlush(heartBeat);
        }
        else
        {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildHeartBeat()
    {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        message.setHeader(header);
        return message;
    }
}
