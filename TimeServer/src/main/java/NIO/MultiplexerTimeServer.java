package NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by user-hfc on 2017/10/29.
 */
public class MultiplexerTimeServer implements Runnable
{
    private Selector selector;
    private ServerSocketChannel servChannel;

    private volatile boolean stop;

    /**
     * 初始化多路复用器，绑定监听端口
     */
    public MultiplexerTimeServer(int port)
    {
        try
        {
            selector = Selector.open();
            servChannel = ServerSocketChannel.open();
            servChannel.configureBlocking(false);
            servChannel.socket().bind(new InetSocketAddress(port),1024);
            servChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The time server is start in port : " + port);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop()
    {
        this.stop = true;
    }

    @Override
    public void run()
    {
        while (!stop)
        {
            try
            {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext())
                {
                    key = it.next();
                    it.remove();
                    try
                    {
                        handleInput(key);
                    }
                    catch (Exception e)
                    {
                        if (null != key)
                        {
                            key.cancel();
                            if (null != key.channel())
                            {
                                key.channel().close();
                            }
                        }
                    }
                }
            }
            catch (Exception e2)
            {
                e2.printStackTrace();
            }
        }

        //多路复用器关闭后，所有注册在上面的Channel和pipe等资源都会被自动去注册并关闭
        // 所以不需要重复释放资源
        if (null != selector)
        {
            try
            {
                selector.close();
            }
            catch (Exception e3)
            {
                e3.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException
    {
        //处理新接入的请求信息
        if (key.isAcceptable())
        {
            //Accept the new connection
            ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
            SocketChannel sc = ssc.accept();
            sc.configureBlocking(false);
            //Add the new connection to the selector
            sc.register(selector, SelectionKey.OP_READ);
        }

        if (key.isReadable())
        {
            //Read the data
            SocketChannel sc = (SocketChannel)key.channel();
            //KB
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);

            /**
             * 由于我们已经将SocketChannel设置为异步非阻塞模式，因此这里的read()是非阻塞的
             * 使用返回值进行判断，有三种可能的结果
             * 返回值大于0：读取到字节，对字节进行编解码
             * 返回值等于0：没有读取到字节，属于正常场景，忽略
             * 返回值等于-1：链路已经关闭，需要关闭SocketChannel，释放资源
             */
            int readBytes = sc.read(readBuffer);
            if (readBytes > 0)
            {
                //把buffer的当前位置更改成buffer缓冲区的第一个位置
                //用于后续对缓冲区的读取操作
                readBuffer.flip();
                byte[] bytes = new byte[readBuffer.remaining()];
                readBuffer.get(bytes);
                String body = new String(bytes,"utf-8");
                System.out.println("The time server receive order : " + body);
                String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)
                        ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                doWrite(sc, currentTime);
            }
            else if (readBytes < 0)
            {
                //对端链路关闭
                key.cancel();
                sc.close();
            }
            else
            {
                //读到0字节，忽略
            }
        }
    }

    private void doWrite(SocketChannel channel, String response) throws IOException
    {
        if (null != response && response.trim().length() > 0)
        {
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }
    }
}
