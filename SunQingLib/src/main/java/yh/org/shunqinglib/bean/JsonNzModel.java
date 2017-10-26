package yh.org.shunqinglib.bean;

import com.google.gson.annotations.SerializedName;

import org.yh.library.bean.YHModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yhlyl on 2017/5/13.
 *  闹钟模型
 */

public class JsonNzModel extends YHModel
{
    @SerializedName("result")
    protected String resultCode;//结果Cde
    @SerializedName("datas")
    protected List<NzModel> datas;//数据

    public static class NzModel implements Serializable
    {
        @SerializedName("id")
        private String id;//数据ID
        @SerializedName("week")
        private String week;//一周生效数据
        @SerializedName("hour")
        private String hour;//闹钟的小时
        @SerializedName("minute")
        private String minute;// 闹钟的分钟


        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getWeek()
        {
            return week;
        }

        public void setWeek(String week)
        {
            this.week = week;
        }

        public String getHour()
        {
            return hour;
        }

        public void setHour(String hour)
        {
            this.hour = hour;
        }

        public String getMinute()
        {
            return minute;
        }

        public void setMinute(String minute)
        {
            this.minute = minute;
        }

        @Override
        public String toString()
        {
            return "NzModel{" +
                    "id='" + id + '\'' +
                    ", week='" + week + '\'' +
                    ", hour='" + hour + '\'' +
                    ", minute='" + minute + '\'' +
                    '}';
        }
    }

    public String getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(String resultCode)
    {
        this.resultCode = resultCode;
    }

    public List<NzModel> getDatas()
    {
        return datas;
    }

    public void setDatas(List<NzModel> datas)
    {
        this.datas = datas;
    }


    @Override
    public String toString()
    {
        return "JsonNzModel{" +
                "resultCode='" + resultCode + '\'' +
                ", datas=" + datas +
                '}';
    }
}
