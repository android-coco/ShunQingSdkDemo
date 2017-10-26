package sdk.yh.org.shunqingsdkdemo;

import android.view.View;
import android.widget.Button;

import org.yh.library.ui.BindView;

import yh.org.shunqinglib.aty.LocationActivity;
import yh.org.shunqinglib.base.BaseActiciy;

public class MainActivity extends BaseActiciy
{

    @BindView(id = R.id.ok, click = true)
    Button ok;

    @Override
    public void setRootView()
    {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void widgetClick(View v)
    {
        super.widgetClick(v);
        switch (v.getId())
        {
            case R.id.ok:
                showActivity(aty, LocationActivity.class);
                break;
        }
    }
}
