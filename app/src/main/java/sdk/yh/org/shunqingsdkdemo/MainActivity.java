package sdk.yh.org.shunqingsdkdemo;

import android.view.View;
import android.widget.Button;

import org.yh.library.ui.BindView;

import yh.org.shunqinglib.aty.LocationByBDActivity;
import yh.org.shunqinglib.aty.LocationByGDActivity;
import yh.org.shunqinglib.base.BaseActiciy;
import yh.org.shunqinglib.utils.GlobalUtils;

/**
 * BaseActiciy 调用
 */
public class MainActivity extends BaseActiciy
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
    }

    @Override
    public void widgetClick(View v)
    {
        super.widgetClick(v);
        switch (v.getId())
        {
            case R.id.ok:
                GlobalUtils.HOME_HOST = "http://www.shunqing365.net";//接口地址
                GlobalUtils.DEIVER_SN = "A100004DC61119883";//SN号
                showActivity(aty, LocationByBDActivity.class);
                break;
            case R.id.ok1:
                GlobalUtils.HOME_HOST = "http://www.shunqing365.net";//接口地址
                GlobalUtils.DEIVER_SN = "1234516789012345";//SN号
                showActivity(aty, LocationByGDActivity.class);
                break;
        }
    }

}
