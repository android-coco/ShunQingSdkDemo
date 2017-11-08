package yh.org.shunqinglib.bean;

import com.google.gson.annotations.SerializedName;

import org.yh.library.bean.YHModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yhlyl on 2017/5/13.
 * 定位记录模型
 */

public class JsonDwJlModel extends YHModel
{
    @SerializedName("result")
    protected String resultCode;//结果Cde
    @SerializedName("page")
    protected int page;//当前数据页数，每页最多数据32条，最后一页可能不足32条
    @SerializedName("total_count")
    protected int totalCount;//数据总条数
    @SerializedName("datas")
    protected List<DwJlModel> datas;//数据

    public static class DwJlModel implements Serializable
    {
        @SerializedName("id")
        private String id;//数据ID
        @SerializedName("data_type")
        private String dataType;//数据类型 （0：状态正常 2：SOS告警 3：电量低 4：电量低关机 5：主动关机 6：开机）
        @SerializedName("datetime")
        private String datetime;//定位时间
        @SerializedName("latitude")
        private double lat;// 纬度
        @SerializedName("longitude")
        private double lon;// 经度
        @SerializedName("clatitude")
        private double clat; //纠偏后的纬度 （百度）
        @SerializedName("clongitude")
        private double clon;//纠偏后的经度 （百度）
        @SerializedName("locate_type")
        private String locateType;//定位类型 （0：基站 1：GPS）
        @SerializedName("address")
        private String address;//定位地址
        @SerializedName("battery")
        private String battery;//所在时间的电量  (百分比)
        @SerializedName("signal")
        private String signal;//所在时间地点的信号  (百分比)

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getDataType()
        {
            return dataType;
        }

        public void setDataType(String dataType)
        {
            this.dataType = dataType;
        }

        public String getDatetime()
        {
            return datetime;
        }

        public void setDatetime(String datetime)
        {
            this.datetime = datetime;
        }

        public double getLat()
        {
            return lat;
        }

        public void setLat(double lat)
        {
            this.lat = lat;
        }

        public double getLon()
        {
            return lon;
        }

        public void setLon(double lon)
        {
            this.lon = lon;
        }

        public double getClat()
        {
            return clat;
        }

        public void setClat(double clat)
        {
            this.clat = clat;
        }

        public double getClon()
        {
            return clon;
        }

        public void setClon(double clon)
        {
            this.clon = clon;
        }

        public String getLocateType()
        {
            return locateType;
        }

        public void setLocateType(String locateType)
        {
            this.locateType = locateType;
        }

        public String getAddress()
        {
            return address;
        }

        public void setAddress(String address)
        {
            this.address = address;
        }

        public String getBattery()
        {
            return battery;
        }

        public void setBattery(String battery)
        {
            this.battery = battery;
        }

        public String getSignal()
        {
            return signal;
        }

        public void setSignal(String signal)
        {
            this.signal = signal;
        }


        @Override
        public String toString()
        {
            return "DwJlModel{" +
                    "id='" + id + '\'' +
                    ", dataType='" + dataType + '\'' +
                    ", datetime='" + datetime + '\'' +
                    ", lat=" + lat +
                    ", lon=" + lon +
                    ", clat=" + clat +
                    ", clon=" + clon +
                    ", locateType='" + locateType + '\'' +
                    ", address='" + address + '\'' +
                    ", battery='" + battery + '\'' +
                    ", signal='" + signal + '\'' +
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

    public List<DwJlModel> getDatas()
    {
        return datas;
    }

    public void setDatas(List<DwJlModel> datas)
    {
        this.datas = datas;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public int getTotalCount()
    {
        return totalCount;
    }

    public void setTotalCount(int totalCount)
    {
        this.totalCount = totalCount;
    }

    @Override
    public String toString()
    {
        return "JsonDwJlModel{" +
                "resultCode='" + resultCode + '\'' +
                ", page='" + page + '\'' +
                ", totalCount='" + totalCount + '\'' +
                ", datas=" + datas +
                '}';
    }
}
