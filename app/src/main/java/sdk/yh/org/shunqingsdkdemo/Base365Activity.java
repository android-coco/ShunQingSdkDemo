package sdk.yh.org.shunqingsdkdemo;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import org.zackratos.ultimatebar.UltimateBar;

import yh.org.shunqinglib.base.BaseActiciy;


/**
 * 作者：游浩 on 2018/1/4 15:34
 * https://github.com/android-coco/YhLibraryForAndroid
 * 邮箱：yh_android@163.com
 */
public abstract class Base365Activity extends BaseActiciy
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setColorBar(ContextCompat.getColor(this, R.color.colorPrimary));
        super.onCreate(savedInstanceState);
    }
}
