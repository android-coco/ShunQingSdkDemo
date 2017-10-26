package yh.org.shunqinglib.bean;

import com.google.gson.annotations.SerializedName;

import org.yh.library.bean.YHModel;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：游浩 on 2017/10/26 19:09
 * https://github.com/android-coco/YhLibraryForAndroid
 * 邮箱：yh_android@163.com
 * 允许呼入model
 */
public class JsonYxFrModel extends YHModel
{
    @SerializedName("result")
    protected String resultCode;//结果Code
    @SerializedName("datas")
    protected List<YxFrModel> datas;//数据

    public static class YxFrModel implements Serializable
    {
        @SerializedName("id")
        private String id;
        @SerializedName("number")
        private String number;

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getNumber()
        {
            return number;
        }

        public void setNumber(String number)
        {
            this.number = number;
        }

        @Override
        public String toString()
        {
            return "YxBhModel{" +
                    "id='" + id + '\'' +
                    ", numher='" + number + '\'' +
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

    public List<YxFrModel> getDatas()
    {
        return datas;
    }

    public void setDatas(List<YxFrModel> datas)
    {
        this.datas = datas;
    }

    @Override
    public String toString()
    {
        return "JsonYxBhModel{" +
                "resultCode='" + resultCode + '\'' +
                ", datas=" + datas +
                "} " + super.toString();
    }
}
