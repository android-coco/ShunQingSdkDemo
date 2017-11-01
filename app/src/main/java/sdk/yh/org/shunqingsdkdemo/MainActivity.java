package sdk.yh.org.shunqingsdkdemo;

import android.view.View;
import android.widget.Button;

import org.yh.library.ui.BindView;

import yh.org.shunqinglib.aty.LocationActivity;
import yh.org.shunqinglib.base.BaseActiciy;
import yh.org.shunqinglib.utils.GlobalUtils;

/**
 * BaseActiciy 调用
 */
public class MainActivity extends BaseActiciy
{

    @BindView(id = R.id.ok, click = true)
    public Button ok;

    @Override
    public void setRootView()
    {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initWidget()
    {
        super.initWidget();
        ok.setText("继承BaseActiciy调用");
    }

    @Override
    public void widgetClick(View v)
    {
        super.widgetClick(v);
        switch (v.getId())
        {
            case R.id.ok:
                GlobalUtils.HOME_HOST = "http://115.159.123.101:8085";//接口地址
                GlobalUtils.DEIVER_SN = "A100004DC57E0D";//SN号
                showActivity(aty, LocationActivity.class);
                break;
        }
    }
}
