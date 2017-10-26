package yh.org.shunqinglib.adapter;

import org.yh.library.adapter.rv.I_ItemViewDelegate;
import org.yh.library.adapter.rv.YHRecyclerViewHolder;

import java.util.Arrays;

import yh.org.shunqinglib.R;
import yh.org.shunqinglib.bean.JsonNzModel;
import yh.org.shunqinglib.utils.GlobalUtils;

/**
 * Created by yhlyl on 2017/10/25.
 */
public class NzItemDelagate implements I_ItemViewDelegate<JsonNzModel.NzModel>
{
    @Override
    public int getItemViewLayoutId()
    {
        return R.layout.nz_item;
    }

    @Override
    public boolean isForViewType(JsonNzModel.NzModel nzModel, int i)
    {
        return true;
    }

    @Override
    public void convert(YHRecyclerViewHolder holder, JsonNzModel.NzModel item, int i)
    {
        String bh =item.getHour();
        String bm =item.getMinute();
        if (Integer.parseInt(bh) <= 9 )
        {
            bh = "0" + bh;
        }
        if (Integer.parseInt(bm) <= 9 )
        {
            bm = "0" + bm;
        }
        holder.setText(R.id.timing_time, bh + ":" + bm);
        byte[] weeks = item.getWeek().getBytes();
        Arrays.sort(weeks);
        String str = "";
        for (int j = 0; j < weeks.length ; j++)
        {
            //[49, 50, 51, 52, 53, 54, 55]
            switch (weeks[j]){
                case 49:
                    str += "周一";
                    break;
                case 50:
                    str += ",周二";
                    break;
                case 51:
                    str += ",周三";
                    break;
                case 52:
                    str += ",周四";
                    break;
                case 53:
                    str += ",周五";
                    break;
                case 54:
                    str += ",周六";
                    break;
                case 55:
                    str += ",周日";
                    break;
            }
        }
        holder.setText(R.id.timing_cycle, GlobalUtils.replaceStr(str));
    }
}
