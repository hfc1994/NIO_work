package MessagePack;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by user-hfc on 2017/11/5.
 */
public class EchoClientHandler extends ChannelHandlerAdapter
{
    private final int sendNumber;

    public EchoClientHandler(int sendNumber)
    {
        this.sendNumber = sendNumber;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)
    {
        System.out.println("channelActive");
//        ByteBuf byteBuf = Unpooled.copiedBuffer("asdf".getBytes());
//        ctx.writeAndFlush(byteBuf);

        UserInfo[] infos = UserInfo();
        for (UserInfo infoE : infos)
        {
            ctx.write(infoE);
        }
        ctx.flush();
    }

    private UserInfo[] UserInfo()
    {
        UserInfo[] userInfos = new UserInfo[sendNumber];
        UserInfo userInfo = null;
        for (int i=0; i < sendNumber; i++)
        {
            userInfo = new UserInfo();
            userInfo.setAge(i);
            userInfo.setName("ABCDEFG --->" + i);
            userInfos[i] = userInfo;
        }
        return userInfos;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        System.out.println("Client receive the msgpack message : " + msg);
        //ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {
        ctx.flush();
    }
}
