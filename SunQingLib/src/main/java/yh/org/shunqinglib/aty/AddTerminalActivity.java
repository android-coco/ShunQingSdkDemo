package yh.org.shunqinglib.aty;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.yh.library.bean.EventBusBean;
import org.yh.library.okhttp.YHRequestFactory;
import org.yh.library.okhttp.callback.HttpCallBack;
import org.yh.library.ui.YHViewInject;
import org.yh.library.utils.JsonUitl;
import org.yh.library.utils.LogUtils;
import org.yh.library.view.loading.dialog.YHLoadingDialog;

import yh.org.shunqinglib.R;
import yh.org.shunqinglib.base.BaseActiciy;
import yh.org.shunqinglib.bean.TerminalJosnBen;
import yh.org.shunqinglib.utils.GlobalUtils;
import yh.org.shunqinglib.view.YhToolbar;

/**
 * 添加终端
 */
public class AddTerminalActivity extends BaseActiciy
{

    // SN号输入框
    private AutoCompleteTextView number;
    private Button bh;
    @Override
    public void setRootView()
    {
        setContentView(R.layout.activity_terminal_add);
    }
    @Override
    public void initWidget()
    {
        super.initWidget();
        initView();
        toolbar.setLeftTitleText1("返回");
        toolbar.setLeftTitleDrawable1(R.mipmap.icon_back_32px, YhToolbar.LEFT,20);
        toolbar.setMainTitle("绑定终端");
    }

    private void initView(){
        number = (AutoCompleteTextView) findViewById(R.id.phonenum);
        bh = (Button) findViewById(R.id.sendlongrep);
        bh.setOnClickListener(this);
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
            if (number.getText().toString().trim().length() < 14)
            {
                YHViewInject.create().showTips("MEID输入有误，请重新输入！");
            }else{
                add();
            }
        }
    }

    private void add(){
        YHLoadingDialog.make(aty).setMessage("添加中。。。")//提示消息
                .setCancelable(false).show();
        String params = "{\"uid\":\"" + GlobalUtils.USER_UID + "\",\"sn\":\"" + number.getText().toString() + "\"}";
        YHRequestFactory.getRequestManger().postString(GlobalUtils.HOME_HOST, GlobalUtils.TERMINAL_ADD, null, params, new
                HttpCallBack()
                {
                    @Override
                    public void onSuccess(String t)
                    {
                        super.onSuccess(t);
                        LogUtils.e(TAG,t);
                        final TerminalJosnBen jsonData = JsonUitl.stringToTObject
                                (t, TerminalJosnBen.class);
                        String resultCode = jsonData.getResultCode();
                        if ("0".equals(resultCode))
                        {
                            YHViewInject.create().showTips("添加成功");
                            YHLoadingDialog.cancel();
                            EventBus.getDefault().post(new EventBusBean());
                            finish();
                        } else if("3".equals(resultCode)){
                            YHViewInject.create().showTips("添加失败,设备不存在");
                        }else if("5".equals(resultCode)){
                            YHViewInject.create().showTips("添加失败,设备已经被别人绑定");
                        }else
                        {
                            YHViewInject.create().showTips("添加失败,未知原因code" + resultCode);
                        }
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg)
                    {
                        super.onFailure(errorNo, strMsg);
                        LogUtils.e(TAG, strMsg);
                        YHViewInject.create().showTips("添加失败"+ strMsg);
                    }

                    @Override
                    public void onFinish()
                    {
                        super.onFinish();
                        YHLoadingDialog.cancel();
                    }
                }, TAG);
    }

}
