package yh.org.shunqinglib.adapter;

import org.yh.library.adapter.rv.YhRecyclerAdapter;

import yh.org.shunqinglib.bean.JsonMdrSdModel;

/**
 * Created by yhlyl on 2017/10/25.
 * 免打扰时段
 */

public class MdrSdAdapter extends YhRecyclerAdapter<JsonMdrSdModel.MdrSdModel>
{
    public MdrSdAdapter(){
        addItemViewDelegate(new MdrSdItemDelagate());
    }
}
