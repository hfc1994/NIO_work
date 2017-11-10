package BIO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * Created by user-hfc on 2017/10/29.
 */
public class TimeServerHandler implements Runnable
{
    private Socket socket;

    public TimeServerHandler(Socket socket)
    {
        this.socket = socket;
    }

    public void run()
    {
        System.out.println("TimeServerHandler running");

        BufferedReader in = null;
        PrintWriter out = null;

        try
        {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(), true);
            String currentTime = null;
            String body = null;
            while (true)
            {
                body = in.readLine();
                if (null == body)
                    break;
                System.out.println("The time server receive order: " + body);
                currentTime = "QUERY TIME ORDER".equals(body)
                        ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                out.println(currentTime);
            }
        }
        catch (Exception e)
        {
            if (null != in)
            {
                try
                {
                    in.close();
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            }
            if (null != out)
            {
                out.close();
                out = null;
            }
            if (null != this.socket)
            {
                try
                {
                    this.socket.close();
                }
                catch (Exception e2)
                {
                    e2.printStackTrace();
                }
                this.socket = null;
            }
        }
    }
}
