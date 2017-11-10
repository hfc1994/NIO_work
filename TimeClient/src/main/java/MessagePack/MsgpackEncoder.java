package MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * Created by user-hfc on 2017/11/6.
 */
public class MsgpackEncoder extends MessageToByteEncoder<Object>
{
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception
    {
        MessagePack msgpack = new MessagePack();
        byte[] raw = msgpack.write(o);
        byteBuf.writeBytes(raw);
    }
}
