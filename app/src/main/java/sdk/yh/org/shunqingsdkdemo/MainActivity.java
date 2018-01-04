package sdk.yh.org.shunqingsdkdemo;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import org.yh.library.ui.BindView;
import org.yh.library.utils.PreferenceUtils;

import yh.org.shunqinglib.aty.LocationByBDActivity;
import yh.org.shunqinglib.aty.LocationByGD2Activity;
import yh.org.shunqinglib.utils.GlobalUtils;

/**
 * BaseActiciy 调用
 */
public class MainActivity extends Base365Activity
{

    @BindView(id = R.id.ok, click = true)
    Button ok;
    @BindView(id = R.id.ok1, click = true)
    Button ok1;

    @Override
    public void setRootView()
    {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initWidget()
    {
        super.initWidget();
        toolbar.setTitle("继承BaseActiciy调用");
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
    }

    @Override
    public void widgetClick(View v)
    {
        super.widgetClick(v);
        switch (v.getId())
        {
            case R.id.ok:
                GlobalUtils.HOME_HOST = "http://www.shunqing365.net";//接口地址
                GlobalUtils.DEIVER_SN = "A8888888888888";//SN号
                showActivity(aty, LocationByBDActivity.class);
                break;
            case R.id.ok1:
//                GlobalUtils.HOME_HOST = "http://www.shunqing365.net";//接口地址
//                GlobalUtils.DEIVER_SN = "1234516789012345";//SN号
//                showActivity(aty, LocationByGDActivity.class);
                GlobalUtils.HOME_HOST = "http://www.shunqing365.net";//接口地址
                GlobalUtils.USER_UID = "29";//SN号
                GlobalUtils.PACKAGE_STR = "sdk.yh.org.shunqingsdkdemo";
                GlobalUtils.LOGIN_ACTIVITY_NAME ="sdk.yh.org.shunqingsdkdemo.MainActivity";
                showActivity(aty, LocationByGD2Activity.class);
                PreferenceUtils.write(aty,GlobalUtils.USER_XML,"111","111");
                break;
        }
    }

}
