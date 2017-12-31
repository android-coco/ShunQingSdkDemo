package yh.org.shunqinglib.aty;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.yh.library.adapter.I_YHItemClickListener;
import org.yh.library.bean.EventBusBean;
import org.yh.library.okhttp.YHRequestFactory;
import org.yh.library.okhttp.callback.HttpCallBack;
import org.yh.library.ui.I_PermissionListener;
import org.yh.library.ui.YHViewInject;
import org.yh.library.utils.Constants;
import org.yh.library.utils.DensityUtils;
import org.yh.library.utils.JsonUitl;
import org.yh.library.utils.LogUtils;
import org.yh.library.utils.StringUtils;
import org.yh.library.utils.SystemUtils;
import org.yh.library.view.YHRecyclerView;
import org.yh.library.view.yhrecyclerview.ProgressStyle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yh.org.shunqinglib.R;
import yh.org.shunqinglib.adapter.TerminalAdapter;
import yh.org.shunqinglib.base.BaseActiciy;
import yh.org.shunqinglib.bean.JsonEquipmentModel;
import yh.org.shunqinglib.bean.JsonLjDWModel;
import yh.org.shunqinglib.bean.TerminalJosnBen;
import yh.org.shunqinglib.utils.GlobalUtils;
import yh.org.shunqinglib.view.YhToolbar;

/**
 * 高德地图
 */
