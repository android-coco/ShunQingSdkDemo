package yh.org.shunqinglib.aty;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TimePicker;

import org.greenrobot.eventbus.EventBus;
import org.yh.library.bean.EventBusBean;
import org.yh.library.okhttp.YHRequestFactory;
import org.yh.library.okhttp.callback.HttpCallBack;
import org.yh.library.ui.YHViewInject;
import org.yh.library.utils.JsonUitl;
import org.yh.library.utils.LogUtils;
import org.yh.library.view.loading.dialog.YHLoadingDialog;

import java.util.Calendar;

import yh.org.shunqinglib.R;
import yh.org.shunqinglib.base.BaseActiciy;
import yh.org.shunqinglib.bean.JsonMdrSdModel;
import yh.org.shunqinglib.utils.GlobalUtils;

/**
 * 添加免打扰时段
 */
public class MdrSdAddActivity extends BaseActiciy implements OnCheckedChangeListener
{
    //星期选择
//    @BindView(id = R.id.x_1)
    CheckBox x_1;
//    @BindView(id = R.id.x_2)
    CheckBox x_2;
//    @BindView(id = R.id.x_3)
    CheckBox x_3;
//    @BindView(id = R.id.x_4)
    CheckBox x_4;
//    @BindView(id = R.id.x_5)
    CheckBox x_5;
//    @BindView(id = R.id.x_6)
    CheckBox x_6;
//    @BindView(id = R.id.x_7)
    CheckBox x_7;
//    @BindView(id = R.id.add, click = true)
    Button add;

    //日期选择
//    @BindView(id = R.id.stime_1, click = true)
    EditText stime_1;
//    @BindView(id = R.id.stime_2, click = true)
    EditText stime_2;
//    @BindView(id = R.id.etime_1, click = true)
    EditText etime_1;
//    @BindView(id = R.id.etime_2, click = true)
    EditText etime_2;
//    @BindView(id = R.id.times, click = true)
    EditText times;
//    @BindView(id = R.id.timingpostion_name)
    EditText timingpostion_name;
    String week = "";//星期

    @Override
    public void setRootView()
    {
        setContentView(R.layout.activity_mdrsd_add);
        initView();
    }


    private void initView()
    {
        //星期选择
        x_1 = (CheckBox) findViewById(R.id.x_1);
        x_2 = (CheckBox) findViewById(R.id.x_2);
        x_3 = (CheckBox) findViewById(R.id.x_3);
        x_4 = (CheckBox) findViewById(R.id.x_4);
        x_5 = (CheckBox) findViewById(R.id.x_5);
        x_6 = (CheckBox) findViewById(R.id.x_6);
        x_7 = (CheckBox) findViewById(R.id.x_7);
        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(this);
        //日期选择
        stime_1 = (EditText) findViewById(R.id.stime_1);
        stime_1.setOnClickListener(this);
        stime_2 = (EditText) findViewById(R.id.stime_2);
        stime_2.setOnClickListener(this);
        etime_1 = (EditText) findViewById(R.id.etime_1);
        etime_1.setOnClickListener(this);
        etime_2 = (EditText) findViewById(R.id.etime_2);
        etime_2.setOnClickListener(this);

        times = (EditText) findViewById(R.id.times);
        times.setOnClickListener(this);

        timingpostion_name = (EditText) findViewById(R.id.timingpostion_name);
    }

