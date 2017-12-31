package yh.org.shunqinglib.adapter;

import android.view.View;

import org.yh.library.adapter.rv.I_ItemViewDelegate;
import org.yh.library.adapter.rv.YHRecyclerViewHolder;

import yh.org.shunqinglib.R;
import yh.org.shunqinglib.bean.TerminalJosnBen;


/**
 * Created by yhlyl on 2017/12/15.
 */

public class TerminalItemDelagate implements I_ItemViewDelegate<TerminalJosnBen.TerminalModel>
{
    @Override
    public int getItemViewLayoutId()
    {
        return R.layout.terminal_item;
    }

    @Override
    public boolean isForViewType(TerminalJosnBen.TerminalModel item, int position)
    {
        return true;
    }

    @Override
    public void convert(YHRecyclerViewHolder holder, TerminalJosnBen.TerminalModel item, int position)
    {
        int type = item.getType();//0 自己的， 1为别人授权的
        String name = item.getName();//名称
        String sn = item.getProductSn();//SN号
        switch (type){
            case 0:
                holder.getView(R.id.img_watch).setVisibility(View.VISIBLE);
                holder.getView(R.id.img_watch1).setVisibility(View.INVISIBLE);
                break;
            case 1:
                holder.getView(R.id.img_watch1).setVisibility(View.VISIBLE);
                holder.getView(R.id.img_watch).setVisibility(View.INVISIBLE);
                break;
        }
        holder.setText(R.id.timing_name,name);
        holder.setText(R.id.sn,sn);
    }
}
