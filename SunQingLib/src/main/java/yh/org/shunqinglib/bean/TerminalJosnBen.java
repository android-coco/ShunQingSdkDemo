package yh.org.shunqinglib.bean;

import com.google.gson.annotations.SerializedName;

import org.yh.library.bean.YHModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yhlyl on 2017/12/15.
 * 我的终端列表解析JSON实体类
 */

public class TerminalJosnBen extends YHModel
{
    @SerializedName("result")
    protected String resultCode;//结果Code
    @SerializedName("datas")
    protected List<TerminalModel> datas;//数据

    public static class TerminalModel implements Serializable
    {
        private String name;//终端名称
        private int id;//数据ID
        @SerializedName("product_sn")
        private String productSn;//SN号
        private int signal;//所在时间地点的信号  (百分比)
        private int battery;//所在时间的电量  (百分比)
        @SerializedName("locate_time")
        private int locateTime;
        private int face;
        private double latitude;
        private double longitude;
        @SerializedName("locate_type")
        private int locateType;
        private double clatitude;
        private double clongitude;
        private String address;
        @SerializedName("flag_power")
        private int flagPower;
        @SerializedName("flag_battery")
        private int flagBattery;
        private int type;//0 自己的， 1为别人授权的

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public String getProductSn()
        {
            return productSn;
        }

        public void setProductSn(String productSn)
        {
            this.productSn = productSn;
        }

        public int getSignal()
        {
            return signal;
        }

        public void setSignal(int signal)
        {
            this.signal = signal;
        }

        public int getBattery()
        {
            return battery;
        }

        public void setBattery(int battery)
        {
            this.battery = battery;
        }

        public int getLocateTime()
        {
            return locateTime;
        }

        public void setLocateTime(int locateTime)
        {
            this.locateTime = locateTime;
        }

        public int getFace()
        {
            return face;
        }

        public void setFace(int face)
        {
            this.face = face;
        }

        public double getLatitude()
        {
            return latitude;
        }

        public void setLatitude(double latitude)
        {
            this.latitude = latitude;
        }

        public double getLongitude()
        {
            return longitude;
        }

        public void setLongitude(double longitude)
        {
            this.longitude = longitude;
        }

        public int getLocateType()
        {
            return locateType;
        }

        public void setLocateType(int locateType)
        {
            this.locateType = locateType;
        }

        public double getClatitude()
        {
            return clatitude;
        }

        public void setClatitude(double clatitude)
        {
            this.clatitude = clatitude;
        }

        public double getClongitude()
        {
            return clongitude;
        }

        public void setClongitude(double clongitude)
        {
            this.clongitude = clongitude;
        }

        public String getAddress()
        {
            return address;
        }

        public void setAddress(String address)
        {
            this.address = address;
        }

        public int getFlagPower()
        {
            return flagPower;
        }

        public void setFlagPower(int flagPower)
        {
            this.flagPower = flagPower;
        }

        public int getFlagBattery()
        {
            return flagBattery;
        }

        public void setFlagBattery(int flagBattery)
        {
            this.flagBattery = flagBattery;
        }

        public int getType()
        {
            return type;
        }

        public void setType(int type)
        {
            this.type = type;
        }

        @Override
        public String toString()
        {
            return "TerminalModel{" +
                    "name='" + name + '\'' +
                    ", id=" + id +
                    ", productSn='" + productSn + '\'' +
                    ", signal=" + signal +
                    ", battery=" + battery +
                    ", locateTime=" + locateTime +
                    ", face=" + face +
                    ", latitude=" + latitude +
                    ", longitude=" + longitude +
                    ", locateType=" + locateType +
                    ", clatitude=" + clatitude +
                    ", clongitude=" + clongitude +
                    ", address='" + address + '\'' +
                    ", flagPower=" + flagPower +
                    ", flagBattery=" + flagBattery +
                    ", type=" + type +
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

    public List<TerminalModel> getDatas()
    {
        return datas;
    }

    public void setDatas(List<TerminalModel> datas)
    {
        this.datas = datas;
    }

    @Override
    public String toString()
    {
        return "TerminalJosnBen{" +
                "resultCode='" + resultCode + '\'' +
                ", datas=" + datas +
                '}';
    }
}
