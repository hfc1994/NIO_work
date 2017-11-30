package NettyProtocol;

import NettyProtocol.struct.Header;
import NettyProtocol.struct.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by user-hfc on 2017/11/19.
 */
public class HeartBeatReqHandler extends ChannelHandlerAdapter
{
    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("heart channel active");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        System.out.println("heart channelRead");

//        System.out.println("主动断开连接");
//        ctx.close();

        NettyMessage message = (NettyMessage) msg;
        //握手成功，主动发送心跳消息
        if (null != message.getHeader() && message.getHeader().getType() == MessageType.LOGIN_RESP.value())
        {
            heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatReqHandler.HeartBeatTask(ctx),
                    0, 5000, TimeUnit.MILLISECONDS);
        }
        else if (null != message.getHeader() && message.getHeader().getType() == MessageType.HEARTBEAT_RESP.value())
        {
            System.out.println("Client receive server heart beat message : --->" + message);
        }
        else
        {
            ctx.fireChannelRead(msg);
        }
    }

    private class HeartBeatTask implements Runnable
    {
        private final ChannelHandlerContext ctx;

        public HeartBeatTask(final ChannelHandlerContext ctx)
        {
            this.ctx = ctx;
        }

        @Override
        public void run()
        {
            NettyMessage heartBeat = buildHeatBeat();
            System.out.println("Client send heart beat message to server : --->" + heartBeat);
            ctx.writeAndFlush(heartBeat);
        }
    }

    private NettyMessage buildHeatBeat()
    {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_REQ.value());
        message.setHeader(header);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        if (null != heartBeat)
        {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }
}
