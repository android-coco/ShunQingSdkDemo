package yh.org.shunqinglib.bean;

import com.google.gson.annotations.SerializedName;

import org.yh.library.bean.YHModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yhlyl on 2017/5/13.
 * 终端位置模型
 */

public class JsonEquipmentModel extends YHModel
{
    @SerializedName("result")
    protected String resultCode;//结果Code
    @SerializedName("datas")
    protected List<EquipmentModel> datas;//数据

    public static class EquipmentModel implements Serializable
    {
        @SerializedName("product_sn")
        private String sn;//终端SN
        @SerializedName("device_type")
        private String deviceType;//终端的型号类型
        @SerializedName("datetime")
        private String datetime;//生产入库时间
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
        @SerializedName("locate_time")
        private String locateTime;//定位时间
        @SerializedName("address")
        private String address;//定位地址
        @SerializedName("battery")
        private String battery;//所在时间的电量  (百分比)
        @SerializedName("signal")
        private String signal;//所在时间地点的信号  (百分比)
        @SerializedName("flag_power")
        private String flagPower;//开关机短信通知  （0 关 1开）
        @SerializedName("flag_battery")
        private String flagBattery;//低电短信通知    （0 关 1开）
        @SerializedName("keysos")
        private String keysos;//SOS求助号码
        @SerializedName("keynum")
        private String keynum;//以逗号分隔，第一个为主亲情号

        public String getSn()
        {
            return sn;
        }

        public void setSn(String sn)
        {
            this.sn = sn;
        }

        public String getDeviceType()
        {
            return deviceType;
        }

        public void setDeviceType(String deviceType)
        {
            this.deviceType = deviceType;
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

        public String getLocateTime()
        {
            return locateTime;
        }

        public void setLocateTime(String locateTime)
        {
            this.locateTime = locateTime;
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

        public String getFlagPower()
        {
            return flagPower;
        }

        public void setFlagPower(String flagPower)
        {
            this.flagPower = flagPower;
        }

        public String getFlagBattery()
        {
            return flagBattery;
        }

        public void setFlagBattery(String flagBattery)
        {
            this.flagBattery = flagBattery;
        }

        public String getKeysos()
        {
            return keysos;
        }

        public void setKeysos(String keysos)
        {
            this.keysos = keysos;
        }

        public String getKeynum()
        {
            return keynum;
        }

        public void setKeynum(String keynum)
        {
            this.keynum = keynum;
        }

        @Override
        public String toString()
        {
            return "EquipmentModel{" +
                    "sn='" + sn + '\'' +
                    ", deviceType='" + deviceType + '\'' +
                    ", datetime='" + datetime + '\'' +
                    ", lat=" + lat +
                    ", lon=" + lon +
                    ", clat=" + clat +
                    ", clon=" + clon +
                    ", locateType='" + locateType + '\'' +
                    ", locateTime='" + locateTime + '\'' +
                    ", address='" + address + '\'' +
                    ", battery='" + battery + '\'' +
                    ", signal='" + signal + '\'' +
                    ", flagPower='" + flagPower + '\'' +
                    ", flagBattery='" + flagBattery + '\'' +
                    ", keysos='" + keysos + '\'' +
                    ", keynum='" + keynum + '\'' +
                    "} " + super.toString();
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

    public List<EquipmentModel> getDatas()
    {
        return datas;
    }

    public void setDatas(List<EquipmentModel> datas)
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
