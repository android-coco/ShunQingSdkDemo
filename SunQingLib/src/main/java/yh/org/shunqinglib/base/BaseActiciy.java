package yh.org.shunqinglib.base;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import org.yh.library.YHActivity;
import org.zackratos.ultimatebar.UltimateBar;

import yh.org.shunqinglib.R;
import yh.org.shunqinglib.view.YhToolbar;


/**
 * Created by yhlyl on 2017/4/25.
 */

public abstract class BaseActiciy extends YHActivity
{
    public YhToolbar toolbar;//标题栏
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setColorBar(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initWidget()
    {
        super.initWidget();
        try
        {
            toolbar = (YhToolbar) findViewById(R.id.simple_toolbar);
            toolbar = bindView(R.id.simple_toolbar);
            toolbar.setRightTitleClickListener1(this);
            toolbar.setRightTitleClickListener2(this);
            toolbar.setRightTitleClickListener3(this);
            toolbar.setLeftTitleClickListener1(this);
            toolbar.setLeftTitleClickListener2(this);
            toolbar.setLeftTitleClickListener3(this);
        }
        catch (NullPointerException e)
        {
            throw new NullPointerException(
                    "TitleBar Notfound from Activity layout");
        }
    }

    @Override
    public void widgetClick(View v)
    {
        super.widgetClick(v);
        int i = v.getId();
        if (i == R.id.txt_right_title1)
        {
            onMenuClick(1);

        }else  if (i == R.id.txt_right_title2)
        {
            onMenuClick(2);

        }else  if (i == R.id.txt_right_title3)
        {
            onMenuClick(3);

        }else if (i == R.id.txt_left_title1)
        {
            onBackClick(1);

        }else if (i == R.id.txt_left_title2)
        {
            onBackClick(2);

        }else if (i == R.id.txt_left_title3)
        {
            onBackClick(3);

        }
    }

    protected void onBackClick(int posion)
    {
    }

    protected void onMenuClick(int posion)
    {
    }
}
