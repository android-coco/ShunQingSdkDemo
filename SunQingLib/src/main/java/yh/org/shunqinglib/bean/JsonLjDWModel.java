package yh.org.shunqinglib.bean;

import com.google.gson.annotations.SerializedName;

import org.yh.library.bean.YHModel;

/**
 * Created by yhlyl on 2017/5/13.
 * 立即定位模型
 */

public class JsonLjDWModel extends YHModel
{
    @SerializedName("result")
    protected String resultCode;//结果Code
    @SerializedName("datetime")
    protected String time;//时间

    public String getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(String resultCode)
    {
        this.resultCode = resultCode;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    @Override
    public String toString()
    {
        return "JsonLjDWModel{" +
                "resultCode='" + resultCode + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
