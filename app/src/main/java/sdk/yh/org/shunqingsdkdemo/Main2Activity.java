package sdk.yh.org.shunqingsdkdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import yh.org.shunqinglib.aty.LocationByBDActivity;
import yh.org.shunqinglib.aty.LocationByGDActivity;
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
                GlobalUtils.DEIVER_SN = "123456789012345";//SN号
                showActivity(Main2Activity.this, LocationByBDActivity.class);
            }
        });

        findViewById(R.id.ok1).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                GlobalUtils.HOME_HOST = "http://www.shunqing365.net";//接口地址
                GlobalUtils.DEIVER_SN = "123456789012345";//SN号
                showActivity(Main2Activity.this, LocationByGDActivity.class);
            }
        });
    }

    public void showActivity(Activity aty, Class<?> cls)
    {
        Intent intent = new Intent();
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }
}
