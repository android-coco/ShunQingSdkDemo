package yh.org.shunqinglib.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import org.yh.library.utils.DensityUtils;

import yh.org.shunqinglib.R;


/**
 * Created by yhlyl on 2017/5/11.
 */

public class YhToolbar extends Toolbar
{
    //int left, int top, int right, int bottom
    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGTH = 3;
    public static final int BOTTOM = 4;
    /**
     * 左侧Title
     */
//    @BindView(id = R.id.txt_left_title)
    private TextView mTxtLeftTitle1;
    private TextView mTxtLeftTitle2;
    private TextView mTxtLeftTitle3;
    /**
     * 中间Title
     */
//    @BindView(id = R.id.txt_main_title)
    private TextView mTxtMiddleTitle;
    /**
     * 右侧Title
     */
//    @BindView(id = R.id.txt_right_title)
    private TextView mTxtRightTitle1;
    private TextView mTxtRightTitle2;
    private TextView mTxtRightTitle3;


    public YhToolbar(Context context)
    {
        super(context);
        //initView();
    }

    public YhToolbar(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        //initView();
    }

    public YhToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        //initView();
    }
    private void initView(){
        mTxtLeftTitle1 = findViewById(R.id.txt_left_title1);
        mTxtLeftTitle2 = findViewById(R.id.txt_left_title2);
        mTxtLeftTitle3 = findViewById(R.id.txt_left_title3);
        mTxtMiddleTitle = findViewById(R.id.txt_main_title);
        mTxtRightTitle1 = findViewById(R.id.txt_right_title1);
        mTxtRightTitle2 = findViewById(R.id.txt_right_title2);
        mTxtRightTitle3 = findViewById(R.id.txt_right_title3);
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        //绑定控件
       // AnnotateUtil.initBindView(this,getRootView());
        mTxtLeftTitle1 = findViewById(R.id.txt_left_title1);
        mTxtLeftTitle2 = findViewById(R.id.txt_left_title2);
        mTxtLeftTitle3 = findViewById(R.id.txt_left_title3);
        mTxtMiddleTitle = findViewById(R.id.txt_main_title);
        mTxtRightTitle1 = findViewById(R.id.txt_right_title1);
        mTxtRightTitle2 = findViewById(R.id.txt_right_title2);
        mTxtRightTitle3 = findViewById(R.id.txt_right_title3);
    }

    //设置中间title的内容
    public void setMainTitle(CharSequence text)
    {
        this.setTitle(" ");
        mTxtMiddleTitle.setVisibility(View.VISIBLE);
        mTxtMiddleTitle.setText(text);
    }



    //设置中间title的内容文字的颜色
    public void setMainTitleColor(int color)
    {
        mTxtMiddleTitle.setVisibility(View.VISIBLE);
        mTxtMiddleTitle.setTextColor(color);
    }

    //设置中间title图标
    public void setMainTitleDrawable(int res,int bearing,int size)
    {
        mTxtMiddleTitle.setVisibility(View.VISIBLE);
        Drawable dwMain = ContextCompat.getDrawable(getContext(), res);
        setMainTitleDrawable(dwMain,bearing,size);

    }

    //设置中间title图标
    public void setMainTitleDrawable(Drawable dwMain,int bearing,int size)
    {
        mTxtMiddleTitle.setVisibility(View.VISIBLE);
        dwMain.setBounds(0, 0, DensityUtils.dip2px(getContext(),size), DensityUtils.dip2px(getContext(),size));
        switch (bearing){
            case LEFT:
                mTxtMiddleTitle.setCompoundDrawables(dwMain, null, null, null);
            case TOP:
                mTxtMiddleTitle.setCompoundDrawables(null, dwMain, null, null);
                break;
            case RIGTH:
                mTxtMiddleTitle.setCompoundDrawables(null, null, dwMain, null);
                break;
            case BOTTOM:
                mTxtMiddleTitle.setCompoundDrawables(null, null, null, dwMain);
                break;
        }
    }

    //设置title左边文字1
    public void setLeftTitleText1(CharSequence text)
    {
        mTxtLeftTitle1.setVisibility(View.VISIBLE);
        mTxtLeftTitle1.setText(text);
    }
    //设置title左边文字2
    public void setLeftTitleText2(CharSequence text)
    {
        mTxtLeftTitle2.setVisibility(View.VISIBLE);
        mTxtLeftTitle2.setText(text);
    }
    //设置title左边文字3
    public void setLeftTitleText3(CharSequence text)
    {
        mTxtLeftTitle3.setVisibility(View.VISIBLE);
        mTxtLeftTitle3.setText(text);
    }

    //设置title左边文字颜色1
    public void setLeftTitleColor1(int color)
    {
        mTxtLeftTitle1.setVisibility(View.VISIBLE);
        mTxtLeftTitle1.setTextColor(color);
    }
    //设置title左边文字颜色2
    public void setLeftTitleColor2(int color)
    {
        mTxtLeftTitle2.setVisibility(View.VISIBLE);
        mTxtLeftTitle2.setTextColor(color);
    }
    //设置title左边文字颜色3
    public void setLeftTitleColor3(int color)
    {
        mTxtLeftTitle3.setVisibility(View.VISIBLE);
        mTxtLeftTitle3.setTextColor(color);
    }

    //设置title左边图标1
    public void setLeftTitleDrawable1(int res,int bearing,int size)
    {
        mTxtLeftTitle1.setVisibility(View.VISIBLE);
        Drawable dwLeft = ContextCompat.getDrawable(getContext(), res);
        //dwLeft.getMinimumWidth(), dwLeft.getMinimumHeight()
        setLeftTitleDrawable1(dwLeft,bearing,size);
    }
    //设置title左边图标2
    public void setLeftTitleDrawable2(int res,int bearing,int size)
    {
        mTxtLeftTitle2.setVisibility(View.VISIBLE);
        Drawable dwLeft = ContextCompat.getDrawable(getContext(), res);
        setLeftTitleDrawable2(dwLeft,bearing,size);
    }
    //设置title左边图标3
    public void setLeftTitleDrawable3(int res,int bearing,int size)
    {
        mTxtLeftTitle3.setVisibility(View.VISIBLE);
        Drawable dwLeft = ContextCompat.getDrawable(getContext(), res);
        setLeftTitleDrawable3(dwLeft,bearing,size);
    }

    //设置title左边图标
    public void setLeftTitleDrawable1(Drawable dwLeft,int bearing,int size)
    {
        mTxtLeftTitle1.setVisibility(View.VISIBLE);
        dwLeft.setBounds(0, 0, DensityUtils.dip2px(getContext(),size), DensityUtils.dip2px(getContext(),size));
        switch (bearing){
            case LEFT:
                mTxtLeftTitle1.setCompoundDrawables(dwLeft, null, null, null);
                break;
            case TOP:
                mTxtLeftTitle1.setCompoundDrawables(null, dwLeft, null, null);
                break;
            case RIGTH:
                mTxtLeftTitle1.setCompoundDrawables(null, null, dwLeft, null);
                break;
            case BOTTOM:
                mTxtLeftTitle1.setCompoundDrawables(null, null, null, dwLeft);
                break;
        }
    }
    //设置title左边图标
    public void setLeftTitleDrawable2(Drawable dwLeft,int bearing,int size)
    {
        mTxtLeftTitle2.setVisibility(View.VISIBLE);
        dwLeft.setBounds(0, 0, DensityUtils.dip2px(getContext(),size), DensityUtils.dip2px(getContext(),size));
        switch (bearing){
            case LEFT:
                mTxtLeftTitle2.setCompoundDrawables(dwLeft, null, null, null);
                break;
            case TOP:
                mTxtLeftTitle2.setCompoundDrawables(null, dwLeft, null, null);
                break;
            case RIGTH:
                mTxtLeftTitle2.setCompoundDrawables(null, null, dwLeft, null);
                break;
            case BOTTOM:
                mTxtLeftTitle2.setCompoundDrawables(null, null, null, dwLeft);
                break;
        }
    }
    //设置title左边图标
    public void setLeftTitleDrawable3(Drawable dwLeft,int bearing,int size)
    {
        mTxtLeftTitle3.setVisibility(View.VISIBLE);
        dwLeft.setBounds(0, 0, DensityUtils.dip2px(getContext(),size), DensityUtils.dip2px(getContext(),size));
        switch (bearing){
            case LEFT:
                mTxtLeftTitle3.setCompoundDrawables(dwLeft, null, null, null);
                break;
            case TOP:
                mTxtLeftTitle3.setCompoundDrawables(null, dwLeft, null, null);
                break;
            case RIGTH:
                mTxtLeftTitle3.setCompoundDrawables(null, null, dwLeft, null);
                break;
            case BOTTOM:
                mTxtLeftTitle3.setCompoundDrawables(null, null, null, dwLeft);
                break;
        }
    }

    //设置title左边点击事件1
    public void setLeftTitleClickListener1(OnClickListener onClickListener)
    {
        mTxtLeftTitle1.setOnClickListener(onClickListener);
    }

    //设置title左边点击事件2
    public void setLeftTitleClickListener2(OnClickListener onClickListener)
    {
        mTxtLeftTitle2.setOnClickListener(onClickListener);
    }
    //设置title左边点击事件3
    public void setLeftTitleClickListener3(OnClickListener onClickListener)
    {
        mTxtLeftTitle3.setOnClickListener(onClickListener);
    }
    //设置title右边文字1
    public void setRightTitleText1(CharSequence text)
    {
        mTxtRightTitle1.setVisibility(View.VISIBLE);
        mTxtRightTitle1.setText(text);
    }

    //设置title右边文字2
    public void setRightTitleText2(CharSequence text)
    {
        mTxtRightTitle2.setVisibility(View.VISIBLE);
        mTxtRightTitle2.setText(text);
    }

    //设置title右边文字3
    public void setRightTitleText3(CharSequence text)
    {
        mTxtRightTitle3.setVisibility(View.VISIBLE);
        mTxtRightTitle3.setText(text);
    }


    //设置title右边文字颜色
    public void setRightTitleColor1(int color)
    {
        mTxtRightTitle1.setVisibility(View.VISIBLE);
        mTxtRightTitle1.setTextColor(color);
    }
    //设置title右边文字颜色
    public void setRightTitleColor2(int color)
    {
        mTxtRightTitle2.setVisibility(View.VISIBLE);
        mTxtRightTitle2.setTextColor(color);
    }
    //设置title右边文字颜色
    public void setRightTitleColor3(int color)
    {
        mTxtRightTitle3.setVisibility(View.VISIBLE);
        mTxtRightTitle3.setTextColor(color);
    }

    //设置title右边图标1
    public void setRightTitleDrawable1(int res,int bearing,int size)
    {
        mTxtRightTitle1.setVisibility(View.VISIBLE);
        Drawable dwRight = ContextCompat.getDrawable(getContext(), res);
        setRightTitleDrawable1(dwRight,bearing,size);
    }
    //设置title右边图标2
    public void setRightTitleDrawable2(int res,int bearing,int size)
    {
        mTxtRightTitle2.setVisibility(View.VISIBLE);
        Drawable dwRight = ContextCompat.getDrawable(getContext(), res);
        setRightTitleDrawable2(dwRight,bearing,size);
    }
    //设置title右边图标3
    public void setRightTitleDrawable3(int res,int bearing,int size)
    {
        mTxtRightTitle3.setVisibility(View.VISIBLE);
        Drawable dwRight = ContextCompat.getDrawable(getContext(), res);
        setRightTitleDrawable3(dwRight,bearing,size);
    }

    //设置title右边图标1
    public void setRightTitleDrawable1(Drawable dwRight,int bearing,int size)
    {
        mTxtRightTitle1.setVisibility(View.VISIBLE);
        dwRight.setBounds(0, 0, DensityUtils.dip2px(getContext(),size), DensityUtils.dip2px(getContext(),size));
        switch (bearing){
            case LEFT:
                mTxtRightTitle1.setCompoundDrawables(dwRight, null, null, null);
                break;
            case TOP:
                mTxtRightTitle1.setCompoundDrawables(null, dwRight, null, null);
                break;
            case RIGTH:
                mTxtRightTitle1.setCompoundDrawables(null, null, dwRight, null);
                break;
            case BOTTOM:
                mTxtRightTitle1.setCompoundDrawables(null, null, null, dwRight);
                break;
        }
    }

    //设置title右边图标2
    public void setRightTitleDrawable2(Drawable dwRight,int bearing,int size)
    {
        mTxtRightTitle2.setVisibility(View.VISIBLE);
        dwRight.setBounds(0, 0, DensityUtils.dip2px(getContext(),size), DensityUtils.dip2px(getContext(),size));
        switch (bearing){
            case LEFT:
                mTxtRightTitle2.setCompoundDrawables(dwRight, null, null, null);
                break;
            case TOP:
                mTxtRightTitle2.setCompoundDrawables(null, dwRight, null, null);
                break;
            case RIGTH:
                mTxtRightTitle2.setCompoundDrawables(null, null, dwRight, null);
                break;
            case BOTTOM:
                mTxtRightTitle2.setCompoundDrawables(null, null, null, dwRight);
                break;
        }
    }

    //设置title右边图标3
    public void setRightTitleDrawable3(Drawable dwRight,int bearing,int size)
    {
        mTxtRightTitle3.setVisibility(View.VISIBLE);
        dwRight.setBounds(0, 0, DensityUtils.dip2px(getContext(),size), DensityUtils.dip2px(getContext(),size));
        switch (bearing){
            case LEFT:
                mTxtRightTitle3.setCompoundDrawables(dwRight, null, null, null);
                break;
            case TOP:
                mTxtRightTitle3.setCompoundDrawables(null, dwRight, null, null);
                break;
            case RIGTH:
                mTxtRightTitle3.setCompoundDrawables(null, null, dwRight, null);
                break;
            case BOTTOM:
                mTxtRightTitle3.setCompoundDrawables(null, null, null, dwRight);
                break;
        }
    }

    //设置title右边点击事件1
    public void setRightTitleClickListener1(OnClickListener onClickListener)
    {
        mTxtRightTitle1.setOnClickListener(onClickListener);
    }
    //设置title右边点击事件
    public void setRightTitleClickListener2(OnClickListener onClickListener)
    {
        mTxtRightTitle2.setOnClickListener(onClickListener);
    }
    //设置title右边点击事件
    public void setRightTitleClickListener3(OnClickListener onClickListener)
    {
        mTxtRightTitle3.setOnClickListener(onClickListener);
    }

}
