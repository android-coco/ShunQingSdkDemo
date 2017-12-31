package yh.org.shunqinglib.adapter;

import org.yh.library.adapter.rv.I_ItemViewDelegate;
import org.yh.library.adapter.rv.YHRecyclerViewHolder;

import yh.org.shunqinglib.R;
import yh.org.shunqinglib.bean.JsonDwJlModel;
import yh.org.shunqinglib.utils.GlobalUtils;

/**
 * Created by yhlyl on 2017/10/25.
 */

public class DwJlItemDelagate implements I_ItemViewDelegate<JsonDwJlModel.DwJlModel>
{
    @Override
    public int getItemViewLayoutId()
    {
        return R.layout.dwjl_item;
    }

    @Override
    public boolean isForViewType(JsonDwJlModel.DwJlModel dwJlModel, int i)
    {
        return true;
    }

    @Override
    public void convert(YHRecyclerViewHolder holder, JsonDwJlModel.DwJlModel item, int i)
    {
        String type = item.getDataType();

        //（0：状态正常 2：SOS告警 3：电量低 4：电量低关机 5：主动关机 6：开机）
        if (type == "0")
        {
            type = "正常";
        } else if (type == "1")
        {
            type = "心跳上报";
        } else if (type == "2")
        {
            type = "SOS告警";
        } else if (type == "3")
        {
            type = "电量低";
        } else if (type == "4")
        {
            type = "电量低关机";
        } else if (type == "5")
        {
            type = "主动关机";
        } else if (type == "6")
        {
            type = "开机";
        } else
        {
            type = "未知";
        }
        String locateType = item.getLocateType();
        if (locateType == "0")
        {
            locateType = "基站";
        } else if (locateType == "1")
        {
            locateType = "GPS";
        } else
        {
            locateType = "未知";
        }
        holder.setText(R.id.timing_name, locateType);//定位类型
        holder.setText(R.id.timing_time, item.getAddress());//当时位置
        holder.setText(R.id.timing_interval, type);//当时状态
        holder.setText(R.id.timing_cycle, GlobalUtils.getDateToString(Long.parseLong(item.getDatetime()),"yyyy年MM月dd日HH时mm分ss秒"));//定位时间
        holder.setText(R.id.timing_edit, "电量：" + item.getBattery());//电量
        holder.setText(R.id.timing_del, "信号：" + item.getSignal());//信号
    }
}
