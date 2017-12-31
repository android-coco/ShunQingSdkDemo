package yh.org.shunqinglib.aty;

import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import org.yh.library.okhttp.YHRequestFactory;
import org.yh.library.okhttp.callback.HttpCallBack;
import org.yh.library.ui.YHViewInject;
import org.yh.library.utils.JsonUitl;
import org.yh.library.utils.StringUtils;
import org.yh.library.view.loading.dialog.YHLoadingDialog;

import yh.org.shunqinglib.R;
import yh.org.shunqinglib.base.BaseActiciy;
import yh.org.shunqinglib.bean.JsonLjDWModel;
import yh.org.shunqinglib.utils.GlobalUtils;
import yh.org.shunqinglib.utils.PreferenceHelper;
import yh.org.shunqinglib.view.YhToolbar;

/**
 * Created by yhlyl on 2017/10/24.
 * 指定拨号
 */

public class ZdBhActivity extends BaseActiciy
{
    //@BindView(id = R.id.number)
    // 手机输入框
    private AutoCompleteTextView number;
    //@BindView(id = R.id.bh, click = true)
    private Button bh;
    private String[] autoStrs = new String[4];
    private int size = 0;// 当前有多少历史手机号码
    // 输入的号码
    String sNumber;
    @Override
    public void setRootView()
    {
        setContentView(R.layout.zdbh);
        initView();
    }

    private void initView(){
        number = (AutoCompleteTextView) findViewById(R.id.phonenum);
        number.setFilters(new InputFilter[]
                {new InputFilter.LengthFilter(16)});
        ArrayAdapter<String> adapter = new ArrayAdapter<>(aty,
                R.layout.simple_long_report, autoStrs);
        number.setAdapter(adapter);


        bh = (Button) findViewById(R.id.sendlongrep);
        bh.setOnClickListener(this);
    }

    @Override
    public void initData()
    {
        super.initData();
        for (int i = 0; i < autoStrs.length; i++)
        {
            String number = PreferenceHelper.readString(GlobalUtils.user_xml,
                    "number1" + i);
            if (StringUtils.isEmpty(number))
            {
                number = "";
            } else
            {
                size++;
            }
            autoStrs[i] = number;
        }
    }

    @Override
    public void initWidget()
    {
        super.initWidget();
        toolbar.setLeftTitleText1("返回");
        toolbar.setLeftTitleDrawable1(R.mipmap.icon_back_32px, YhToolbar.LEFT,20);
        toolbar.setMainTitle("指定拨号");
    }

    @Override
    protected void onBackClick(int postion)
    {
        super.onBackClick(postion);
        finish();
    }

    @Override
    public void widgetClick(View v)
    {
        super.widgetClick(v);
        int i = v.getId();
        if (i == R.id.sendlongrep)
        {
            sNumber = number.getText().toString();
            if (!StringUtils.isEmpty(sNumber))
            {
                if (sNumber.length() >= 7 && sNumber.length() <= 16)
                {
                    zjBH();
                } else
                {
                    YHViewInject.create().showTips("请输入正确的号码！");
                }
            } else
            {
                YHViewInject.create().showTips("号码不能为空！");
            }

        }
    }

    private void zjBH()
    {
        YHLoadingDialog.make(aty).setMessage("拨号中。。。")//提示消息
                .setCancelable(false).show();
        YHRequestFactory.getRequestManger().postString(GlobalUtils.HOME_HOST, GlobalUtils
                .TERMINAL_CALL, null, "{\"sn\":\"" + GlobalUtils.DEIVER_SN + "\",\"number\":\""
                + sNumber + "\"}", new
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
                            YHViewInject.create().showTips("拨号指令发送成功！");
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
                        boolean f = true;
                        for (int i = 0; i < autoStrs.length; i++)
                        {
                            if (sNumber.equals(autoStrs[i]))
                            {
                                f = false;
                                break;
                            }
                        }
                        if (f)
                        {
                            if (size > 3)
                            {
                                size = (size % 4);
                            }
                            PreferenceHelper.write(GlobalUtils.user_xml,
                                    "number1" + size, sNumber);
                        }
                        YHLoadingDialog.cancel();
                    }
                }, TAG);
    }
}
