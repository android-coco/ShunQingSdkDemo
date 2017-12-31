package yh.org.shunqinglib.aty;

import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.yh.library.bean.EventBusBean;
import org.yh.library.okhttp.YHRequestFactory;
import org.yh.library.okhttp.callback.HttpCallBack;
import org.yh.library.ui.YHViewInject;
import org.yh.library.utils.JsonUitl;
import org.yh.library.utils.LogUtils;
import org.yh.library.utils.StringUtils;
import org.yh.library.view.YHAlertDialog;
import org.yh.library.view.loading.dialog.YHLoadingDialog;

import yh.org.shunqinglib.R;
import yh.org.shunqinglib.base.BaseActiciy;
import yh.org.shunqinglib.bean.JsonDwSdModel;
import yh.org.shunqinglib.bean.JsonEquipmentModel;
import yh.org.shunqinglib.bean.TerminalJosnBen;
import yh.org.shunqinglib.utils.GlobalUtils;
import yh.org.shunqinglib.view.YhToolbar;

/**
 * 基本设置界面
 */
public class JbSz365Activity extends BaseActiciy
{
    public static final String DATA_ACTION = "equipmentModel";
    // 低电量   开关机
    ImageView btalow_on, btalow_off, pwonff_on, pwonff_off;
    //SOS号码 亲情号码
    EditText terminal_name_rev, terminal_affection, terminal_affection1, terminal_affection2,
            terminal_affection3;
    EditText terminal_name;//设备名称
    LinearLayout del_terminal;//解除绑定
    LinearLayout mdr;//免打扰
    LinearLayout yxhj;//允许呼叫
    LinearLayout nzsz;//闹钟设置
    LinearLayout dssb;//定时上报
    //SOS号码
    RelativeLayout lt_sos_call;

    String btalow = "0";//低电量
    String power = "0";//开关机
    String name = "";//记录更改前的名称

    EditText[] terminal_affections;

    JsonEquipmentModel.EquipmentModel equipmentModel;//设备model

    @Override
    public void setRootView()
    {
        setContentView(R.layout.activity_jbsz365);
        initView();
    }

    private void initView()
    {
        terminal_affections = new EditText[3];
        btalow_on = (ImageView) findViewById(R.id.btalow_on);
        btalow_on.setOnClickListener(this);
        btalow_off = (ImageView) findViewById(R.id.btalow_off);
        btalow_off.setOnClickListener(this);
        pwonff_on = (ImageView) findViewById(R.id.pwonff_on);
        pwonff_on.setOnClickListener(this);
        pwonff_off = (ImageView) findViewById(R.id.pwonff_off);
        pwonff_off.setOnClickListener(this);
        terminal_name_rev = (EditText) findViewById(R.id.terminal_name_rev);
        terminal_affection = (EditText) findViewById(R.id.terminal_affection);
        terminal_affections[0] = terminal_affection;
        terminal_affection1 = (EditText) findViewById(R.id.terminal_affection1);
        terminal_affections[1] = terminal_affection1;
        terminal_affection2 = (EditText) findViewById(R.id.terminal_affection2);
        terminal_affections[2] = terminal_affection2;
//        terminal_affection3 = (EditText) findViewById(R.id.terminal_affection3);
//        terminal_affections[3] = terminal_affection3;
        lt_sos_call = (RelativeLayout) findViewById(R.id.lt_sos_call);
        lt_sos_call.setOnClickListener(this);


        //设备名称
        terminal_name = (EditText) findViewById(R.id.terminal_name);
        //删除设备
        del_terminal = (LinearLayout) findViewById(R.id.del_device);
        del_terminal.setOnClickListener(this);

        mdr = (LinearLayout) findViewById(R.id.mdr);
        mdr.setOnClickListener(this);

        yxhj = (LinearLayout) findViewById(R.id.yxhj);
        yxhj.setOnClickListener(this);

        nzsz = (LinearLayout) findViewById(R.id.nzsz);
        nzsz.setOnClickListener(this);
        dssb = (LinearLayout) findViewById(R.id.dssb);
        dssb.setOnClickListener(this);
    }

