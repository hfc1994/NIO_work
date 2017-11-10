package Marshalling;

import java.io.Serializable;

/**
 * Created by user-hfc on 2017/11/10.
 */
public class SubscribeResp implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int subReqID;

    private int respCode;

    private String desc;

    public final int getSubReqID() {
        return subReqID;
    }

    public final void setSubReqID(int subReqID) {
        this.subReqID = subReqID;
    }

    public final int getRespCode() {
        return respCode;
    }

    public final void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public final String getDesc() {
        return desc;
    }

    public final void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString()
    {
        return "SubscribeResp [subReqID=" + subReqID + ", respCode=" + respCode
                + ", desc=" + desc + "]";
    }
}
