package yh.org.shunqinglib.aty;

import android.app.TimePickerDialog;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import org.greenrobot.eventbus.EventBus;
import org.yh.library.bean.EventBusBean;
import org.yh.library.okhttp.YHRequestFactory;
import org.yh.library.okhttp.callback.HttpCallBack;
import org.yh.library.ui.YHViewInject;
import org.yh.library.utils.JsonUitl;
import org.yh.library.utils.LogUtils;
import org.yh.library.view.YHLabelsView;
import org.yh.library.view.loading.dialog.YHLoadingDialog;

import java.util.ArrayList;
import java.util.Calendar;

import yh.org.shunqinglib.R;
import yh.org.shunqinglib.base.BaseActiciy;
import yh.org.shunqinglib.bean.JsonNzModel;
import yh.org.shunqinglib.utils.GlobalUtils;

/**
 * 添加闹钟
 */
public class NzAddActivity extends BaseActiciy //implements CompoundButton.OnCheckedChangeListener
{

    //星期选择
    YHLabelsView labelsView;
    //CheckBox x_1, x_2, x_3, x_4, x_5, x_6, x_7;
    Button add;
    EditText stime_1, stime_2, alarm_name;
    String week = "";//执行周期

    @Override
    public void setRootView()
    {
        setContentView(R.layout.activity_nzadd);
        initView();
    }


    private void initView()
    {
//        x_1 = (CheckBox) findViewById(R.id.x_1);
//        x_2 = (CheckBox) findViewById(R.id.x_2);
//        x_3 = (CheckBox) findViewById(R.id.x_3);
//        x_4 = (CheckBox) findViewById(R.id.x_4);
//        x_5 = (CheckBox) findViewById(R.id.x_5);
//        x_6 = (CheckBox) findViewById(R.id.x_6);
//        x_7 = (CheckBox) findViewById(R.id.x_7);

        labelsView = (YHLabelsView) findViewById(R.id.labels);
        ArrayList<String> label = new ArrayList<>();
        label.add("周一");
        label.add("周二");
        label.add("周三");
        label.add("周四");
        label.add("周五");
        label.add("周六");
        label.add("周日");
        labelsView.setLabels(label);
        labelsView.setSelectType(YHLabelsView.SelectType.MULTI);
        labelsView.setMaxSelect(0);
//        labelsView.setOnLabelClickListener(new YHLabelsView.OnLabelClickListener() {
//            @Override
//            public void onLabelClick(View label, String labelText, int position) {
//                YHViewInject.create().showTips(position + " : " + labelText);
//                LogUtils.e(TAG,labelsView.getSelectLabels());
//            }
//        });
        add = (Button) findViewById(R.id.add);
        stime_1 = (EditText) findViewById(R.id.stime_1);
        stime_1.setFocusable(false);
        stime_1.setOnClickListener(this);
        stime_2 = (EditText) findViewById(R.id.stime_2);
        stime_2.setFocusable(false);
        stime_2.setOnClickListener(this);
        alarm_name = (EditText) findViewById(R.id.alarm_name);

        add.setOnClickListener(this);
    }

    @Override
    protected void onBackClick()
    {
        super.onBackClick();
        finish();
    }

    @Override
    public void initWidget()
    {
        super.initWidget();
        toolbar.setLeftTitleText("返回");
        toolbar.setMainTitle("添加闹钟");

//        x_1.setOnCheckedChangeListener(this);
//        x_2.setOnCheckedChangeListener(this);
//        x_3.setOnCheckedChangeListener(this);
//        x_4.setOnCheckedChangeListener(this);
//        x_5.setOnCheckedChangeListener(this);
//        x_6.setOnCheckedChangeListener(this);
//        x_7.setOnCheckedChangeListener(this);

        InputFilter[] filters = {new InputFilter.LengthFilter(6)}; // 设置最大长度为6个字符
        alarm_name.setFilters(filters);
    }

