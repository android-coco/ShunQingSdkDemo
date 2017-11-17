package yh.org.shunqinglib.aty;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.yh.library.okhttp.YHRequestFactory;
import org.yh.library.okhttp.callback.HttpCallBack;
import org.yh.library.ui.YHViewInject;
import org.yh.library.utils.JsonUitl;
import org.yh.library.utils.StringUtils;
import org.yh.library.view.loading.dialog.YHLoadingDialog;

import yh.org.shunqinglib.R;
import yh.org.shunqinglib.app.ShunQingApp;
import yh.org.shunqinglib.base.BaseActiciy;
import yh.org.shunqinglib.bean.JsonLjDWModel;
import yh.org.shunqinglib.utils.GlobalUtils;
import yh.org.shunqinglib.utils.PreferenceHelper;


/**
 * 执行监听
 */
public class ZxJtActivity extends BaseActiciy
{
    Button jt;
    // 选择分钟
    private TextView five, ten, fifteen, twenty, twenty_five, thirty, zero;
    // 通讯录
    RelativeLayout rl_address_book;
    // 是否监听prompt_text
    private ImageView monitor;
    // 手机输入框
    private AutoCompleteTextView numEdit;
    private String[] autoStrs = new String[4];
    private int size = 0;// 当前有多少历史手机号码
    // 默认是不监听
    String mute = "0";
    // 默认5分钟
    String minute = "5";
    // 输入的号码
    String sNumber;

    @Override
    public void setRootView()
    {
        setContentView(R.layout.activity_zxjt);
        initView();
    }

    private void initView()
    {

        five = (TextView) findViewById(R.id.long_num_five);
        five.setOnClickListener(this);
        ten = (TextView) findViewById(R.id.long_num_ten);
        ten.setOnClickListener(this);
        fifteen = (TextView) findViewById(R.id.long_num_fifteen);
        fifteen.setOnClickListener(this);
        twenty = (TextView) findViewById(R.id.long_num_twenty);
        twenty.setOnClickListener(this);
        twenty_five = (TextView) findViewById(R.id.long_num_twenty_five);
        twenty_five.setOnClickListener(this);
        thirty = (TextView) findViewById(R.id.long_num_thirty);
        thirty.setOnClickListener(this);
        zero = (TextView) findViewById(R.id.long_num_zero);
        zero.setOnClickListener(this);


        rl_address_book = (RelativeLayout) findViewById(R.id.rl_address_book);
        rl_address_book.setOnClickListener(this);
        monitor = (ImageView) findViewById(R.id.monitor_model);
        monitor.setOnClickListener(this);

        numEdit = (AutoCompleteTextView) findViewById(R.id.phonenum);
        numEdit.setFilters(new InputFilter[]
                {new InputFilter.LengthFilter(16)});
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.simple_long_report, autoStrs);
        numEdit.setAdapter(adapter);


