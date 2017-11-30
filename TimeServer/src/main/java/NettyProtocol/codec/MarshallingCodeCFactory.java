package NettyProtocol.codec;

import org.jboss.marshalling.*;

import java.io.IOException;

/**
 * Created by user-hfc on 2017/11/10.
 */
public final class MarshallingCodeCFactory
{
    protected static Marshaller buildMarshalling() throws IOException
    {
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        Marshaller marshaller = marshallerFactory.createMarshaller(configuration);
        return marshaller;
    }

    protected static Unmarshaller builderUnMarshalling() throws IOException
    {
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        final Unmarshaller unmarshaller = marshallerFactory.createUnmarshaller(configuration);
        return unmarshaller;
    }
}
