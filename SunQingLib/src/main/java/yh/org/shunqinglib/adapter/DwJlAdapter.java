package yh.org.shunqinglib.adapter;

import org.yh.library.adapter.rv.YhRecyclerAdapter;

import yh.org.shunqinglib.bean.JsonDwJlModel;

/**
 * Created by yhlyl on 2017/10/25.
 * 定位记录
 */

public class DwJlAdapter extends YhRecyclerAdapter<JsonDwJlModel.DwJlModel>
{
    public DwJlAdapter(){
        addItemViewDelegate(new DwJlItemDelagate());
    }
}
