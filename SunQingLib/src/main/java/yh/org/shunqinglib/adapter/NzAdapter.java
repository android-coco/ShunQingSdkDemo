package yh.org.shunqinglib.adapter;

import org.yh.library.adapter.rv.YhRecyclerAdapter;

import yh.org.shunqinglib.bean.JsonNzModel;

/**
 * Created by yhlyl on 2017/10/25.
 * 闹钟
 */

public class NzAdapter extends YhRecyclerAdapter<JsonNzModel.NzModel>
{
    public NzAdapter(){
        addItemViewDelegate(new NzItemDelagate());
    }
}
