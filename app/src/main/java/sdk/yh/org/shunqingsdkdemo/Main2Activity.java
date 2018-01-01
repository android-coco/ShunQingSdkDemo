package sdk.yh.org.shunqingsdkdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.yh.library.utils.PreferenceUtils;

import yh.org.shunqinglib.aty.LocationByBDActivity;
import yh.org.shunqinglib.aty.LocationByGD2Activity;
import yh.org.shunqinglib.utils.GlobalUtils;

/**
 * 普通Activity 调用
 */
public class Main2Activity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("继承AppCompatActivity调用");
        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                GlobalUtils.HOME_HOST = "http://www.shunqing365.net";//接口地址
                GlobalUtils.DEIVER_SN = "A100004DC69883";//SN号
                showActivity(Main2Activity.this, LocationByBDActivity.class);
            }
        });

        findViewById(R.id.ok1).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                GlobalUtils.HOME_HOST = "http://www.shunqing365.net";//接口地址
                GlobalUtils.USER_UID = "29";//SN号
                GlobalUtils.PACKAGE_STR = "sdk.yh.org.shunqingsdkdemo";
                GlobalUtils.LOGIN_ACTIVITY_NAME ="sdk.yh.org.shunqingsdkdemo.Main2Activity";
                showActivity(Main2Activity.this, LocationByGD2Activity.class);
                PreferenceUtils.write(Main2Activity.this,GlobalUtils.USER_XML,"111","111");
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    public void showActivity(Activity aty, Class<?> cls)
    {
        Intent intent = new Intent();
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }
}
