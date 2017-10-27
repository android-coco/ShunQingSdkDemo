package yh.org.shunqinglib.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.yh.library.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cn.qqtheme.framework.picker.DatePicker;
import yh.org.shunqinglib.R;


/**
 * 筛选 日期
 */
public class MyPopup extends PopupWindow implements View.OnClickListener
{
    public Context con;
    PatientOnclick onclick;
    View view;
    private FrameLayout fl_content;
    private TextView tv_content;
    private TextView tv_start;
    private TextView tv_end;
    private TextView tv_determine;
    TextView tv_back;
    ArrayList<String> data = new ArrayList<>();

    public MyPopup(Context mContext, View parent, PatientOnclick onclick1)//, CPMOnclick onclick1
    {
        con = mContext;
        onclick = onclick1;
        view = View.inflate(mContext, R.layout.pop_screening, null);
        view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.popup_in));
        LinearLayout ll_popup = view.findViewById(R.id.ll_popup);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.popup_in_other));
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        showAsDropDown(parent);
        update();
        tv_back = view.findViewById(R.id.tv_back);
        fl_content = view.findViewById(R.id.fl_content);//文书选择
        tv_content = view.findViewById(R.id.tv_content);//选择内容
        tv_start = view.findViewById(R.id.tv_start);//开始时间
        tv_end = view.findViewById(R.id.tv_end);//结束时间
        tv_determine = view.findViewById(R.id.tv_determine);//确定
        // fl_content.setOnClickListener(this);
        tv_content.setOnClickListener(this);
        tv_start.setOnClickListener(this);
        tv_end.setOnClickListener(this);
        tv_determine.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new java.util.Date());
        tv_start.setText(date);
        tv_end.setText(date);
    }

    @Override
    public void onClick(View v)
    {
        int i = v.getId();
        if (i == R.id.tv_back)////消失
        {
            dismiss();

        } else if (i == R.id.tv_determine)//确定
        {
//            String content = tv_content.getText().toString();
            String strat = tv_start.getText().toString();
            String end = tv_end.getText().toString();
            onclick.useOnclick(strat, end);
            dismiss();

        } else if (i == R.id.tv_start)
        {
            String data = tv_start.getText().toString();
            if (!StringUtils.isEmpty(data))
            {
                String[] time = data.split("-");
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);//当前年
                DatePicker picker = new DatePicker((Activity) con, DatePicker.YEAR_MONTH_DAY);
                picker.setRange(2000, year);//年份范围
                picker.setSelectedItem(Integer.valueOf(time[0]), Integer.valueOf(time[1]), Integer.valueOf(time[2]));
                picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener()
                {
                    @Override
                    public void onDatePicked(String year, String month, String day)
                    {
                        tv_start.setText(year + "-" + month + "-" + day);

                    }
                });
                picker.show();
            }

        } else if (i == R.id.tv_end)
        {
            String data1 = tv_end.getText().toString();
            if (!StringUtils.isEmpty(data1))
            {
                String[] time = data1.split("-");
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);//当前年
                DatePicker picker = new DatePicker((Activity) con, DatePicker.YEAR_MONTH_DAY);
                picker.setRange(2000, year);//年份范围
                picker.setSelectedItem(Integer.valueOf(time[0]), Integer.valueOf(time[1]), Integer.valueOf(time[2]));
                picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener()
                {
                    @Override
                    public void onDatePicked(String year, String month, String day)
                    {
                        tv_end.setText(year + "-" + month + "-" + day);

                    }
                });
                picker.show();
            }
        }
    }

    public interface PatientOnclick
    {
        void useOnclick(String startTime, String endTime);//使用
    }

}


