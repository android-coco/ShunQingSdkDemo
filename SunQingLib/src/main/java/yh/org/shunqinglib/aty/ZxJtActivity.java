package yh.org.shunqinglib.aty;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.yh.library.okhttp.YHRequestFactory;
import org.yh.library.okhttp.callback.HttpCallBack;
import org.yh.library.ui.YHViewInject;
import org.yh.library.utils.JsonUitl;
import org.yh.library.utils.StringUtils;

import yh.org.shunqinglib.R;
import yh.org.shunqinglib.base.BaseActiciy;
import yh.org.shunqinglib.bean.JsonLjDWModel;
import yh.org.shunqinglib.utils.GlobalUtils;

/**
 * 执行监听
 */
public class ZxJtActivity extends BaseActiciy
{
    EditText number, times;
    Button jt;

    @Override
    public void setRootView()
    {
        setContentView(R.layout.activity_zxjt);
        initView();
    }

    private void initView()
    {
        times = (EditText) findViewById(R.id.times);
        times.setOnClickListener(this);

        number = (EditText) findViewById(R.id.number);

        jt = (Button) findViewById(R.id.jt);
        jt.setOnClickListener(this);
    }

    @Override
    public void initWidget()
    {
        super.initWidget();
        toolbar.setLeftTitleText("返回");
        toolbar.setMainTitle("执行监听");
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
        if (i == R.id.jt)
        {
            if (StringUtils.isEmpty(number.getText().toString()))
            {
                YHViewInject.create().showTips("请输入号码！");
            } else
            {
                zxJt();
            }

        }else if (i == R.id.times)
        {
            getTiems();

        }
    }
    private void getTiems()
    {
        final String[] lang = {"5", "10", "15", "20", "30", "40", "50", "60"};
        new AlertDialog.Builder(this)
                .setTitle("选择监听时间(分)")
                // 标题
                .setSingleChoiceItems(lang, 0,
                        new DialogInterface.OnClickListener()
                        {// 设置条目
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {// 响应事件
                                // do something
                                // 关闭对话框
                                dialog.dismiss();
                                times.setText(lang[which]);
                            }
                        }).show();
    }
    private void zxJt()
    {
        YHRequestFactory.getRequestManger().postString(GlobalUtils.HOME_HOST, GlobalUtils
                .TERMINAL_LISTEN, null, "{\"sn\":\"" + GlobalUtils.DEIVER_SN + "\",\"number\":\""
                + number
                .getText().toString() + "\",\"minute\":\""
                + times
                .getText().toString() + "\"}", new
                HttpCallBack()
                {
                    @Override
                    public void onSuccess(String t)
                    {
                        super.onSuccess(t);
                        final JsonLjDWModel jsonEquipmentModel = JsonUitl.stringToTObject
                                (t, JsonLjDWModel.class);
                        if ("0".equals(jsonEquipmentModel.getResultCode()))
                        {
                            YHViewInject.create().showTips("监听指令发送成功！");
                        }
                        else if ("5".equals(jsonEquipmentModel.getResultCode()))
                        {
                            YHViewInject.create().showTips("设备不在线");
                        }
                        else if ("6".equals(jsonEquipmentModel.getResultCode()))
                        {
                            YHViewInject.create().showTips("设备无反应");
                        }
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg)
                    {
                        super.onFailure(errorNo, strMsg);
                        YHViewInject.create().showTips("未知错误");
                    }

                    @Override
                    public void onFinish()
                    {
                        super.onFinish();
                    }
                }, TAG);
    }
}
