package yh.org.shunqinglib.aty;

import android.Manifest;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import org.yh.library.okhttp.YHRequestFactory;
import org.yh.library.okhttp.callback.HttpCallBack;
import org.yh.library.ui.I_PermissionListener;
import org.yh.library.ui.YHViewInject;
import org.yh.library.utils.Constants;
import org.yh.library.utils.JsonUitl;
import org.yh.library.utils.StringUtils;

import java.util.List;

import yh.org.shunqinglib.R;
import yh.org.shunqinglib.app.SQSDKinit;
import yh.org.shunqinglib.base.BaseActiciy;
import yh.org.shunqinglib.bean.JsonEquipmentModel;
import yh.org.shunqinglib.bean.JsonLjDWModel;
import yh.org.shunqinglib.utils.GlobalUtils;


public class LocationActivity extends BaseActiciy
{
    private static final String TAG = LocationActivity.class.getSimpleName();
    //    @BindView(id = R.id.bmapView)
    MapView mMapView = null;
    //    @BindView(id = R.id.img_position_of, click = true)
    ImageView img_position_of;// 回到手表的位子
    //    @BindView(id = R.id.img_According, click = true)
    ImageView img_According;// 显示或不显示自己的位子

    //    @BindView(id = R.id.location_rall, click = true)
    RelativeLayout location_rall;
    //    @BindView(id = R.id.location_log, click = true)
    RelativeLayout location_log;
    //    @BindView(id = R.id.location_report, click = true)
    RelativeLayout location_report;
    //    @BindView(id = R.id.location_cry, click = true)
    RelativeLayout location_cry;
    //    @BindView(id = R.id.location_blood_plu, click = true)
    RelativeLayout location_blood_plu;
    //    @BindView(id = R.id.location_sos, click = true)
    LinearLayout location_sos;
    //    @BindView(id = R.id.sleep_time, click = true)
    LinearLayout sleep_time;
    //    @BindView(id = R.id.gps_command, click = true)
    LinearLayout gps_command;

    //    @BindView(id = tv_street)
    TextView textAddress;//地址
    TextView textStreet;//定位类型
    TextView textLocation;//定位时间


    private BaiduMap mBaiduMap;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;

    private boolean isWork = true;

    @Override
    public void setRootView()
    {
        setContentView(R.layout.activity_location);
    }

