package yh.org.shunqinglib.aty;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.yh.library.okhttp.YHRequestFactory;
import org.yh.library.okhttp.callback.HttpCallBack;
import org.yh.library.ui.I_PermissionListener;
import org.yh.library.ui.YHViewInject;
import org.yh.library.utils.Constants;
import org.yh.library.utils.JsonUitl;
import org.yh.library.utils.LogUtils;
import org.yh.library.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import yh.org.shunqinglib.R;
import yh.org.shunqinglib.base.BaseActiciy;
import yh.org.shunqinglib.bean.JsonEquipmentModel;
import yh.org.shunqinglib.bean.JsonLjDWModel;
import yh.org.shunqinglib.utils.GlobalUtils;

/**
 * 高德地图
 */
public class LocationByGDActivity extends BaseActiciy implements AMapLocationListener
{
    private static final String TAG = LocationByGDActivity.class.getSimpleName();
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
    LinearLayout location_sos;
    //    @BindView(id = R.id.sleep_time, click = true)
    LinearLayout sleep_time;
    //    @BindView(id = R.id.icon_gcoding, click = true)
    LinearLayout gps_command;

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
    public int ranges = 18;// 缩放级别（zoom）
    private boolean isWork = true;

    JsonEquipmentModel jsonEquipmentModel;

    @Override
    public void setRootView()
    {
        setContentView(R.layout.activity_gd_location);
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
        YHRequestFactory.getRequestManger().postString(GlobalUtils.HOME_HOST, GlobalUtils
                .DEVER_INFO, null, "{\"sns\":\"" + GlobalUtils.DEIVER_SN + "\"}", new HttpCallBack()
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
                            String locateType = jsonEquipmentModel.getDatas().get(0).getLocateType();
                            if (locateType == "0")
                            {
                                locateType = "基站";
                            } else if (locateType == "1")
                            {
                                locateType = "GPS";
                            } else
                            {
                                locateType = "未知";
                            }
                            textStreet.setText("定位方式：" + locateType);
                            String address = jsonEquipmentModel.getDatas().get(0).getAddress();
                            if (!StringUtils.isEmpty(address) && address.length() > 12)
                            {
                                address = GlobalUtils.addStr(12, "\n", address);
                            }
                            textAddress.setText(address);
                            addMaker();
                        }
                    });
                } else
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


        location_sos = (LinearLayout) findViewById(R.id.location_sos);
        location_sos.setOnClickListener(this);

        sleep_time = (LinearLayout) findViewById(R.id.sleep_time);
        sleep_time.setOnClickListener(this);


        gps_command = (LinearLayout) findViewById(R.id.gps_command);
        gps_command.setOnClickListener(this);


        textAddress = (TextView) findViewById(R.id.tv_street);//定位位置
        textLocation = (TextView) findViewById(R.id.tv_location);//定位时间
        textStreet = (TextView) findViewById(R.id.tv_address);//定位类型
    }

    @Override
    public void initWidget()
    {
        super.initWidget();

        //toolbar.setLeftTitleText("返回");
        toolbar.setMainTitle("控制中心");
        toolbar.setRightTitleText1("设置");
        //toolbar.setRightTitleDrawable1(R.mipmap.img_screening, YhToolbar.LEFT);
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
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                centerMarker.getPosition(), ranges));
    }


    @Override
    public void widgetClick(View v)
    {
        super.widgetClick(v);
        int i = v.getId();
        if (i == R.id.location_blood_plu)//执行监听
        {
            if (StringUtils.isEmpty(jsonEquipmentModel) || StringUtils.isEmpty
                    (jsonEquipmentModel.getDatas()))
            {
                YHViewInject.create().showTips("数据加载中或者无法获取手表数据！");
            } else
            {
                showActivity(aty, ZxJtActivity.class);
            }
        } else if (i == R.id.img_position_of)//回到终端位置
        {
            addMaker();
        } else if (i == R.id.location_report)//闹钟
        {
            showActivity(aty, NzActivity.class);
        } else if (i == R.id.location_cry)//免扰时段
        {
            showActivity(aty, MdrSdActivity.class);
        } else if (i == R.id.location_log)//定位时段
        {
            showActivity(aty, DwSdActivity.class);
        } else if (i == R.id.gps_command)//定位记录
        {
            showActivity(aty, DwJlActivity.class);
        } else if (i == R.id.location_rall)//立即定位
        {
            if (StringUtils.isEmpty(jsonEquipmentModel) || StringUtils.isEmpty
                    (jsonEquipmentModel.getDatas()))
            {
                YHViewInject.create().showTips("数据加载中或者无法获取手表数据！");
            } else
            {
                ljDW();
            }

        } else if (i == R.id.location_sos)//指定拨号
        {
            if (StringUtils.isEmpty(jsonEquipmentModel) || StringUtils.isEmpty
                    (jsonEquipmentModel.getDatas()))
            {
                YHViewInject.create().showTips("数据加载中或者无法获取手表数据！");
            } else
            {
                showActivity(aty, ZdBhActivity.class);
            }
        } else if (i == R.id.sleep_time)//允许呼入
        {
            showActivity(aty, YxFrActivity.class);
        }
    }


    //立即定位
    private void ljDW()
    {
        YHRequestFactory.getRequestManger().postString(GlobalUtils.HOME_HOST, GlobalUtils
                .TERMINAL_LOCATE, null, "{\"sn\":\"" + GlobalUtils.DEIVER_SN + "\"}", new
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
                        } else if ("5".equals(jsonEquipmentModel.getResultCode()))
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
    protected void onMenuClick(int posion)
    {
        super.onMenuClick(posion);
        if (StringUtils.isEmpty(jsonEquipmentModel) || StringUtils.isEmpty
                (jsonEquipmentModel.getDatas()))
        {
            YHViewInject.create().showTips("数据加载中或者无法获取手表数据！");
        } else
        {
            // 终端设置
            Intent i = new Intent(aty, SzActivity.class);
            i.putExtra(JbSzActivity.DATA_ACTION, jsonEquipmentModel.getDatas().get(0));
            showActivity(aty, i);
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        getLastLoction();
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
            amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
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
        } else
        {
            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
            LogUtils.e("AmapError", "location Error, ErrCode:"
                    + amapLocation.getErrorCode() + ", errInfo:"
                    + amapLocation.getErrorInfo());
        }
    }
}
