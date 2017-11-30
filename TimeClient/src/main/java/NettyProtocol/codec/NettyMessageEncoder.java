package NettyProtocol.codec;

import NettyProtocol.struct.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by user-hfc on 2017/11/19.
 */
public final class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage>
{
    MarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder() throws IOException
    {
        this.marshallingEncoder = new MarshallingEncoder();
    }

    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf sendBuf) throws Exception
    {
        System.out.println("client netty message encode");
        if (null == msg || null == msg.getHeader())
            throw new Exception("The encode message is null");
        sendBuf.writeInt(msg.getHeader().getCrcCode());
        sendBuf.writeInt(msg.getHeader().getLength());
        sendBuf.writeLong(msg.getHeader().getSessionID());
        sendBuf.writeByte(msg.getHeader().getType());
        sendBuf.writeByte(msg.getHeader().getPriority());
        sendBuf.writeInt(msg.getHeader().getAttachment().size());

        String key = null;
        byte[] keyArray = null;
        Object value = null;
        for (Map.Entry<String,Object> param : msg.getHeader().getAttachment().entrySet())
        {
            key = param.getKey();
            keyArray = key.getBytes("utf-8");
            sendBuf.writeInt(keyArray.length);
            sendBuf.writeBytes(keyArray);
            value = param.getValue();
            marshallingEncoder.encode(value, sendBuf);
        }

        key = null;
        keyArray = null;
        value = null;
        if (null != msg.getBody())
        {
            marshallingEncoder.encode(msg.getBody(), sendBuf);
        }
//        else
//        {
//            //在本来是body数据的位置写入一个数据
//            sendBuf.writeInt(0);
//            //更新length位置的数值，减8是为什么？怀疑一个4是crcCode,相当于包头
//            //另外一个4是Length字段长度，LengthFieldBasedFrameDecoder需要过滤Length字段长度
//            //sendBuf.setInt(4, sendBuf.readableBytes() - 8);
//        }
        sendBuf.setInt(4, sendBuf.readableBytes() - 8);

    }

    public static void main(String[] args)
    {

    }
}