    @Override
    public void initData()
    {
        super.initData();
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

    //获取最后位置
    private void getLastLoction()
    {
        //"{\"sns\":\"123456789012345\"}"
        YHRequestFactory.getRequestManger().postString(SQSDKinit.HOME_HOST, GlobalUtils
                .DEVER_INFO, null, "{\"sns\":\"" + SQSDKinit.DEIVER_SN + "\"}", new HttpCallBack()
        {
            @Override
            public void onSuccess(String t)
            {
                super.onSuccess(t);
                final JsonEquipmentModel jsonEquipmentModel = JsonUitl.stringToTObject
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
                            String locateType = jsonEquipmentModel.getDatas().get(0).getLocateType();
                            if (locateType == "0")
                            {
                                locateType = "基站";
                            }else if (locateType == "1")
                            {
                                locateType = "GPS";
                            }else{
                                locateType = "未知";
                            }
                            textStreet.setText("定位方式：" + locateType);
                            textAddress.setText(jsonEquipmentModel.getDatas().get(0).getAddress());
                            add();
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

        location_rall = (RelativeLayout) findViewById(R.id.location_rall);
        location_rall.setOnClickListener(this);

        location_log = (RelativeLayout) findViewById(R.id.location_log);
        location_log.setOnClickListener(this);


        location_report = (RelativeLayout) findViewById(R.id.location_report);
        location_report.setOnClickListener(this);


        location_cry = (RelativeLayout) findViewById(R.id.location_cry);
        location_cry.setOnClickListener(this);

        location_blood_plu = (RelativeLayout) findViewById(R.id.location_blood_plu);
        location_blood_plu.setOnClickListener(this);


        location_sos = (LinearLayout) findViewById(R.id.location_sos);
        location_sos.setOnClickListener(this);

        sleep_time = (LinearLayout) findViewById(R.id.sleep_time);
        sleep_time.setOnClickListener(this);


        gps_command = (LinearLayout) findViewById(R.id.gps_command);
        gps_command.setOnClickListener(this);


        textAddress = (TextView) findViewById(R.id.tv_street);//定位位置
        textLocation = (TextView) findViewById(R.id.tv_location);//定位时间
        textStreet= (TextView) findViewById(R.id.tv_address);//定位类型
    }

    @Override
    public void initWidget()
    {
        super.initWidget();
        initView();
        //toolbar.setLeftTitleText("返回");
        toolbar.setMainTitle("控制中心");
        toolbar.setRightTitleDrawable(R.mipmap.config_set);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap = mMapView.getMap();
        //地图logo位置
//        mMapView.setLogoPosition(LogoPosition.logoPostionleftTop);
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        //卫星地图
        //mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);

        //空白地图, 基础地图瓦片将不会被渲染。在地图类型中设置为NONE，将不会使用流量下载基础地图瓦片图层。使用场景：与瓦片图层一起使用，节省流量，提升自定义瓦片图下载速度。
        //mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);

        //开启交通图
        //mBaiduMap.setTrafficEnabled(true);
        //百度城市热力图
        //mBaiduMap.setBaiduHeatMapEnabled(true);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        //mLocClient.start();
    }

    private BitmapDescriptor bdSt;
    private BitmapDescriptor bdEn;

    private void add()
    {
        if ((mCurrentLat == mCurrentLon) || mCurrentLat == 0 || mCurrentLon == 0)
        {
            return;
        }
        if (!StringUtils.isEmpty(mBaiduMap))
        {
            mBaiduMap.clear();
        }
        //定义Maker坐标点
        LatLng point = new LatLng(mCurrentLat, mCurrentLon);
        //构建Marker图标
        bdSt = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_gcoding);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bdSt);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);


        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(point);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner extends BDAbstractLocationListener
    {

        @Override
        public void onReceiveLocation(BDLocation location)
        {

            //获取定位结果
            location.getTime();    //获取定位时间
            location.getLocationID();    //获取定位唯一ID，v7.2版本新增，用于排查定位问题
            location.getLocType();    //获取定位类型
            location.getLatitude();    //获取纬度信息
            location.getLongitude();    //获取经度信息
            location.getRadius();    //获取定位精准度
            location.getAddrStr();    //获取地址信息
            location.getCountry();    //获取国家信息
            location.getCountryCode();    //获取国家码
            location.getCity();    //获取城市信息
            location.getCityCode();    //获取城市码
            location.getDistrict();    //获取区县信息
            location.getStreet();    //获取街道信息
            location.getStreetNumber();    //获取街道码
            location.getLocationDescribe();    //获取当前位置描述信息
            location.getPoiList();    //获取当前位置周边POI信息

            location.getBuildingID();    //室内精准定位下，获取楼宇ID
            location.getBuildingName();    //室内精准定位下，获取楼宇名称
            location.getFloor();    //室内精准定位下，获取当前位置所处的楼层信息

            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
            {
                return;
            }

            if (location.getLocType() == BDLocation.TypeGpsLocation)
            {

                //当前为GPS定位结果，可获取以下信息
                location.getSpeed();    //获取当前速度，单位：公里每小时
                location.getSatelliteNumber();    //获取当前卫星数
                location.getAltitude();    //获取海拔高度信息，单位米
                location.getDirection();    //获取方向信息，单位度

            }
            else if (location.getLocType() == BDLocation.TypeNetWorkLocation)
            {

                //当前为网络定位结果，可获取以下信息
                location.getOperators();    //获取运营商信息

            }
            else if (location.getLocType() == BDLocation.TypeOffLineLocation)
            {

                //当前为网络定位结果

            }
            else if (location.getLocType() == BDLocation.TypeServerError)
            {

                //当前网络定位失败
                //可将定位唯一ID、IMEI、定位失败时间反馈至loc-bugs@baidu.com

            }
            else if (location.getLocType() == BDLocation.TypeNetWorkException)
            {

                //当前网络不通

            }
            else if (location.getLocType() == BDLocation.TypeCriteriaException)
            {

                //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
                //可进一步参考onLocDiagnosticMessage中的错误返回码

            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc)
            {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        /**
         * 回调定位诊断信息，开发者可以根据相关信息解决定位遇到的一些问题
         * 自动回调，相同的diagnosticType只会回调一次
         *
         * @param locType           当前定位类型
         * @param diagnosticType    诊断类型（1~9）
         * @param diagnosticMessage 具体的诊断信息释义
         */
        public void onLocDiagnosticMessage(int locType, int diagnosticType, String
                diagnosticMessage)
        {

            if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_GPS)
            {

                //建议打开GPS

            }
            else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_WIFI)
            {

                //建议打开wifi，不必连接，这样有助于提高网络定位精度！

            }
            else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_LOC_PERMISSION)
            {

                //定位权限受限，建议提示用户授予APP定位权限！

            }
            else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_NET)
            {

                //网络异常造成定位失败，建议用户确认网络状态是否异常！

            }
            else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CLOSE_FLYMODE)
            {

                //手机飞行模式造成定位失败，建议用户关闭飞行模式后再重试定位！

            }
            else if (diagnosticType == LocationClient
                    .LOC_DIAGNOSTIC_TYPE_NEED_INSERT_SIMCARD_OR_OPEN_WIFI)
            {

                //无法获取任何定位依据，建议用户打开wifi或者插入sim卡重试！

            }
            else if (diagnosticType == LocationClient
                    .LOC_DIAGNOSTIC_TYPE_NEED_OPEN_PHONE_LOC_SWITCH)
            {

                //无法获取有效定位依据，建议用户打开手机设置里的定位开关后重试！

            }
            else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_SERVER_FAIL)
            {

                //百度定位服务端定位失败
                //建议反馈location.getLocationID()和大体定位时间到loc-bugs@baidu.com

            }
            else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_FAIL_UNKNOWN)
            {

                //无法获取有效定位依据，但无法确定具体原因
                //建议检查是否有安全软件屏蔽相关定位权限
                //或调用LocationClient.restart()重新启动后重试！

            }
        }

        public void onReceivePoi(BDLocation poiLocation)
        {
        }
    }


    @Override
    public void widgetClick(View v)
    {
        super.widgetClick(v);
        int i = v.getId();
        if (i == R.id.location_blood_plu)//执行监听
        {
            showActivity(aty, ZxJtActivity.class);
        }
        else if (i == R.id.img_position_of)//回到终端位置
        {
            add();
        }
        else if (i == R.id.location_report)//闹钟
        {
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
            showActivity(aty,DwJlActivity.class);
        }
        else if (i == R.id.location_rall)//立即定位
        {
            ljDW();
        }
        else if (i == R.id.location_sos)//指定拨号
        {
            showActivity(aty, ZdBhActivity.class);
        }
        else if (i == R.id.sleep_time)//允许呼入
        {
            showActivity(aty, YxFrActivity.class);
        }
    }


    //立即定位
    private void ljDW()
    {
        YHRequestFactory.getRequestManger().postString(SQSDKinit.HOME_HOST, GlobalUtils
                .TERMINAL_LOCATE, null, "{\"sn\":\"" + SQSDKinit.DEIVER_SN + "\"}", new
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
                            YHViewInject.create().showTips("发送立即定位成功");
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
    protected void onMenuClick()
    {
        super.onMenuClick();
        // 终端设置
        YHViewInject.create().showTips("设置");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (bdSt != null && bdEn != null)
        {
            bdSt.recycle();// 释放图片
            bdEn.recycle();
        }
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
    }

}
