package MessagePack;

import org.msgpack.annotation.Message;

/**
 * Created by user-hfc on 2017/11/6.
 */
@Message
public class UserInfo
{
    private int age;

    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "age = " + age + "; name = " + name;
    }
}