public class LocationByGD2Activity extends BaseActiciy implements AMapLocationListener,
        I_YHItemClickListener<TerminalJosnBen.TerminalModel>, AMap.OnMapClickListener, AMap
                .OnMarkerClickListener
{
    private static final String TAG = LocationByGD2Activity.class.getSimpleName();
    //    @BindView(id = R.id.bmapView)
    MapView mMapView = null;//高德地图
    //    @BindView(id = R.id.img_position_of, click = true)
    ImageView img_position_of;// 回到手表的位子
    //    @BindView(id = R.id.img_According, click = true)
    ImageView img_According;// 显示或不显示自己的位子

    //    @BindView(id = R.id.location_rall, click = true)
    LinearLayout location_rall;
    //    @BindView(id = R.id.location_log, click = true)
    LinearLayout location_log;
    //    @BindView(id = R.id.location_report, click = true)
    LinearLayout location_report;
    //    @BindView(id = R.id.location_cry, click = true)
    LinearLayout location_cry;
    //    @BindView(id = R.id.location_blood_plu, click = true)
    LinearLayout location_blood_plu;
    //    @BindView(id = R.id.location_sos, click = true)
    FrameLayout location_sos;
    //    @BindView(id = R.id.sleep_time, click = true)
    LinearLayout sleep_time;
    //    @BindView(id = R.id.icon_gcoding, click = true)
    LinearLayout gps_command;

    LinearLayout sz;//终端设置

    //    @BindView(id = tv_street)
    TextView textAddress;//地址
    TextView textStreet;//定位类型
    TextView textLocation;//定位时间


    //初始化地图控制器对象
    private AMap aMap;
    boolean isFirstLoc = true; // 是否首次定位
    // 高德地图定位
    // 声明mLocationOption对象
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private int pos_time = 5 * 60;// 60s
    // 地图起点终点
    private LatLng mMapStart;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    public int ranges = 12;// 缩放级别（zoom）
    private boolean isWork = true;
    JsonEquipmentModel jsonEquipmentModel;

    private PopupWindow mCurPopupWindow;
    //侧边栏
    DrawerLayout drawer = null;

    private YHRecyclerView mRecyclerView;
    private LinearLayout empty_layout;//空布局
    private TextView id_empty_text;//空布局描述
    private LinearLayout gd_linearlayout;//空高德地图布局1
    private FrameLayout gd_framelayout;//高德地图布局2
    private RelativeLayout gd_relativelayout;//高德地图布局3
    private ImageView img_battery;//电量
    private TerminalAdapter mAdapter;
    ArrayList<TerminalJosnBen.TerminalModel> data = null;
    private String c_sn = "";//当前SN号
    TerminalJosnBen.TerminalModel c_termianlModel = null;//当前终端

    @Override
    public void setRootView()
    {
        setContentView(R.layout.activity_sh365);
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    public void initData()
    {
        super.initData();
        data = new ArrayList<>();
        //权限判断
        /***
         * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
         */
        // 定位精确位置

        // 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
        // 读取电话状态权限
        requestRunTimePermission(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, new
                I_PermissionListener()
                {
                    @Override
                    public void onSuccess()//所有权限OK
                    {
                        //直接执行相应操作了
                        Constants.Config.IS_WRITE_EXTERNAL_STORAGE = true;

                        mRecyclerView.setLoadingListener(new YHRecyclerView.LoadingListener()
                        {
                            @Override
                            public void onRefresh()
                            {
//                                mAdapter.getDatas().clear();//必须在数据更新前清空，不能太早
//                                getData();
                            }

                            @Override
                            public void onLoadMore()
                            {
                            }
                        });
                        mAdapter.getDatas().clear();//必须在数据更新前清空，不能太早
                        getData();
                        mRecyclerView.refresh();
                    }

                    @Override
                    public void onGranted(List<String> grantedPermission)//部分权限OK
                    {
                    }

                    @Override
                    public void onFailure(List<String> deniedPermission)//全部拒绝
                    {
                        Constants.Config.IS_WRITE_EXTERNAL_STORAGE = false;
                        YHViewInject.create().showTips("拒绝授权列表：" + Constants.initPermissionNames
                                ().get(deniedPermission.get(0)));
                    }
                });

    }

    private void getData()
    {
        String params = "{\"uid\":\"" + GlobalUtils.USER_UID + "\"}";
        YHRequestFactory.getRequestManger().postString(GlobalUtils.HOME_HOST, GlobalUtils
                        .TERMINAL_LIST,
                null, params, new
                        HttpCallBack()
                        {
                            @Override
                            public void onSuccess(String t)
                            {
                                super.onSuccess(t);
                                final TerminalJosnBen jsonData = JsonUitl.stringToTObject
                                        (t, TerminalJosnBen.class);
                                LogUtils.e(TAG, jsonData);
                                String resultCode = jsonData.getResultCode();
                                if ("0".equals(resultCode))
                                {
                                    if (StringUtils.isEmpty(jsonData.getDatas()))
                                    {
                                        //暂无终端!
                                        empty_layout.setVisibility(View.VISIBLE);
                                        id_empty_text.setText("暂无终端");
                                        gd_relativelayout.setVisibility(View.GONE);
                                        gd_linearlayout.setVisibility(View.GONE);
                                        gd_framelayout.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        empty_layout.setVisibility(View.GONE);
                                        gd_relativelayout.setVisibility(View.VISIBLE);
                                        gd_linearlayout.setVisibility(View.VISIBLE);
                                        gd_framelayout.setVisibility(View.VISIBLE);
                                        data.addAll(jsonData.getDatas());
                                        mAdapter.setDatas(data);
                                        c_sn = data.get(0).getProductSn();
                                        c_termianlModel = data.get(0);
                                        GlobalUtils.DEIVER_SN = c_sn;
                                        getLastLoctionByOneMine();
                                    }
                                }
                                else
                                {
                                    //获取终端错误!
                                    //empty_layout.setVisibility(View.VISIBLE);
                                    id_empty_text.setText("获取终端错误Code:" + resultCode);
                                    gd_relativelayout.setVisibility(View.GONE);
                                    gd_linearlayout.setVisibility(View.GONE);
                                    gd_framelayout.setVisibility(View.GONE);
                                }
                                //刷新完毕
                                mRecyclerView.refreshComplete();
                            }

                            @Override
                            public void onFailure(int errorNo, String strMsg)
                            {
                                super.onFailure(errorNo, strMsg);
                                LogUtils.e(TAG, strMsg);
                                //获取终端错误!
                                //empty_layout.setVisibility(View.VISIBLE);
                                id_empty_text.setText("获取终端错误:" + strMsg);
                                gd_relativelayout.setVisibility(View.GONE);
                                gd_linearlayout.setVisibility(View.GONE);
                                gd_framelayout.setVisibility(View.GONE);
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

    //1分钟获取一次最后位置
    private void getLastLoctionByOneMine()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                super.run();

                while (isWork)
                {
                    getLastLoction();
                    try
                    {
                        sleep(60 * 1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
    }

    //获取最后位置
    private void getLastLoction()
    {
        //"{\"sns\":\"123456789012345\"}"
        YHRequestFactory.getRequestManger().postString(GlobalUtils.HOME_HOST, GlobalUtils
                .DEVER_INFO, null, "{\"sns\":\"" + c_sn + "\"}", new HttpCallBack()
        {
            @Override
            public void onSuccess(String t)
            {
                super.onSuccess(t);
                jsonEquipmentModel = JsonUitl.stringToTObject
                        (t, JsonEquipmentModel.class);
                if (!StringUtils.isEmpty(jsonEquipmentModel) && !StringUtils.isEmpty
                        (jsonEquipmentModel.getDatas()))
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mCurrentLat = jsonEquipmentModel.getDatas().get(0).getClat();
                            mCurrentLon = jsonEquipmentModel.getDatas().get(0).getClon();
                            textAddress.setText(jsonEquipmentModel.getDatas().get(0).getAddress());
                            String locateType = jsonEquipmentModel.getDatas().get(0)
                                    .getLocateType();
                            if (locateType == "0")
                            {
                                locateType = "基站";
                            }
                            else if (locateType == "1")
                            {
                                locateType = "GPS";
                            }
                            else
                            {
                                locateType = "未知";
                            }
                            String time = GlobalUtils.getDateToString(Long.parseLong
                                            (jsonEquipmentModel.getDatas().get(0).getLocateTime()),
                                    "yyyy-MM-dd HH:mm:ss");
                            textStreet.setText(time + " " + locateType);
                            String address = jsonEquipmentModel.getDatas().get(0).getAddress();
                            if (!StringUtils.isEmpty(address) && address.length() > 12)
                            {
                                address = GlobalUtils.addStr(12, "\n", address);
                            }
                            textAddress.setText(address);
                            int battery = Integer.parseInt(jsonEquipmentModel.getDatas().get(0)
                                    .getBattery());
                            if (battery < 20)
                            {
                                img_battery.setBackgroundResource(R.mipmap.b1);
                            }
                            else if (20 <= battery && battery < 40)
                            {
                                img_battery.setBackgroundResource(R.mipmap.b2);
                            }
                            else if (40 <= battery && battery < 60)
                            {
                                img_battery.setBackgroundResource(R.mipmap.b3);
                            }
                            else if (60 <= battery && battery < 80)
                            {
                                img_battery.setBackgroundResource(R.mipmap.b4);
                            }
                            else if (80 <= battery)
                            {
                                img_battery.setBackgroundResource(R.mipmap.b5);
                            }
                            if (255 == battery)
                            {
                                img_battery.setBackgroundResource(R.mipmap.b6);
                            }
                            addMaker();
                        }
                    });
                }
                else
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            textStreet.setText("暂无手表信息,请确认您的SN号是否正确！");
                            textAddress.setVisibility(View.GONE);
                        }
                    });
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg)
            {
                super.onFailure(errorNo, strMsg);
            }

            @Override
            public void onFinish()
            {
                super.onFinish();
            }
        }, TAG);
    }

    private void initView()
    {
        mMapView = (MapView) findViewById(R.id.bmapView);
        img_position_of = (ImageView) findViewById(R.id.img_position_of);
        img_position_of.setOnClickListener(this);

        img_According = (ImageView) findViewById(R.id.img_According);
        img_According.setOnClickListener(this);

        location_rall = (LinearLayout) findViewById(R.id.location_rall);
        location_rall.setOnClickListener(this);

        location_log = (LinearLayout) findViewById(R.id.location_log);
        location_log.setOnClickListener(this);


        location_report = (LinearLayout) findViewById(R.id.location_report);
        location_report.setOnClickListener(this);


        location_cry = (LinearLayout) findViewById(R.id.location_cry);
        location_cry.setOnClickListener(this);

        location_blood_plu = (LinearLayout) findViewById(R.id.location_blood_plu);
        location_blood_plu.setOnClickListener(this);


        location_sos = (FrameLayout) findViewById(R.id.location_sos);
        location_sos.setOnClickListener(this);

        sleep_time = (LinearLayout) findViewById(R.id.sleep_time);
        sleep_time.setOnClickListener(this);


        gps_command = (LinearLayout) findViewById(R.id.gps_command);
        gps_command.setOnClickListener(this);


        textAddress = (TextView) findViewById(R.id.tv_street);//定位位置
        textLocation = (TextView) findViewById(R.id.tv_location);//定位时间
        textStreet = (TextView) findViewById(R.id.tv_address);//定位类型


        sz = (LinearLayout) findViewById(R.id.location_track);
        sz.setOnClickListener(this);
        //侧边栏
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(toggle);
        //禁止手势滑动
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //打开手势滑动
        //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        toggle.syncState();

        //侧边栏数据
        mRecyclerView = (YHRecyclerView) findViewById(R.id.nav_view);
        empty_layout = (LinearLayout) findViewById(R.id.include);
        id_empty_text = (TextView) findViewById(R.id.id_empty_text);
        gd_relativelayout = (RelativeLayout) findViewById(R.id.gd_relativelayout);
        gd_framelayout = (FrameLayout) findViewById(R.id.gd_framelayout);
        gd_linearlayout = (LinearLayout) findViewById(R.id.buttom_view);
        img_battery = (ImageView) findViewById(R.id.battery);
        //lineartlayout
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //分割线为LinearLayoutManager
        mRecyclerView.addItemDecoration(mRecyclerView.new YHItemDecoration());//分割线
        mRecyclerView.setLayoutManager(layoutManager);
        //mRecyclerView.setEmptyView(empty_layout);//没有数据的空布局

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallPulseRise);//可以自定义下拉刷新的样式
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);//可以自定义上拉加载的样式
        mRecyclerView.setFootViewText(getString(R.string.listview_loading), "我是有底线的。");
        // mRecyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);//箭头
        mAdapter = new TerminalAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setPullRefreshEnabled(false);

    }

    @Override
    public void initWidget()
    {
        super.initWidget();

        //toolbar.setLeftTitleText("返回");
//        toolbar.setLeftTitleDrawable1(YhResource.getIdByName(this, "mipmap", "logo"), YhToolbar
//                .LEFT, 30);
        toolbar.setMainTitle("护卫365");
        toolbar.setRightTitleDrawable1(R.mipmap.config_set, YhToolbar.LEFT, 20);
        toolbar.setRightTitleDrawable2(R.mipmap.icon_plus, YhToolbar.LEFT, 30);
        if (aMap == null)
        {
            aMap = mMapView.getMap();
            mUiSettings = aMap.getUiSettings();
            setUpMap();
            startLocation();
        }
    }

    /**
     * 开始定位
     */
    private void startLocation()
    {
        // 定位初始化
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置为不是单次定位
        locationOption.setOnceLocation(false);
        // 设置定位模式为低功耗模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        /**
         * 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算 只有持续定位设置定位间隔才有效，单次定位无效
         */
        locationOption.setInterval(pos_time * 1000);
        // 设置定位监听
        locationClient.setLocationListener(this);
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    //重新定位
    private void repositionLocation()
    {
        /**
         * 销毁定位
         */
        if (!StringUtils.isEmpty(locationClient))
        {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
        // 定位初始化
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置为不是单次定位
        locationOption.setOnceLocation(false);
        // 设置定位模式为低功耗模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        /**
         * 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算 只有持续定位设置定位间隔才有效，单次定位无效
         */
        locationOption.setInterval(pos_time * 1000);
        // 设置定位监听
        locationClient.setLocationListener(this);
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }


    private UiSettings mUiSettings;

    /**
     * 设置一些amap的属性
     */
    private void setUpMap()
    {
//        aMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器
//        aMap.setOnMapTouchListener(this);// 对amap添加触摸地图事件监听器
        // aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        // aMap.setMyLocationEnabled(true);//
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        // aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setScaleControlsEnabled(true);// 设置地图默认的比例尺是否显示
    }


    private MarkerOptions markerStart;
    private BitmapDescriptor bdSt = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_gcoding);
    private Marker centerMarker;

    private void addMaker()
    {
        if ((mCurrentLat == mCurrentLon) || mCurrentLat == 0 || mCurrentLon == 0)
        {
            return;
        }
        if (!StringUtils.isEmpty(aMap))
        {
            aMap.clear();
        }
        //定义Maker坐标点
        mMapStart = new LatLng(mCurrentLat, mCurrentLon);
        markerStart = new MarkerOptions();
        markerStart.position(mMapStart);
        markerStart.icon(bdSt);
        // myMarker =
        centerMarker = aMap.addMarker(markerStart);
        centerMarker.setTitle(c_termianlModel.getName());
        centerMarker.setAutoOverturnInfoWindow(true);
        centerMarker.setSnippet("时间：" + GlobalUtils.getDateToString(c_termianlModel.getLocateTime
                (), "yyyy/MM/dd HH:mm:ss"));
        centerMarker.showInfoWindow();
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                centerMarker.getPosition(), ranges));
    }


    @Override
    public void widgetClick(View v)
    {
        super.widgetClick(v);
        int i = v.getId();
        if (i == R.id.location_blood_plu)//终端设置
        {


//            if (StringUtils.isEmpty(jsonEquipmentModel) || StringUtils.isEmpty
//                    (jsonEquipmentModel.getDatas()))
//            {
//                YHViewInject.create().showTips("数据加载中或者无法获取手表数据！");
//            }
//            else
//            {
//                showActivity(aty, ZxJtActivity.class);
//            }

            if (StringUtils.isEmpty(jsonEquipmentModel) || StringUtils.isEmpty
                    (jsonEquipmentModel.getDatas()))
            {
                YHViewInject.create().showTips("数据加载中或者无法获取手表数据！");
            }
            else
            {
                jsonEquipmentModel.getDatas().get(0).setType(c_termianlModel.getType() + "");
                Intent i1 = new Intent(aty, JbSz365Activity.class);
                i1.putExtra(JbSz365Activity.DATA_ACTION, jsonEquipmentModel.getDatas().get(0));
                showActivity(aty, i1);
            }
        }
        else if (i == R.id.img_position_of)//回到终端位置
        {
            addMaker();
        }
        else if (i == R.id.location_report)//闹钟
        {
            showActivity(aty, NzActivity.class);
        }
        else if (i == R.id.location_cry)//免扰时段
        {
            showActivity(aty, MdrSdActivity.class);
        }
        else if (i == R.id.location_log)//定位时段
        {
            showActivity(aty, DwSdActivity.class);
        }
        else if (i == R.id.gps_command)//定位记录
        {
            showActivity(aty, DwJlActivity.class);
        }
        else if (i == R.id.location_rall)//历史数据
        {
            showActivity(aty, DwJlActivity.class);
//            if (StringUtils.isEmpty(jsonEquipmentModel) || StringUtils.isEmpty
//                    (jsonEquipmentModel.getDatas()))
//            {
//                YHViewInject.create().showTips("数据加载中或者无法获取手表数据！");
//            }
//            else
//            {
//                ljDW();
//            }

        }
        else if (i == R.id.location_sos)//立即执行
        {
            showTipPopupWindow(v);
//            if (StringUtils.isEmpty(jsonEquipmentModel) || StringUtils.isEmpty
//                    (jsonEquipmentModel.getDatas()))
//            {
//                YHViewInject.create().showTips("数据加载中或者无法获取手表数据！");
//            }
//            else
//            {
//                showActivity(aty, ZdBhActivity.class);
//            }
        }
        else if (i == R.id.sleep_time)//允许呼入
        {
            showActivity(aty, YxFrActivity.class);
        }
        else if (i == R.id.location_track)
        {
        }
    }

    private View getPopupWindowContentView()
    {
        // 一个自定义的布局，作为显示的内容
        int layoutId = R.layout.popup_content_layout;   // 布局ID
        View contentView = LayoutInflater.from(this).inflate(layoutId, null);
        View.OnClickListener menuItemOnClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int i = v.getId();
                if (i == R.id.menu_item1)
                {
                    if (StringUtils.isEmpty(jsonEquipmentModel) || StringUtils.isEmpty
                            (jsonEquipmentModel.getDatas()))
                    {
                        YHViewInject.create().showTips("数据加载中或者无法获取手表数据！");
                    }
                    else
                    {
                        ljDW();
                    }
                }
                else if (i == R.id.menu_item2)
                {
                    if (StringUtils.isEmpty(jsonEquipmentModel) || StringUtils.isEmpty
                            (jsonEquipmentModel.getDatas()))
                    {
                        YHViewInject.create().showTips("数据加载中或者无法获取手表数据！");
                    }
                    else
                    {
                        showActivity(aty, ZdBhActivity.class);
                    }
                }
                else if (i == R.id.menu_item3)
                {
                    if (StringUtils.isEmpty(jsonEquipmentModel) || StringUtils.isEmpty
                            (jsonEquipmentModel.getDatas()))
                    {
                        YHViewInject.create().showTips("数据加载中或者无法获取手表数据！");
                    }
                    else
                    {
                        showActivity(aty, ZxJtActivity.class);
                    }
                }
                if (mCurPopupWindow != null)
                {
                    mCurPopupWindow.dismiss();
                }
            }
        };
        contentView.findViewById(R.id.menu_item1).setOnClickListener(menuItemOnClickListener);
        contentView.findViewById(R.id.menu_item2).setOnClickListener(menuItemOnClickListener);
        contentView.findViewById(R.id.menu_item3).setOnClickListener(menuItemOnClickListener);
        return contentView;
    }

    public void showTipPopupWindow(final View anchorView)
    {
        View contentView = getPopupWindowContentView();
        mCurPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        mCurPopupWindow.setBackgroundDrawable(new ColorDrawable());
        // 设置好参数之后再show
        // popupWindow.showAsDropDown(mButton2);  // 默认在mButton2的左下角显示
//        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        int xOffset = anchorView.getWidth() / 2 - contentView.getMeasuredWidth() / 2;
//        mCurPopupWindow.showAsDropDown(anchorView, Gravity.TOP, xOffset, 0);    // 在mButton2的中间显示

        // 设置好参数之后再show
        int windowPos[] = calculatePopWindowPos(anchorView, contentView);
        int xOff = 0; // 可以自己调整偏移
        windowPos[0] -= xOff;
        mCurPopupWindow.showAtLocation(anchorView, Gravity.TOP | Gravity.END, windowPos[0],
                windowPos[1]);
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView  呼出window的view
     * @param contentView window的内容布局
     * @return window显示的左上角的xOff, yOff坐标
     */
    public static int[] calculatePopWindowPos(final View anchorView, final View contentView)
    {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = DensityUtils.getScreenH(anchorView.getContext());
        final int screenWidth = DensityUtils.getScreenW(anchorView.getContext());
        // 测量contentView
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp)
        {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        }
        else
        {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }

    //立即定位
    private void ljDW()
    {
        YHRequestFactory.getRequestManger().postString(GlobalUtils.HOME_HOST, GlobalUtils
                .TERMINAL_LOCATE, null, "{\"sn\":\"" + c_sn + "\"}", new
                HttpCallBack()
                {
                    @Override
                    public void onSuccess(String t)
                    {
                        super.onSuccess(t);
                        final JsonLjDWModel jsonEquipmentModel = JsonUitl.stringToTObject(t,
                                JsonLjDWModel.class);
                        if ("0".equals(jsonEquipmentModel.getResultCode()))
                        {
                            YHViewInject.create().showTips("发送立即定位成功,稍后更新位置！");
                        }
                        else if ("5".equals(jsonEquipmentModel.getResultCode()))
                        {
                            YHViewInject.create().showTips("设备不在线");
                        }
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg)
                    {
                        super.onFailure(errorNo, strMsg);
                        YHViewInject.create().showTips("设备定位超时或接口异常");
                    }

                    @Override
                    public void onFinish()
                    {
                        super.onFinish();
                    }
                }, 6000L, 9000L, 9000L, TAG);
    }


    @Override
    protected void onMenuClick(int postion)
    {
        super.onMenuClick(postion);
        switch (postion)
        {
            case 1:

                break;
            case 4:
                if (StringUtils.isEmpty(jsonEquipmentModel) || StringUtils.isEmpty
                        (jsonEquipmentModel.getDatas()))
                {
                    YHViewInject.create().showTips("数据加载中或者无法获取手表数据！");
                }
                else
                {
                    //显示右侧栏
                    drawer.openDrawer(GravityCompat.END);
                    // 终端设置
//            Intent i = new Intent(aty, SzActivity.class);
//            i.putExtra(JbSzActivity.DATA_ACTION, jsonEquipmentModel.getDatas().get(0));
//            showActivity(aty, i);
                }
                break;
            case 5:
                showActivity(aty, AddTerminalActivity.class);
                break;
        }


    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
//        getLastLoction();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        isWork = false;
    }


    @Override
    protected void onStop()
    {
        super.onStop();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (bdSt != null)
        {
            bdSt.recycle();// 释放图片
            bdSt = null;
        }
        isWork = false;
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mMapView = null;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation)
    {
        if (amapLocation.getErrorCode() == 0)
        {
            //定位成功回调信息，设置相关消息
            amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
            amapLocation.getLatitude();//获取纬度
            amapLocation.getLongitude();//获取经度
            amapLocation.getAccuracy();//获取精度信息
            amapLocation.getAddress();
            //地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
            amapLocation.getCountry();//国家信息
            amapLocation.getProvince();//省信息
            amapLocation.getCity();//城市信息
            amapLocation.getDistrict();//城区信息
            amapLocation.getStreet();//街道信息
            amapLocation.getStreetNum();//街道门牌号信息
            amapLocation.getCityCode();//城市编码
            amapLocation.getAdCode();//地区编码
            amapLocation.getAoiName();//获取当前定位点的AOI信息
            amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
            amapLocation.getFloor();//获取当前室内定位的楼层
            //amapLocation.getGpsStatus();//获取GPS的当前状态
            //获取定位时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(amapLocation.getTime());
            df.format(date);
        }
        else
        {
            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
            LogUtils.e("AmapError", "location Error, ErrCode:"
                    + amapLocation.getErrorCode() + ", errInfo:"
                    + amapLocation.getErrorInfo());
        }
    }

    @Override
    public boolean onItemLongClick(View view, TerminalJosnBen.TerminalModel data, int position)
    {
        return false;
    }

    @Override
    public void onItemClick(View view, TerminalJosnBen.TerminalModel data, int position)
    {
        c_sn = data.getProductSn();
        c_termianlModel = data;
        getLastLoction();
        drawer.closeDrawer(GravityCompat.END);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ploginOut(EventBusBean msg)
    {
        if (!isWork)
        {
            isWork = true;
        }
        mAdapter.getDatas().clear();//必须在数据更新前清空，不能太早
        getData();
    }

    // 按两下返回键退出程序
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if (drawer.isDrawerOpen(GravityCompat.END))
            {
                drawer.closeDrawer(GravityCompat.END);
            }
            else if (mCurPopupWindow != null && mCurPopupWindow.isShowing())
            {
                mCurPopupWindow.dismiss();
            }
            else
            {
                if ((System.currentTimeMillis() - exitTime) > 2000)
                {
                    YHViewInject.create().showTips("再按一次退出程序");
                    exitTime = System.currentTimeMillis();
                }
                else
                {
                    SystemUtils.goHome(aty);
                }
            }

            return true;
        }
        else
        {
            return true;
        }

    }

    @Override
    public void onMapClick(LatLng latLng)
    {
        if (centerMarker.isInfoWindowShown())
        {
            centerMarker.hideInfoWindow();//这个是隐藏infowindow窗口的方法
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        centerMarker = marker;
        return false;
    }
}
