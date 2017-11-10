package MessagePack;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;


/**
 * Created by user-hfc on 2017/11/5.
 */
public class EchoClient
{
    private final String host;
    private final int port;
    private final int sendNumber;

    public EchoClient(String host, int port, int sendNumber)
    {
        this.host = host;
        this.port = port;
        this.sendNumber = sendNumber;
    }

    public void connect() throws Exception
    {
        //配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try
        {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>()
                    {
                        protected void initChannel(SocketChannel socketChannel) throws Exception
                        {
                            socketChannel.pipeline().addLast("frameDecoder",
                                    new LengthFieldBasedFrameDecoder(65536, 0, 2, 0, 2));
                            socketChannel.pipeline().addLast("msgpack decoder", new MsgpackDecoder());
                            socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
                            socketChannel.pipeline().addLast("msgpack encoder", new MsgpackEncoder());
                            socketChannel.pipeline().addLast(new EchoClientHandler(sendNumber));
                        }
                    });
            //发起异步连接操作
            ChannelFuture f = b.connect(host, port).sync();

            System.out.println("connect");
            //等待客户端链路关闭
            f.channel().closeFuture().sync();
            System.out.println("close");
        }
        finally
        {
            //优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception
    {
        int port = 22233;
        if (null != args && args.length > 0)
        {
            try
            {
                port = Integer.parseInt(args[0]);
            }
            catch (Exception e)
            {
                port = 22233;
            }
        }

        new EchoClient("127.0.0.1", 22233, 100).connect();
    }
}
