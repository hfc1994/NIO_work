package NettyProtocol.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteOutput;

import java.io.IOException;

/**
 * Created by user-hfc on 2017/11/19.
 */
public class ChannelBufferByteOutput implements ByteOutput
{
    private final ByteBuf buffer;

    public ChannelBufferByteOutput(ByteBuf buffer)
    {
        this.buffer = buffer;
    }


    public void write(int i) throws IOException
    {
        buffer.writeByte(i);
    }

    public void write(byte[] bytes) throws IOException
    {
        buffer.writeBytes(bytes);
    }

    public void write(byte[] bytes, int srcIndex, int length) throws IOException
    {
        buffer.writeBytes(bytes, srcIndex, length);
    }

    public void close() throws IOException {}

    public void flush() throws IOException {}

    ByteBuf getBuffer()
    {
        return buffer;
    }
}