    @Override
    public void initWidget()
    {
        super.initWidget();
        toolbar.setLeftTitleText("返回");
        toolbar.setMainTitle("添加免打扰时段");

        x_1.setOnCheckedChangeListener(this);
        x_2.setOnCheckedChangeListener(this);
        x_3.setOnCheckedChangeListener(this);
        x_4.setOnCheckedChangeListener(this);
        x_5.setOnCheckedChangeListener(this);
        x_6.setOnCheckedChangeListener(this);
        x_7.setOnCheckedChangeListener(this);
        InputFilter[] filters = {new InputFilter.LengthFilter(6)}; // 设置最大长度为6个字符
        timingpostion_name.setFilters(filters);
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
        String bh1,bm1,eh1,em1;
        int i = v.getId();
        if (i == R.id.add)
        {
            String bh = stime_1.getText().toString().trim();
            String bm = stime_2.getText().toString().trim();
            String eh = etime_1.getText().toString().trim();
            String em = etime_2.getText().toString().trim();
            String name = timingpostion_name.getText().toString().trim();
            String times = this.times.getText().toString().trim();
            if ("".equals(week))
            {
                YHViewInject.create().showTips("执行周期不能为空！");
            } else
            {
                if (bh.startsWith("0") && bh.length() >= 2)
                {
                    bh = bh.substring(1);
                }
                if (bm.startsWith("0") && bm.length() >= 2)
                {
                    bm = bm.substring(1);
                }
                if (eh.startsWith("0") && eh.length() >= 2)
                {
                    eh = eh.substring(1);
                }
                if (em.startsWith("0") && em.length() >= 2)
                {
                    em = em.substring(1);
                }
                upload(bh, bm, eh, em);
            }

        } else if (i == R.id.stime_1)
        {
            whichTime = 0;
            bh1 = stime_1.getText().toString().trim();
            bm1 = stime_2.getText().toString().trim();
            eh1 = etime_1.getText().toString().trim();
            em1 = etime_2.getText().toString().trim();
            if (bh1.startsWith("0") && bh1.length() >= 2)
            {
                bh1 = bh1.substring(1);
            }
            if (bm1.startsWith("0") && bm1.length() >= 2)
            {
                bm1 = bm1.substring(1);
            }
            if (eh1.startsWith("0") && eh1.length() >= 2)
            {
                eh1 = eh1.substring(1);
            }
            if (em1.startsWith("0") && em1.length() >= 2)
            {
                em1 = em1.substring(1);
            }
            showTime(Integer.parseInt(bh1), Integer.parseInt(bm1));

        } else if (i == R.id.stime_2)
        {
            whichTime = 0;
            bh1 = stime_1.getText().toString().trim();
            bm1 = stime_2.getText().toString().trim();
            eh1 = etime_1.getText().toString().trim();
            em1 = etime_2.getText().toString().trim();
            if (bh1.startsWith("0") && bh1.length() >= 2)
            {
                bh1 = bh1.substring(1);
            }
            if (bm1.startsWith("0") && bm1.length() >= 2)
            {
                bm1 = bm1.substring(1);
            }
            if (eh1.startsWith("0") && eh1.length() >= 2)
            {
                eh1 = eh1.substring(1);
            }
            if (em1.startsWith("0") && em1.length() >= 2)
            {
                em1 = em1.substring(1);
            }
            showTime(Integer.parseInt(bh1), Integer.parseInt(bm1));

        } else if (i == R.id.etime_1)
        {
            whichTime = 1;
            bh1 = stime_1.getText().toString().trim();
            bm1 = stime_2.getText().toString().trim();
            eh1 = etime_1.getText().toString().trim();
            em1 = etime_2.getText().toString().trim();
            if (bh1.startsWith("0") && bh1.length() >= 2)
            {
                bh1 = bh1.substring(1);
            }
            if (bm1.startsWith("0") && bm1.length() >= 2)
            {
                bm1 = bm1.substring(1);
            }
            if (eh1.startsWith("0") && eh1.length() >= 2)
            {
                eh1 = eh1.substring(1);
            }
            if (em1.startsWith("0") && em1.length() >= 2)
            {
                em1 = em1.substring(1);
            }
            showTime(Integer.parseInt(eh1), Integer.parseInt(em1));

        } else if (i == R.id.etime_2)
        {
            whichTime = 1;
            bh1 = stime_1.getText().toString().trim();
            bm1 = stime_2.getText().toString().trim();
            eh1 = etime_1.getText().toString().trim();
            em1 = etime_2.getText().toString().trim();
            if (bh1.startsWith("0") && bh1.length() >= 2)
            {
                bh1 = bh1.substring(1);
            }
            if (bm1.startsWith("0") && bm1.length() >= 2)
            {
                bm1 = bm1.substring(1);
            }
            if (eh1.startsWith("0") && eh1.length() >= 2)
            {
                eh1 = eh1.substring(1);
            }
            if (em1.startsWith("0") && em1.length() >= 2)
            {
                em1 = em1.substring(1);
            }
            showTime(Integer.parseInt(eh1), Integer.parseInt(em1));

        } else if (i == R.id.times)
        {
            getTiems();

        }
    }