    @Override
    public void widgetClick(View v)
    {
        super.widgetClick(v);
        String bh1, bm1;
        int i = v.getId();
        if (i == R.id.add)
        {
            String bh = stime_1.getText().toString().trim();
            String bm = stime_2.getText().toString().trim();
            String name = alarm_name.getText().toString().trim();
            ArrayList<Integer> weeks = labelsView.getSelectLabels();
            week = "";
            //获取选中的星期
            for (int j = 0; j < weeks.size(); j++)
            {
                week += weeks.get(j) + 1;
            }
            if ("".equals(week))
            {
                YHViewInject.create().showTips("执行周期不能为空！");
            }
            else
            {
                if (bh.startsWith("0") && bh.length() >= 2)
                {
                    bh = bh.substring(1);
                }
                if (bm.startsWith("0") && bm.length() >= 2)
                {
                    bm = bm.substring(1);
                }
                upload(bh, bm);
            }

        }
        else if (i == R.id.stime_1)
        {
            bh1 = stime_1.getText().toString().trim();
            bm1 = stime_2.getText().toString().trim();
            if (bh1.startsWith("0") && bh1.length() >= 2)
            {
                bh1 = bh1.substring(1);
            }
            if (bm1.startsWith("0") && bm1.length() >= 2)
            {
                bm1 = bm1.substring(1);
            }
            showTime(Integer.parseInt(bh1), Integer.parseInt(bm1));

        }
        else if (i == R.id.stime_2)
        {
            bh1 = stime_1.getText().toString().trim();
            bm1 = stime_2.getText().toString().trim();
            if (bh1.startsWith("0") && bh1.length() >= 2)
            {
                bh1 = bh1.substring(1);
            }
            if (bm1.startsWith("0") && bm1.length() >= 2)
            {
                bm1 = bm1.substring(1);
            }
            showTime(Integer.parseInt(bh1), Integer.parseInt(bm1));

        }
    }

    private void upload(final String bh, final String bm)
    {
        YHLoadingDialog.make(aty).setMessage("添加中。。。")//提示消息
                .setCancelable(false).show();
        String parameter = "{\"sn\":\"" + GlobalUtils.DEIVER_SN + "\",\"week\":\"" +
                week + "\",\"hour\":\"" + bh + "\",\"minute\":\"" + bm + "\"}";
        YHRequestFactory.getRequestManger().postString(GlobalUtils.HOME_HOST, GlobalUtils
                .ALARM_ADD, null, parameter, new
                HttpCallBack()
                {
                    @Override
                    public void onSuccess(String t)
                    {
                        super.onSuccess(t);
                        final JsonNzModel jsonData = JsonUitl.stringToTObject
                                (t, JsonNzModel.class);
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

//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//    {
//        int i = buttonView.getId();
//        if (i == R.id.x_1)
//        {
//            if (isChecked)
//            {
//                week += "1";
//            }
//            else
//            {
//                week = week.replace("1", "");
//            }
//
//        }
//        else if (i == R.id.x_2)
//        {
//            if (isChecked)
//            {
//                week += "2";
//            }
//            else
//            {
//                week = week.replace("2", "");
//            }
//
//        }
//        else if (i == R.id.x_3)
//        {
//            if (isChecked)
//            {
//                week += "3";
//            }
//            else
//            {
//                week = week.replace("3", "");
//            }
//
//        }
//        else if (i == R.id.x_4)
//        {
//            if (isChecked)
//            {
//                week += "4";
//            }
//            else
//            {
//                week = week.replace("4", "");
//            }
//
//        }
//        else if (i == R.id.x_5)
//        {
//            if (isChecked)
//            {
//                week += "5";
//            }
//            else
//            {
//                week = week.replace("5", "");
//            }
//
//        }
//        else if (i == R.id.x_6)
//        {
//            if (isChecked)
//            {
//                week += "6";
//            }
//            else
//            {
//                week = week.replace("6", "");
//            }
//
//        }
//        else if (i == x_7)
//        {
//            if (isChecked)
//            {
//                week += "7";
//            }
//            else
//            {
//                week = week.replace("7", "");
//            }
//
//        }
//        System.out.println("aaaaaaaaaa:" + week);
//        LogUtils.e(TAG, week);
//    }

    /**
     * 选择时间
     */
    private void showTime(int hour, int minute)
    {
        final Calendar calendar = Calendar.getInstance();
        //final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //final int minute = calendar.get(Calendar.MINUTE);
        LogUtils.e(TAG, hour + "  " + minute);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                setting, hour, minute, true);
        timePickerDialog.show();

    }

    // 当点击TimePickerDialog控件的设置按钮时，调用该方法
    TimePickerDialog.OnTimeSetListener setting = new TimePickerDialog.OnTimeSetListener()
    {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute)
        {
            System.out.println(hour + " ////" + minute);
            String h, m;
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
        }

    };
}
