package yh.org.shunqinglib.bean;

import com.google.gson.annotations.SerializedName;

import org.yh.library.bean.YHModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yhlyl on 2017/5/13.
 * 定位时段模型
 */

public class JsonDwSdModel extends YHModel
{
    @SerializedName("result")
    protected String resultCode;//结果Code
    @SerializedName("datas")
    protected List<DwSdModel> datas;//数据

    public static class DwSdModel implements Serializable{
        @SerializedName("id")
        private String id;//数据ID
        @SerializedName("week")
        private String week;//星期数据，周一为1， 周日为7，最多7天共存(1-7)
        @SerializedName("start_hour")
        private String startHour;//开始时间的小时
        @SerializedName("start_minute")
        private String startMinute;// 开始时间的分钟
        @SerializedName("end_hour")
        private String endHour;// 结束时间的小时
        @SerializedName("end_minute")
        private String endMinute; //结束时间的分钟
        @SerializedName("times")
        private String time;//定位上报的周期，秒，默认300（5分钟）

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

        public String getStartHour()
        {
            return startHour;
        }

        public void setStartHour(String startHour)
        {
            this.startHour = startHour;
        }

        public String getStartMinute()
        {
            return startMinute;
        }

        public void setStartMinute(String startMinute)
        {
            this.startMinute = startMinute;
        }

        public String getEndHour()
        {
            return endHour;
        }

        public void setEndHour(String endHour)
        {
            this.endHour = endHour;
        }

        public String getEndMinute()
        {
            return endMinute;
        }

        public void setEndMinute(String endMinute)
        {
            this.endMinute = endMinute;
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
            return "DwSdModel{" +
                    "Id='" + id + '\'' +
                    ", week='" + week + '\'' +
                    ", startHour='" + startHour + '\'' +
                    ", startMinute='" + startMinute + '\'' +
                    ", endHour='" + endHour + '\'' +
                    ", endMinute='" + endMinute + '\'' +
                    ", time='" + time + '\'' +
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

    public List<DwSdModel> getDatas()
    {
        return datas;
    }

    public void setDatas(List<DwSdModel> datas)
    {
        this.datas = datas;
    }


    @Override
    public String toString()
    {
        return "JsonEquipmentModel{" +
                "resultCode='" + resultCode + '\'' +
                ", datas=" + datas +
                "} " + super.toString();
    }
}
