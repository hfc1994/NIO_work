package BIO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by user-hfc on 2017/10/29.
 */
public class TimeClient
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

        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try
        {
            socket = new Socket("127.0.0.1",port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("QUERY TIME ORDER");
            System.out.println("Send order to server succeed.");
            String res = in.readLine();
            System.out.println("Now is : " + res);
        }
        catch (Exception e)
        {}
        finally
        {
            if (null != out)
            {
                out.close();
                out = null;
            }

            if (null != in)
            {
                try
                {
                    in.close();
                }
                catch (Exception e2)
                {
                    e2.printStackTrace();
                }
                in = null;
            }

            if (null != socket)
            {
                try
                {
                    socket.close();
                }
                catch (Exception e3)
                {
                    e3.printStackTrace();
                }
                socket = null;
            }
        }
    }
}
