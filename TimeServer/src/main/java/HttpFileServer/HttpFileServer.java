package HttpFileServer;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by user-hfc on 2017/11/13.
 */
public class HttpFileServer
{
    private static final String DEFAULT_URL = "/src/";

    public void run(final int port, final String url) throws Exception
    {
        //配置服务端的NIO线程
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //.option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception
                        {
                            ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                            //将多个消息转换为单一的FullHttpRequest或者FullHttpResponse
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                            ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                            //支持异步发送大的码流，但不占用过多的内存
                            ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                            ch.pipeline().addLast("fileServerHandler",new HttpFileServerHandler(url));
                        }
                    });
            //绑定端口，同步等待成功
            ChannelFuture f = b.bind("127.0.0.1",port).sync();
            System.out.println("Http文件目录服务器启动，网址是：" + "http://127.0.0.1:" + port + url);

            //等待服务端监听端口关闭
            System.out.println("bind");
            f.channel().closeFuture().sync();
            System.out.println("close");
        }
        finally
        {
            //优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
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

        String url = DEFAULT_URL;
        if (args.length > 1)
            url = args[1];

        new HttpFileServer().run(port, url);
    }
}
