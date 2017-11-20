package yh.org.shunqinglib.aty;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
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
import org.yh.library.view.YHSheetDialog;
import org.yh.library.view.loading.dialog.YHLoadingDialog;
import org.yh.library.view.yhrecyclerview.ProgressStyle;

import java.util.ArrayList;

import yh.org.shunqinglib.R;
import yh.org.shunqinglib.adapter.MdrSdAdapter;
import yh.org.shunqinglib.base.BaseActiciy;
import yh.org.shunqinglib.bean.JsonMdrSdModel;
import yh.org.shunqinglib.utils.GlobalUtils;

/**
 * 免打扰时段查询
 */
public class MdrSdActivity extends BaseActiciy implements I_YHItemClickListener<JsonMdrSdModel.MdrSdModel>
{
    private YHRecyclerView mRecyclerView;
    private LinearLayout empty_layout;
    private TextView id_empty_text;
    private MdrSdAdapter mAdapter;
    ArrayList<JsonMdrSdModel.MdrSdModel> data = null;

    @Override
    public void setRootView()
    {
        setContentView(R.layout.activity_mdrsd);
        initView();
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
        toolbar.setMainTitle("免打扰时段");
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
        mAdapter = new MdrSdAdapter();
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
        showActivity(aty, MdrSdAddActivity.class);
    }

    private void getData()
    {
        YHRequestFactory.getRequestManger().postString(GlobalUtils.HOME_HOST, GlobalUtils
                .DISTURB_LIST, null, "{\"sn\":\"" + GlobalUtils.DEIVER_SN + "\"}", new
                HttpCallBack()
                {
                    @Override
                    public void onSuccess(String t)
                    {
                        super.onSuccess(t);
                        final JsonMdrSdModel jsonData = JsonUitl.stringToTObject
                                (t, JsonMdrSdModel.class);
                        String resultCode = jsonData.getResultCode();
                        if ("0".equals(resultCode))
                        {
                            if (StringUtils.isEmpty(jsonData.getDatas()))
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
                        mRecyclerView.setEmptyView(empty_layout);
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
    public boolean onItemLongClick(View view, JsonMdrSdModel.MdrSdModel mdrSdModel, int i)
    {
        return false;
    }

    @Override
    public void onItemClick(View view, final JsonMdrSdModel.MdrSdModel mdrSdModel, int i)
    {
        new YHSheetDialog(aty)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("编辑", YHSheetDialog.SheetItemColor.Blue,
                        new YHSheetDialog.OnSheetItemClickListener()
                        {
                            @Override
                            public void onClick(int which)
                            {
                                Intent i = new Intent(aty, MdrSdEditActivity.class);
                                i.putExtra(MdrSdEditActivity.DATA_ACTION, mdrSdModel);
                                showActivity(aty, i);
                            }
                        })
                .addSheetItem("删除", YHSheetDialog.SheetItemColor.Red,
                        new YHSheetDialog.OnSheetItemClickListener()
                        {
                            @Override
                            public void onClick(int which)
                            {
                                DelMdrSd(mdrSdModel);
                            }
                        }).show();
    }


    private void DelMdrSd(final JsonMdrSdModel.MdrSdModel mdrSdModel)
    {
        YHLoadingDialog.make(aty).setMessage("删除中。。。")//提示消息
                .setCancelable(false).show();
        YHRequestFactory.getRequestManger().postString(GlobalUtils.HOME_HOST, GlobalUtils
                .DISTURB_DEL, null, "{\"id\":\"" + mdrSdModel.getId() + "\"}", new
                HttpCallBack()
                {
                    @Override
                    public void onSuccess(String t)
                    {
                        super.onSuccess(t);
                        final JsonMdrSdModel jsonData = JsonUitl.stringToTObject
                                (t, JsonMdrSdModel.class);
                        String resultCode = jsonData.getResultCode();
                        if ("0".equals(resultCode))
                        {
                            YHViewInject.create().showTips("删除成功");
                            data.remove(mdrSdModel);
                            mAdapter.notifyDataSetChanged();
                        }  else if ("5".equals(resultCode))
                        {
                            YHViewInject.create().showTips("删除成功,但是设备不在线,设备启动后同步");
                            YHLoadingDialog.cancel();
                            EventBus.getDefault().post(new EventBusBean());
                            finish();
                        }else
                        {
                            YHViewInject.create().showTips("删除失败");
                        }
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg)
                    {
                        super.onFailure(errorNo, strMsg);
                        YHViewInject.create().showTips("删除失败" + strMsg);
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
