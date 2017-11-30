package NettyProtocol.codec;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import org.jboss.marshalling.Marshaller;

import java.io.IOException;

/**
 * Created by user-hfc on 2017/11/19.
 */
@ChannelHandler.Sharable
public class MarshallingEncoder
{
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
    Marshaller marshaller;

    public MarshallingEncoder() throws IOException
    {
        marshaller = MarshallingCodeCFactory.buildMarshalling();
    }

    protected void encode(Object msg, ByteBuf out) throws Exception
    {
        try
        {
            int lengthPos = out.writerIndex();
            out.writeBytes(LENGTH_PLACEHOLDER);
            ChannelBufferByteOutput output = new ChannelBufferByteOutput(out);
            marshaller.start(output);
            marshaller.writeObject(msg);
            marshaller.finish();
            //setInt(index, value)
            out.setInt(lengthPos, out.writerIndex() - lengthPos - 4);
        }
        finally
        {
            marshaller.close();
        }
    }
}
