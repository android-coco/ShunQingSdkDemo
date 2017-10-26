package yh.org.shunqinglib.adapter;

import org.yh.library.adapter.rv.I_ItemViewDelegate;
import org.yh.library.adapter.rv.YHRecyclerViewHolder;

import yh.org.shunqinglib.R;
import yh.org.shunqinglib.bean.JsonYxFrModel;

/**
 * Created by yhlyl on 2017/10/25.
 */

public class YxFrItemDelagate implements I_ItemViewDelegate<JsonYxFrModel.YxFrModel>
{
    @Override
    public int getItemViewLayoutId()
    {
        return R.layout.yxfr_item;
    }

    @Override
    public boolean isForViewType(JsonYxFrModel.YxFrModel dwSdModel, int i)
    {
        return true;
    }

    @Override
    public void convert(YHRecyclerViewHolder holder, JsonYxFrModel.YxFrModel item, int i)
    {
        holder.setText(R.id.numbertxt, item.getNumber());
    }
}
