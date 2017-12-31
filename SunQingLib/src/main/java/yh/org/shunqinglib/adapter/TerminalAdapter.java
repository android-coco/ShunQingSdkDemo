package yh.org.shunqinglib.adapter;


import org.yh.library.adapter.rv.YhRecyclerAdapter;

import yh.org.shunqinglib.bean.TerminalJosnBen;

/**
 * Created by yhlyl on 2017/12/15.
 * 我的设备列表
 */

public class TerminalAdapter extends YhRecyclerAdapter<TerminalJosnBen.TerminalModel>
{
    public TerminalAdapter(){
        addItemViewDelegate(new TerminalItemDelagate());
    }
}
