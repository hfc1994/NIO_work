package zhanbaoAndchaibao.LineBasedFrameDecoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by user-hfc on 2017/11/1.
 */
public class TimeClient
{
    public void connect(int port, String host) throws Exception
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
                            //加解码器
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            socketChannel.pipeline().addLast(new StringDecoder());

                            socketChannel.pipeline().addLast(new TimeClientHandler());
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

        new TimeClient().connect(port, "127.0.0.1");
    }
}
