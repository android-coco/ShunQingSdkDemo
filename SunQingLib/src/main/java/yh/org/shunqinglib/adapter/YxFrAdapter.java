package yh.org.shunqinglib.adapter;

import org.yh.library.adapter.rv.YhRecyclerAdapter;

import yh.org.shunqinglib.bean.JsonYxFrModel;

/**
 * Created by yhlyl on 2017/10/25.
 * 允许拨号
 */

public class YxFrAdapter extends YhRecyclerAdapter<JsonYxFrModel.YxFrModel>
{
    public YxFrAdapter(){
        addItemViewDelegate(new YxFrItemDelagate());
    }
}