        jt = (Button) findViewById(R.id.sendlongrep);
        jt.setOnClickListener(this);
    }

    @Override
    public void initData()
    {
        super.initData();
        for (int i = 0; i < autoStrs.length; i++)
        {
            String number = PreferenceHelper.readString(GlobalUtils.user_xml,
                    "number" + i);
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
        if (i == R.id.rl_address_book)// 通讯录
        {
            startActivityForResult(new Intent(Intent.ACTION_PICK,
                    ContactsContract.Contacts.CONTENT_URI), 0);

        } else if (i == R.id.sendlongrep)// 确定
        {
            sNumber = numEdit.getText().toString();
            if (!StringUtils.isEmpty(sNumber))
            {
                if (sNumber.length() >= 7 && sNumber.length() <= 16)
                {
                    zxJt();
                } else
                {
                    YHViewInject.create().showTips("请输入正确的号码！");
                }
            } else
            {
                YHViewInject.create().showTips("号码不能为空！");
            }

        } else if (i == R.id.monitor_model)
        {// 监听选择
            if ("0".equals(mute))
            {
                monitor.setBackgroundResource(R.mipmap.opend);
                mute = "1";
            } else if ("1".equals(mute))
            {
                mute = "0";
                monitor.setBackgroundResource(R.mipmap.no_opend);
            }
        } else if (i == R.id.long_num_five)
        {
            minuteSel();
            five.setBackgroundColor(0);
            five.setTextColor(getResources().getColor(R.color.white));
            minute = "5";
        } else if (i == R.id.long_num_ten)
        {
            minuteSel();
            ten.setBackgroundColor(0);
            ten.setTextColor(getResources().getColor(R.color.white));
            minute = "10";
        } else if (i == R.id.long_num_fifteen)
        {
            minuteSel();
            fifteen.setBackgroundColor(0);
            fifteen.setTextColor(getResources().getColor(R.color.white));
            minute = "15";
        } else if (i == R.id.long_num_twenty)
        {
            minuteSel();
            twenty.setBackgroundColor(0);
            twenty.setTextColor(getResources().getColor(R.color.white));
            minute = "20";
        } else if (i == R.id.long_num_twenty_five)
        {
            minuteSel();
            twenty_five.setBackgroundColor(0);
            twenty_five.setTextColor(getResources().getColor(R.color.white));
            minute = "25";
        } else if (i == R.id.long_num_thirty)
        {
            minuteSel();
            thirty.setBackgroundColor(0);
            thirty.setTextColor(getResources().getColor(R.color.white));
            minute = "30";
        } else if (i == R.id.long_num_zero)
        {
            minuteSel();
            zero.setBackgroundResource(0);
            zero.setTextColor(getResources().getColor(R.color.white));
            minute = "0";
        }
    }

    public void minuteSel()
    {
        // three.setBackgroundColor(getResources().getColor(R.color.white));
        thirty.setBackgroundColor(ContextCompat.getColor(ShunQingApp.getInstance(),R.color.white));
        ten.setBackgroundColor(ContextCompat.getColor(ShunQingApp.getInstance(),R.color.white));
        fifteen.setBackgroundColor(ContextCompat.getColor(ShunQingApp.getInstance(),R.color.white));
        twenty.setBackgroundColor(ContextCompat.getColor(ShunQingApp.getInstance(),R.color.white));
        twenty_five.setBackgroundColor(ContextCompat.getColor(ShunQingApp.getInstance(),R.color.white));
        zero.setBackgroundResource(R.drawable.time_selector_end);
        five.setBackgroundResource(R.drawable.time_selector_start);

        // three.setTextColor(getResources().getColor(R.color.other_grayblack_bg));
        thirty.setTextColor(ContextCompat.getColor(ShunQingApp.getInstance(),R.color.main_tab_bottom_notsel));
        ten.setTextColor(ContextCompat.getColor(ShunQingApp.getInstance(),R.color.main_tab_bottom_notsel));
        fifteen.setTextColor(ContextCompat.getColor(ShunQingApp.getInstance(),R.color.main_tab_bottom_notsel));
        twenty.setTextColor(ContextCompat.getColor(ShunQingApp.getInstance(),R.color.main_tab_bottom_notsel));
        twenty_five.setTextColor(ContextCompat.getColor(ShunQingApp.getInstance(),R.color.main_tab_bottom_notsel));
        zero.setTextColor(ContextCompat.getColor(ShunQingApp.getInstance(),R.color.main_tab_bottom_notsel));
        five.setTextColor(ContextCompat.getColor(ShunQingApp.getInstance(),R.color.main_tab_bottom_notsel));
    }

    private void zxJt()
    {
        YHLoadingDialog.make(aty).setMessage("拨号中。。。")//提示消息
                .setCancelable(false).show();
        YHRequestFactory.getRequestManger().postString(GlobalUtils.HOME_HOST, GlobalUtils
                .TERMINAL_LISTEN, null, "{\"sn\":\"" + GlobalUtils.DEIVER_SN + "\",\"number\":\""
                + sNumber + "\",\"minute\":\""
                + minute + "\"}", new
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
                        } else if ("5".equals(jsonEquipmentModel.getResultCode()))
                        {
                            YHViewInject.create().showTips("设备不在线");
                        } else if ("6".equals(jsonEquipmentModel.getResultCode()))
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
                                    "number" + size, sNumber);
                        }
                        YHLoadingDialog.cancel();
                    }
                }, TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)

    { // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
        {

            if (data != null)
            {

                ContentResolver reContentResolverol = getContentResolver();

                Uri contactData = data.getData();
                @SuppressWarnings("deprecation")
                Cursor cursor = managedQuery(contactData, null, null, null,
                        null);
                if (!StringUtils.isEmpty(cursor))
                {
                    cursor.moveToFirst(); // 获取联系人的姓名
                } else
                {
                    YHViewInject.create().showTips("无法获取到联系人信息,请打开相关权限");
                    return;
                }
                try
                {

                    // 获取用户名
                    String contactId = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.Contacts._ID));

                    Cursor phone = reContentResolverol.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,

                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = " + contactId, null, null);

                    while (phone.moveToNext())
                    {

                        // 获取联系人号码

                        String usernumber = phone
                                .getString(phone
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        // 显示返回的号码
                        numEdit.setText(usernumber);

                    }

                }

                catch (Exception e) // 此处为了防止通讯录提示是否点击禁止而抛出异常

                {

                    YHViewInject.create().showTips("无法获取到联系人信息,请打开相关权限");
                    // 设置空
                }

            }

        }

    }

}
