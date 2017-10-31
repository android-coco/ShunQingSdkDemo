package yh.org.shunqinglib.aty;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import org.yh.library.ui.YHViewInject;
import org.yh.library.utils.StringUtils;

import yh.org.shunqinglib.R;
import yh.org.shunqinglib.base.BaseActiciy;
import yh.org.shunqinglib.bean.JsonEquipmentModel;

/**
 * 作者：游浩 on 2017/10/31 10:45
 * https://github.com/android-coco/YhLibraryForAndroid
 * 邮箱：yh_android@163.com
 * 设置
 */
public class SzActivity extends BaseActiciy
{

    RelativeLayout sz, dwsd, nz, mdrsd, yxfj, dwjl;
    JsonEquipmentModel.EquipmentModel equipmentModel;//设备model

    @Override
    public void setRootView()
    {
        setContentView(R.layout.activity_sz);
        initView();
    }

    private void initView()
    {
        sz = (RelativeLayout) findViewById(R.id.lt_health_circle_friends);
        dwsd = (RelativeLayout) findViewById(R.id.lt_health_accessory_person);
        nz = (RelativeLayout) findViewById(R.id.lt_health_nicole);
        mdrsd = (RelativeLayout) findViewById(R.id.lt_health_food);
        yxfj = (RelativeLayout) findViewById(R.id.lt_game);
        dwjl = (RelativeLayout) findViewById(R.id.lt_mall);
        sz.setOnClickListener(this);
        dwsd.setOnClickListener(this);
        nz.setOnClickListener(this);
        mdrsd.setOnClickListener(this);
        yxfj.setOnClickListener(this);
        dwjl.setOnClickListener(this);
    }

    @Override
    public void initData()
    {
        super.initData();
        equipmentModel = (JsonEquipmentModel.EquipmentModel) getIntent().getSerializableExtra
                (JbSzActivity.DATA_ACTION);
    }

    @Override
    public void initWidget()
    {
        super.initWidget();
        toolbar.setLeftTitleText("返回");
        toolbar.setMainTitle("终端设置");
    }

    @Override
    protected void onBackClick()
    {
        super.onBackClick();
        finish();
    }

    @Override
    public void widgetClick(View v)
    {
        super.widgetClick(v);
        int i = v.getId();
        if (i == R.id.lt_health_circle_friends)//基本设置
        {
            if (!StringUtils.isEmpty(equipmentModel))
            {
                Intent i1 = new Intent(aty, JbSzActivity.class);
                i1.putExtra(JbSzActivity.DATA_ACTION, equipmentModel);
                showActivity(aty, i1);
            } else
            {
                YHViewInject.create().showTips("数据加载中请稍候！");
            }

        } else if (i == R.id.lt_health_accessory_person)//定时定位
        {
            showActivity(aty, DwSdActivity.class);
        } else if (i == R.id.lt_health_nicole)//闹钟
        {
            showActivity(aty, NzActivity.class);
        } else if (i == R.id.lt_health_food)//免扰时段
        {
            showActivity(aty, MdrSdActivity.class);
        } else if (i == R.id.lt_game)//允许呼入
        {
            showActivity(aty, YxFrActivity.class);
        } else if (i == R.id.lt_mall)//定位记录
        {
            showActivity(aty, DwJlActivity.class);
        }
    }
}