    @Override
    public void initData()
    {
        super.initData();
        equipmentModel = (JsonEquipmentModel.EquipmentModel) getIntent().getSerializableExtra
                (DATA_ACTION);
        LogUtils.e(TAG,equipmentModel);
        if (!StringUtils.isEmpty(equipmentModel))
        {
            terminal_name_rev.setText(equipmentModel.getKeysos());
            String number = equipmentModel.getKeynum();
            if (!StringUtils.isEmpty(number))
            {
                String[] numbers = number.split(",");
                int len = numbers.length > terminal_affections.length ? terminal_affections
                        .length : numbers.length;
                for (int i = 0; i < len; i++)
                {
                    terminal_affections[i].setText(numbers[i]);
                }
            }
            btalow = equipmentModel.getFlagBattery();
            power = equipmentModel.getFlagPower();
            name = equipmentModel.getName();
            if ("0".equals(btalow))//关
            {
                btalow_on.setVisibility(View.GONE);
                btalow_off.setVisibility(View.VISIBLE);
            }
            else
            {
                btalow_on.setVisibility(View.VISIBLE);
                btalow_off.setVisibility(View.GONE);
            }

            if ("0".equals(power))//关
            {
                pwonff_on.setVisibility(View.GONE);
                pwonff_off.setVisibility(View.VISIBLE);
            }
            else
            {
                pwonff_on.setVisibility(View.VISIBLE);
                pwonff_off.setVisibility(View.GONE);
            }
            terminal_name.setText(equipmentModel.getName());
            if ("0".equals(equipmentModel.getType()))
            {
                del_terminal.setVisibility(View.VISIBLE);
            }
            else
            {
                del_terminal.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void initWidget()
    {
        super.initWidget();
        toolbar.setLeftTitleText1("返回");
        toolbar.setLeftTitleDrawable1(R.mipmap.icon_back_32px, YhToolbar.LEFT, 20);
        toolbar.setMainTitle("终端设置");
        //toolbar.setRightTitleDrawable1(R.mipmap.img_screening);//保存
        toolbar.setRightTitleText1("保存");
    }

    @Override
    public void widgetClick(View v)
    {
        super.widgetClick(v);
        int i = v.getId();
        if (i == R.id.btalow_on)
        {
            btalow = "0";
            btalow_on.setVisibility(View.GONE);
            btalow_off.setVisibility(View.VISIBLE);
        }
        else if (i == R.id.btalow_off)
        {
            btalow = "1";
            btalow_on.setVisibility(View.VISIBLE);
            btalow_off.setVisibility(View.GONE);
        }
        else if (i == R.id.pwonff_on)
        {
            power = "0";
            pwonff_on.setVisibility(View.GONE);
            pwonff_off.setVisibility(View.VISIBLE);
        }
        else if (i == R.id.pwonff_off)
        {
            power = "1";
            pwonff_on.setVisibility(View.VISIBLE);
            pwonff_off.setVisibility(View.GONE);
        }else if (i == R.id.del_device)
        {
            //删除设备
            new YHAlertDialog(aty).builder().setMsg("解绑后无法恢复,确认解绑？").setNegativeButton("确定", new View
                    .OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    delTerminal();
                }
            }).setPositiveButton("取消", new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                }
            }).show();
        }else if (i == R.id.mdr)
        {
            //免打扰
            showActivity(aty, MdrSdActivity.class);
        }else if (i == R.id.yxhj)
        {
            //允许呼叫
            showActivity(aty, YxFrActivity.class);
        }else if (i == R.id.nzsz)
        {
            //闹钟设置
            showActivity(aty, NzActivity.class);
        }else if (i == R.id.dssb)
        {
            //定时上报
            showActivity(aty, DwSdActivity.class);
        }
    }

    /**
     * 删除设备
     *
     */
    private void delTerminal()
    {
        YHLoadingDialog.make(aty).setMessage("解绑中。。。")//提示消息
                .setCancelable(false).show();
        String params = "{\"uid\":\"" + GlobalUtils.USER_UID + "\",\"sn\":\"" + GlobalUtils.DEIVER_SN + "\"}";
        YHRequestFactory.getRequestManger().postString(GlobalUtils.HOME_HOST, GlobalUtils.TERMINAL_DEL,
                null, params, new
                        HttpCallBack()
                        {
                            @Override
                            public void onSuccess(String t)
                            {
                                super.onSuccess(t);
                                LogUtils.e(TAG, t);
                                final TerminalJosnBen jsonData = JsonUitl.stringToTObject
                                        (t, TerminalJosnBen.class);
                                String resultCode = jsonData.getResultCode();
                                if ("0".equals(resultCode))
                                {
                                    YHViewInject.create().showTips("解绑成功");
                                    //发送广播
                                    EventBus.getDefault().post(new EventBusBean());
                                    finish();
                                }
                                else if ("2".equals(resultCode))
                                {
                                    YHViewInject.create().showTips("解绑失败,设备不存在");
                                }
                                else
                                {
                                    YHViewInject.create().showTips("解绑失败,未知原因code" + resultCode);
                                }
                            }

                            @Override
                            public void onFailure(int errorNo, String strMsg)
                            {
                                super.onFailure(errorNo, strMsg);
                                YHViewInject.create().showTips("解绑失败" + strMsg);
                            }

                            @Override
                            public void onFinish()
                            {
                                super.onFinish();
                                YHLoadingDialog.cancel();
                            }
                        }, TAG);
    }

    @Override
    protected void onMenuClick(int postion)
    {
        super.onMenuClick(postion);
        String keysos = terminal_name_rev.getText().toString().trim();//SOS号码不能为空
        String affection_number = terminal_affection.getText().toString().trim();//主亲情号码不能为空
        if (StringUtils.isEmpty(keysos))
        {
            YHViewInject.create().showTips("SOS号码不能为空！");
        }
        else if (StringUtils.isEmpty(affection_number))
        {
            YHViewInject.create().showTips("主亲情号码不能为空！");
        }
        else
        {
            edit(keysos);
        }
    }

    //判断是否改动
    private boolean isChange()
    {
        boolean isChange = false;
        if (!btalow.equals(equipmentModel.getFlagBattery()) || !power.equals(equipmentModel
                .getFlagPower()) || !terminal_name_rev.getText().toString().trim().equals
                (equipmentModel.getKeysos())
                )
        {
            isChange = true;
        }
        if (!isChange)
        {
            if (!name.equals(terminal_name.getText().toString().trim()))
            {
                isChange = true;
            }
        }
        if (!isChange)
        {
            String number = equipmentModel.getKeynum();
            if (!StringUtils.isEmpty(number))
            {
                String[] numbers = number.split(",");
                int len = numbers.length > terminal_affections.length ? terminal_affections
                        .length : numbers.length;
                for (int i = 0; i < len; i++)
                {
                    if (!terminal_affections[i].getText().toString().trim().equals(numbers[i]))
                    {
                        isChange = true;
                        break;
                    }
                }
            }
        }
        return isChange;
    }

    private void edit(final String keysos)
    {
        YHLoadingDialog.make(aty).setMessage("修改中。。。")//提示消息
                .setCancelable(false).show();
        String keynum = "";
        for (int i = 0; i < terminal_affections.length; i++)
        {
            keynum += terminal_affections[i].getText().toString().trim() + ",";
        }
        if (!StringUtils.isEmpty(keynum) && keynum.endsWith(","))
        {
            keynum = keynum.substring(0, keynum.length() - 1);
        }
        String parameter = "{\"sn\":\"" +
                GlobalUtils.DEIVER_SN + "\",\"keysos\":\"" +
                keysos + "\",\"flag_power\":\"" + power + "\",\"flag_battery\":\"" + btalow +
                "\",\"keynum\":\"" + keynum + "\",\"name\":\"" + terminal_name.getText().toString().trim() + "\"}";
        YHRequestFactory.getRequestManger().postString(GlobalUtils.HOME_HOST, GlobalUtils
                .DEVER_MODIFY, null, parameter, new
                HttpCallBack()
                {
                    @Override
                    public void onSuccess(String t)
                    {
                        super.onSuccess(t);
                        final JsonDwSdModel jsonData = JsonUitl.stringToTObject
                                (t, JsonDwSdModel.class);
                        String resultCode = jsonData.getResultCode();
                        if ("0".equals(resultCode))
                        {
                            YHViewInject.create().showTips("修改成功");
                            YHLoadingDialog.cancel();
                            EventBus.getDefault().post(new EventBusBean());
                            finish();
                        }
                        else if ("5".equals(resultCode))
                        {
                            YHViewInject.create().showTips("修改成功,但是设备不在线,设备启动后同步");
                            YHLoadingDialog.cancel();
                            EventBus.getDefault().post(new EventBusBean());
                            finish();
                        }
                        else
                        {
                            YHViewInject.create().showTips("修改失败");
                        }
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg)
                    {
                        super.onFailure(errorNo, strMsg);
                        YHViewInject.create().showTips("修改失败" + strMsg);
                    }

                    @Override
                    public void onFinish()
                    {
                        super.onFinish();
                        YHLoadingDialog.cancel();
                    }
                }, TAG);
    }

    @Override
    protected void onBackClick(int positon)
    {
        super.onBackClick(positon);
        if (isChange())
        {
            YHViewInject.create().getExitDialog(aty, "编辑状态，是否退出！", null, null, new
                    DialogInterface
                            .OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            finish();
                        }
                    });
        }
        else
        {
            finish();
        }

//        new YHAlertDialog(aty).builder().setTitle("提示").setMsg("编辑状态，是否退出！").setCancelable
// (true).setNegativeButton("确定",new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View i)
//            {
//                finish();
//            }
//        }).setPositiveButton("取消", new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//
//            }
//        }).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if (isChange())
            {
                YHViewInject.create().getExitDialog(aty, "编辑状态，是否退出！", null, null, new
                        DialogInterface
                                .OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                finish();
                            }
                        });
            }
            else
            {
                finish();
            }
            return true;
        }
        else
        {
            return false;
        }
    }
}