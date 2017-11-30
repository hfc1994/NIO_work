package NettyProtocol;

import NettyProtocol.struct.Header;
import NettyProtocol.struct.NettyMessage;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by user-hfc on 2017/11/19.
 */
public class LoginAuthReqHandler extends ChannelHandlerAdapter
{
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("login channelActive");
        ctx.writeAndFlush(buildLoginReq());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        System.out.println("login channelRead");
        NettyMessage message = (NettyMessage) msg;

        //如果是握手应答消息，需要判断是否认证成功
        if (null != message.getHeader() && message.getHeader().getType() == MessageType.LOGIN_RESP.value())
        {
            byte loginResult = (byte) message.getBody();
            if (loginResult != (byte) 0)
            {
                //握手失败，关闭连接
                ctx.close();
            }
            else
            {
                System.out.println("login is ok : " + message);
                ctx.fireChannelRead(msg);
            }
        }
        else
        {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildLoginReq()
    {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.value());
        /*-----*/
//        header.setLength(12);
//        header.setSessionID(1234567890L);
//        header.setPriority((byte)4);
//        message.setBody("12345678900987");
        /*------*/

        message.setHeader(header);
        System.out.println(message.toString());
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        ctx.fireExceptionCaught(cause);
    }
}
