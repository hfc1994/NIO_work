package AIO;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

/**
 * Created by user-hfc on 2017/10/30.
 */
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer>
{
    private AsynchronousSocketChannel channel;

    public ReadCompletionHandler(AsynchronousSocketChannel channel)
    {
        if (null == this.channel)
        {
           this.channel = channel;
        }
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment)
    {
        attachment.flip();
        byte[] body = new byte[attachment.remaining()];
        attachment.get(body);
        try
        {
            String req = new String(body, "utf-8");
            System.out.println("The time server receive order : " + req);
            String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(req) ?
                    new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
            doWrite(currentTime);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void doWrite(String currentTime)
    {
        if (null != currentTime && currentTime.trim().length() > 0)
        {
            byte[] bytes = currentTime.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer, writeBuffer,
                    new CompletionHandler<Integer, ByteBuffer>()
                    {
                        @Override
                        public void completed(Integer result, ByteBuffer buffer)
                        {
                            //如果没有发送完成，继续发送
                            if (buffer.hasRemaining())
                                channel.write(buffer, buffer, this);
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment)
                        {
                            try
                            {
                                channel.close();
                            }
                            catch (Exception e2)
                            {
                                //ingnore on close
                            }
                        }
                    });
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment)
    {
        try
        {
            this.channel.close();
        }
        catch (Exception e3)
        {
            exc.printStackTrace();
        }
    }
}
