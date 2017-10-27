package sdk.yh.org.shunqingsdkdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import yh.org.shunqinglib.aty.LocationActivity;
import yh.org.shunqinglib.utils.GlobalUtils;

/**
 * 普通Activity 调用
 */
public class Main2Activity extends AppCompatActivity
{
    Button ok;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ok = (Button) findViewById(R.id.ok);
        ok.setText("继承AppCompatActivity调用");
        ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                GlobalUtils.HOME_HOST = "http://115.159.123.101:8085";//接口地址
                GlobalUtils.DEIVER_SN = "123456789012345";//SN号
                showActivity(Main2Activity.this, LocationActivity.class);
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
