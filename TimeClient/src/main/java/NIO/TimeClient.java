package NIO;

/**
 * Created by user-hfc on 2017/10/30.
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

        new Thread(new TimeClientHandle("127.0.0.1", port), "TimeClient-001").start();
    }
}