    private void upload(final String bh, final String bm, final String eh,
                        final String em)
    {
        YHLoadingDialog.make(aty).setMessage("添加中。。。")//提示消息
                .setCancelable(false).show();
        String parameter = "{\"sn\":\"" + GlobalUtils.DEIVER_SN + "\",\"week\":\"" +
                week + "\",\"start_hour\":\"" + bh + "\",\"start_minute\":\"" + bm + "\"," +
                "\"end_hour\":\"" + eh + "\",\"end_minute\":\"" + em + "\"}";
        YHRequestFactory.getRequestManger().postString(GlobalUtils.HOME_HOST, GlobalUtils
                .DISTURB_ADD, null, parameter, new
                HttpCallBack()
                {
                    @Override
                    public void onSuccess(String t)
                    {
                        super.onSuccess(t);
                        final JsonMdrSdModel jsonData = JsonUitl.stringToTObject
                                (t, JsonMdrSdModel.class);
                        String resultCode = jsonData.getResultCode();
                        if ("0".equals(resultCode))
                        {
                            YHViewInject.create().showTips("添加成功");
                            YHLoadingDialog.cancel();
                            EventBus.getDefault().post(new EventBusBean());
                            finish();
                        }else if ("5".equals(resultCode))
                        {
                            YHViewInject.create().showTips("添加成功,但是设备不在线,设备启动后同步");
                            YHLoadingDialog.cancel();
                            EventBus.getDefault().post(new EventBusBean());
                            finish();
                        }
                        else
                        {
                            YHViewInject.create().showTips("添加失败");
                        }
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg)
                    {
                        super.onFailure(errorNo, strMsg);
                        YHViewInject.create().showTips("添加失败");
                    }

                    @Override
                    public void onFinish()
                    {
                        super.onFinish();
                        YHLoadingDialog.cancel();
                    }
                }, TAG);
    }

    private void getTiems()
    {
        final String[] lang = {"5", "10", "15", "20", "30", "40", "50", "60"};
        new AlertDialog.Builder(this)
                .setTitle("选择时隔时间(分)")
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


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        int i = buttonView.getId();
        if (i == R.id.x_1)
        {
            if (isChecked)
            {
                week += "1";
            } else
            {
                week = week.replace("1", "");
            }

        } else if (i == R.id.x_2)
        {
            if (isChecked)
            {
                week += "2";
            } else
            {
                week = week.replace("2", "");
            }

        } else if (i == R.id.x_3)
        {
            if (isChecked)
            {
                week += "3";
            } else
            {
                week = week.replace("3", "");
            }

        } else if (i == R.id.x_4)
        {
            if (isChecked)
            {
                week += "4";
            } else
            {
                week = week.replace("4", "");
            }

        } else if (i == R.id.x_5)
        {
            if (isChecked)
            {
                week += "5";
            } else
            {
                week = week.replace("5", "");
            }

        } else if (i == R.id.x_6)
        {
            if (isChecked)
            {
                week += "6";
            } else
            {
                week = week.replace("6", "");
            }

        } else if (i == R.id.x_7)
        {
            if (isChecked)
            {
                week += "7";
            } else
            {
                week = week.replace("7", "");
            }

        }
        System.out.println("aaaaaaaaaa:" + week);
        LogUtils.e(TAG, week);
    }

    /**
     * 选择时间
     */
    private void showTime(int hour,int minute)
    {
        final Calendar calendar = Calendar.getInstance();
        //final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //final int minute = calendar.get(Calendar.MINUTE);
        LogUtils.e(TAG,hour + "  " + minute);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                setting, hour, minute, true);
        timePickerDialog.show();

    }

    int whichTime = 0;
    // 当点击TimePickerDialog控件的设置按钮时，调用该方法
    TimePickerDialog.OnTimeSetListener setting = new TimePickerDialog.OnTimeSetListener()
    {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute)
        {
            switch (whichTime)
            {
                case 0:// 开始时间
                    System.out.println(hour + " ////" + minute);
                    String h = "00",
                            m = "00";
                    if (hour < 10)
                    {
                        h = "0" + hour;
                    }
                    else
                    {
                        h = hour + "";
                    }
                    if (minute < 10)
                    {
                        m = "0" + minute;
                    }
                    else
                    {
                        m = minute + "";
                    }
                    stime_1.setText(h);
                    stime_2.setText(m);
                    break;
                case 1:// 结束时间
                    String h1 = "00",
                            m1 = "00";
                    if (hour < 10)
                    {
                        h1 = "0" + hour;
                    }
                    else
                    {
                        h1 = hour + "";
                    }
                    if (minute < 10)
                    {
                        m1 = "0" + minute;
                    }
                    else
                    {
                        m1 = minute + "";
                    }
                    etime_1.setText(h1);
                    etime_2.setText(m1);
                    break;
            }
        }

    };
}
