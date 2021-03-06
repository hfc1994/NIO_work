package NettyProtocol.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

/**
 * Created by user-hfc on 2017/11/19.
 */
public class MarshallingDecoder
{
    private final Unmarshaller unmarshaller;

    public MarshallingDecoder() throws IOException
    {
        unmarshaller = MarshallingCodeCFactory.builderUnMarshalling();
    }

    protected Object decode(ByteBuf in) throws Exception
    {
        System.out.println("server marshalling deocde");

        int objectSize = in.readInt();
        ByteBuf buf = in.slice(in.readerIndex(), objectSize);
        ByteInput input = new ChannelBufferByteInput(buf);
        try
        {
            unmarshaller.start(input);
            Object obj = unmarshaller.readObject();
            unmarshaller.finish();
            in.readerIndex(in.readerIndex() + objectSize);
            return obj;
        }
        finally
        {
            unmarshaller.close();
        }
    }
}
