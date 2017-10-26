package yh.org.shunqinglib.adapter;

import org.yh.library.adapter.rv.YhRecyclerAdapter;

import yh.org.shunqinglib.bean.JsonDwSdModel;

/**
 * Created by yhlyl on 2017/10/25.
 * 定位时段
 */

public class DwSdAdapter extends YhRecyclerAdapter<JsonDwSdModel.DwSdModel>
{
    public DwSdAdapter(){
        addItemViewDelegate(new DwSdItemDelagate());
    }
}
