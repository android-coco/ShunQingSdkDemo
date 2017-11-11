package yh.org.shunqinglib.aty;

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
import org.yh.library.view.YHRecyclerView;
import org.yh.library.view.YHSheetDialog;
import org.yh.library.view.loading.dialog.YHLoadingDialog;
import org.yh.library.view.yhrecyclerview.ProgressStyle;

import java.util.ArrayList;

import yh.org.shunqinglib.R;
import yh.org.shunqinglib.adapter.DwJlAdapter;
import yh.org.shunqinglib.base.BaseActiciy;
import yh.org.shunqinglib.bean.JsonDwJlModel;
import yh.org.shunqinglib.utils.GlobalUtils;
import yh.org.shunqinglib.view.MyPopup;


/**
 * 定位记录
 */
public class DwJlActivity extends BaseActiciy implements I_YHItemClickListener<JsonDwJlModel
        .DwJlModel>
{

    private YHRecyclerView mRecyclerView;
    private LinearLayout empty_layout;
    private TextView id_empty_text;
    private DwJlAdapter mAdapter;
    ArrayList<JsonDwJlModel.DwJlModel> data = null;
    int page = 1;
    int TOTAL_DATA = 32;//每页32条数据 少于就加载完毕

    @Override
    public void setRootView()
    {
        setContentView(R.layout.activity_dwjl);
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
        toolbar.setMainTitle("定位记录");
        toolbar.setRightTitleText("筛选");
        toolbar.setRightTitleDrawable(R.mipmap.img_screening);

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
        mAdapter = new DwJlAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
//        mRecyclerView.setLoadingMoreEnabled(false);
//        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingListener(new YHRecyclerView.LoadingListener()
        {
            @Override
            public void onRefresh()
            {
                page = 1;
                mAdapter.getDatas().clear();//必须在数据更新前清空，不能太早
                String jsonParm = "{\"sn\":\"" + GlobalUtils.DEIVER_SN + "\",\"page\":\"" + page + "\"}";
                getData(jsonParm);
            }

            @Override
            public void onLoadMore()
            {
                page++;
                if (TOTAL_DATA >= 32)
                {//有32条数据就加载更多
                    // loading more
                    String jsonParm = "{\"sn\":\"" + GlobalUtils.DEIVER_SN + "\",\"page\":\"" + page + "\"}";
                    getData(jsonParm);
                } else
                {
                    //the end
                    mRecyclerView.setNoMore(true);
                }
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
        new MyPopup(aty, toolbar, new MyPopup.PatientOnclick()
        {
            @Override
            public void useOnclick(String startTime, String endTime)
            {
                String jsonParm = "{\"sn\":\"" + GlobalUtils.DEIVER_SN + "\",\"page\":\"" + page + "\"," +
                        "\"start_time\":\"" + startTime + " 00:00:00" + "\",\"end_time\":\"" + endTime + " 23:59:59" + "\"}";
                mAdapter.getDatas().clear();//必须在数据更新前清空，不能太早
                getData(jsonParm);
            }
        });
    }

    private void getData(String jsonParm)
    {
        YHRequestFactory.getRequestManger().postString(GlobalUtils.HOME_HOST, GlobalUtils
                .TREMINAL_POSITION, null, jsonParm, new
                HttpCallBack()
                {
                    @Override
                    public void onSuccess(String t)
                    {
                        super.onSuccess(t);
                        final JsonDwJlModel jsonData = JsonUitl.stringToTObject
                                (t, JsonDwJlModel.class);
                        String resultCode = jsonData.getResultCode();
                        TOTAL_DATA = jsonData.getTotalCount();
                        if ("0".equals(resultCode))
                        {
                            if (TOTAL_DATA == 0)
                            {
                                id_empty_text.setText("暂无数据!");
                                mRecyclerView.setEmptyView(empty_layout);//没有数据的空布局
                            } else
                            {
                                data.addAll(jsonData.getDatas());
                                mAdapter.setDatas(data);
                            }
                        } else
                        {
                            id_empty_text.setText("Code:" + resultCode);
                            mRecyclerView.setEmptyView(empty_layout);
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
                        mRecyclerView.setEmptyView(empty_layout);//没有数据的空布局
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
    public boolean onItemLongClick(View view, JsonDwJlModel.DwJlModel dwJlModel, int i)
    {
        return false;
    }

    @Override
    public void onItemClick(View view, final JsonDwJlModel.DwJlModel dwJlModel, int i)
    {
        new YHSheetDialog(aty)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setTitle("警告：删除后无法恢复！")
                .addSheetItem("删除", YHSheetDialog.SheetItemColor.Red,
                        new YHSheetDialog.OnSheetItemClickListener()
                        {
                            @Override
                            public void onClick(int which)
                            {
                                DelDwSd(dwJlModel);
                            }
                        }).show();
    }


    private void DelDwSd(final JsonDwJlModel.DwJlModel dwJlModel)
    {
        YHLoadingDialog.make(aty).setMessage("删除中。。。")//提示消息
                .setCancelable(false).show();
        YHRequestFactory.getRequestManger().postString(GlobalUtils.HOME_HOST, GlobalUtils
                .TREMINAL_POSITION_DEL, null, "{\"id\":\"" + dwJlModel.getId() + "\"}", new
                HttpCallBack()
                {
                    @Override
                    public void onSuccess(String t)
                    {
                        super.onSuccess(t);
                        final JsonDwJlModel jsonData = JsonUitl.stringToTObject
                                (t, JsonDwJlModel.class);
                        String resultCode = jsonData.getResultCode();
                        if ("0".equals(resultCode))
                        {
                            YHViewInject.create().showTips("删除成功");
                            data.remove(dwJlModel);
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
        String jsonParm = "{\"sn\":\"" + GlobalUtils.DEIVER_SN + "\",\"page\":\"" + page + "\"}";
        getData(jsonParm);
    }
}
