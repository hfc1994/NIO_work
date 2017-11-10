package NIO;

import java.io.IOException;

/**
 * Created by user-hfc on 2017/10/30.
 */
public class TimeServer
{
    public static void main(String[] args) throws IOException
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

        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
    }
}
