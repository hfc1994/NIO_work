package FakeAIO;

import BIO.TimeServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by user-hfc on 2017/10/29.
 * 伪异步
 */
public class TimeServer
{
    public static void main(String[] args)
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

        ServerSocket server = null;
        try
        {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port : " + port);
            Socket socket = null;
            TimeServerHandlerExecutePool singleExecutor =
                    new TimeServerHandlerExecutePool(5,100);
            while (true)
            {
                socket = server.accept();
                singleExecutor.execute(new TimeServerHandler(socket));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != server)
            {
                System.out.println("The time server close");
                try
                {
                    server.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                server = null;
            }
        }
    }
}
