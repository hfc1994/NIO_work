package NettyProtocol;

import NettyProtocol.codec.NettyMessageDecoder;
import NettyProtocol.codec.NettyMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by user-hfc on 2017/11/19.
 */
public class NettyClient
{
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    EventLoopGroup group = new NioEventLoopGroup();
    public void connect(int port, String host) throws Exception
    {
        //EventLoopGroup group = new NioEventLoopGroup();
        //配置客户端NIO线程组
        try
        {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception
                        {
                            ch.pipeline().addLast(new NettyMessageDecoder(1024*1024, 4, 4));
                            ch.pipeline().addLast(new NettyMessageEncoder());
                            ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
                            ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler());
                            ch.pipeline().addLast("HeartBeatHandler", new HeartBeatReqHandler());
                        }
                    });

            //发起异步操作,此处绑定本地端口，主要用于服务端重复登录保护
//            ChannelFuture f = b.connect(new InetSocketAddress(host, port),
//                    new InetSocketAddress(NettyConstant.LOCALIP,
//                    NettyConstant.LOCAL_PORT)).sync();
            ChannelFuture f = b.connect(host,port).sync();
//            ChannelFuture f = b.connect(host, port).sync();

            System.out.println("connect");
            f.channel().closeFuture().sync();
            System.out.println("close");
        }
        finally
        {
            //所有资源释放完成之后，清空资源，再次发起重连操作
            executor.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        //group.shutdownGracefully();
                        System.out.println("异常重连");
                        TimeUnit.SECONDS.sleep(5);
                        //发起重连操作
                        connect(NettyConstant.PORT, NettyConstant.REMOTEIP);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void main(String[] args) throws Exception
    {
        new NettyClient().connect(NettyConstant.PORT, NettyConstant.REMOTEIP);
    }
}
