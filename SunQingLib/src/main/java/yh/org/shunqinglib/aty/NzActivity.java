package yh.org.shunqinglib.aty;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.yh.library.adapter.I_YHItemClickListener;
import org.yh.library.bean.EventBusBean;
import org.yh.library.okhttp.YHRequestFactory;
import org.yh.library.okhttp.callback.HttpCallBack;
import org.yh.library.ui.YHViewInject;
import org.yh.library.utils.JsonUitl;
import org.yh.library.utils.LogUtils;
import org.yh.library.utils.StringUtils;
import org.yh.library.view.YHRecyclerView;
import org.yh.library.view.loading.dialog.YHLoadingDialog;
import org.yh.library.view.yhrecyclerview.ProgressStyle;

import java.io.Serializable;
import java.util.ArrayList;

import yh.org.shunqinglib.R;
import yh.org.shunqinglib.adapter.NzAdapter;
import yh.org.shunqinglib.base.BaseActiciy;
import yh.org.shunqinglib.bean.JsonNzModel;
import yh.org.shunqinglib.utils.GlobalUtils;
import yh.org.shunqinglib.view.ActionSheetDialog;

/**
 * 闹钟
 */
public class NzActivity extends BaseActiciy implements I_YHItemClickListener<JsonNzModel.NzModel>
{

    //    @BindView(id = R.id.recyclerview)
    private YHRecyclerView mRecyclerView;
    //    @BindView(id = R.id.empty_layout)
    private LinearLayout empty_layout;
    //    @BindView(id = R.id.id_empty_text)
    private TextView id_empty_text;
    private NzAdapter mAdapter;
    ArrayList<JsonNzModel.NzModel> data = null;
    @Override
    public void setRootView()
    {
        setContentView(R.layout.activity_nz);
    }


    private void initView()
    {
        mRecyclerView = (YHRecyclerView) findViewById(R.id.recyclerview);
        empty_layout = (LinearLayout) findViewById(R.id.empty_layout);
        id_empty_text = (TextView) findViewById(R.id.id_empty_text);
    }

    @Override
    public void initWidget()
    {
        super.initWidget();
        initView();
        toolbar.setLeftTitleText("返回");
        toolbar.setMainTitle("闹钟");
        toolbar.setRightTitleText("");

        id_empty_text.setText("加载中。。。");
        //lineartlayout
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //分割线为LinearLayoutManager
        mRecyclerView.addItemDecoration(mRecyclerView.new YHItemDecoration());//分割线
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setEmptyView(empty_layout);//没有数据的空布局

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallPulseRise);//可以自定义下拉刷新的样式
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);//可以自定义上拉加载的样式
        mRecyclerView.setFootViewText(getString(R.string.listview_loading), "我是有底线的。");
        // mRecyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);//箭头
        mAdapter = new NzAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLoadingMoreEnabled(false);
//        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingListener(new YHRecyclerView.LoadingListener()
        {
            @Override
            public void onRefresh()
            {
//                page = 1;
                mAdapter.getDatas().clear();//必须在数据更新前清空，不能太早
                getData();
            }

            @Override
            public void onLoadMore()
            {
//                //page++;
//                if (page <= TOTAL_PAGE) {//小于总页数就加载更多
//                    // loading more
//                    getData();
//                } else {
//                    //the end
//                    mRecyclerView.setNoMore(true);
//                }
            }
        });
        mRecyclerView.refresh();
    }

    @Override
    public void initData()
    {
        super.initData();
        data = new ArrayList<>();
        //getData();
    }

    @Override
    protected void onBackClick()
    {
        super.onBackClick();
        finish();
    }

    @Override
    protected void onMenuClick()
    {
        super.onMenuClick();
        showActivity(aty, NzAddActivity.class);
    }

    private void getData()
    {
        YHRequestFactory.getRequestManger().postString(GlobalUtils.HOME_HOST, GlobalUtils
                .ALARM_LIST, null, "{\"sn\":\"" + GlobalUtils.DEIVER_SN + "\"}", new
                HttpCallBack()
                {
                    @Override
                    public void onSuccess(String t)
                    {
                        super.onSuccess(t);
                        final JsonNzModel jsonData = JsonUitl.stringToTObject
                                (t, JsonNzModel.class);
                        String resultCode = jsonData.getResultCode();
                        if ("0".equals(resultCode))
                        {
                            if (StringUtils.isEmpty(jsonData.getDatas()))
                            {
                                id_empty_text.setText("暂无数据!");
                            } else
                            {
                                data.addAll(jsonData.getDatas());
                                mAdapter.setDatas(data);
                            }
                        } else
                        {
                            mAdapter.notifyDataSetChanged();
                        }
                        //刷新完毕
                        mRecyclerView.refreshComplete();
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg)
                    {
                        super.onFailure(errorNo, strMsg);
                        LogUtils.e(TAG, strMsg);
                        id_empty_text.setText("加载失败");
                        mAdapter.getDatas().clear();//必须在数据更新前清空，不能太早
                        //刷新完毕
                        mRecyclerView.refreshComplete();
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFinish()
                    {
                        super.onFinish();
                    }
                }, TAG);
    }

    @Override
    public boolean onItemLongClick(View view, JsonNzModel.NzModel nzModel, int i)
    {
        return false;
    }

    @Override
    public void onItemClick(View view, final JsonNzModel.NzModel nzModel, int i)
    {
        new ActionSheetDialog(aty)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("编辑", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener()
                        {
                            @Override
                            public void onClick(int which)
                            {
                                Intent i = new Intent(aty, NzEditActivity.class);
                                i.putExtra(NzEditActivity.DATA_ACTION, (Serializable) nzModel);
                                showActivity(aty, i);
                            }
                        })
                .addSheetItem("删除", ActionSheetDialog.SheetItemColor.Red,
                        new ActionSheetDialog.OnSheetItemClickListener()
                        {
                            @Override
                            public void onClick(int which)
                            {
                                DelDwSd(nzModel);
                            }
                        }).show();
    }


    private void DelDwSd(final JsonNzModel.NzModel nzModel)
    {
        YHLoadingDialog.make(aty).setMessage("删除中。。。")//提示消息
                .setCancelable(false).show();
        YHRequestFactory.getRequestManger().postString(GlobalUtils.HOME_HOST, GlobalUtils
                .ALARM_DEL, null, "{\"id\":\"" + nzModel.getId() + "\"}", new
                HttpCallBack()
                {
                    @Override
                    public void onSuccess(String t)
                    {
                        super.onSuccess(t);
                        final JsonNzModel jsonData = JsonUitl.stringToTObject
                                (t, JsonNzModel.class);
                        String resultCode = jsonData.getResultCode();
                        if ("0".equals(resultCode))
                        {
                            YHViewInject.create().showTips("删除成功");
                            data.remove(nzModel);
                            mAdapter.notifyDataSetChanged();
                        } else
                        {
                            YHViewInject.create().showTips("删除失败");
                        }
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg)
                    {
                        super.onFailure(errorNo, strMsg);
                        YHViewInject.create().showTips("删除失败");
                    }

                    @Override
                    public void onFinish()
                    {
                        super.onFinish();
                        YHLoadingDialog.cancel();
                    }
                }, TAG);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ploginOut(EventBusBean msg)
    {
        mAdapter.getDatas().clear();//必须在数据更新前清空，不能太早
        getData();
    }
}
