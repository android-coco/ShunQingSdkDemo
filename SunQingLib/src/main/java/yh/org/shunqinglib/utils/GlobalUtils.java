package yh.org.shunqinglib.utils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * Created by yhlyl on 2017/10/24.
 */

public class GlobalUtils
{
    public static String HOME_HOST = "http://115.159.123.101:8085";//IP地址
    public static String DEIVER_SN = "123456789012345";//SN号
    //终端信息
    public static final String DEVER_INFO = "/interface/terminal_profile";
    //修改终端信息
    public static final String DEVER_MODIFY = "/interface/terminal_modify";


    //定位时段列表
    public static final String REPORT_LIST = "/interface/report_list";
    //添加时段
    public static final String REPORT_ADD = "/interface/report_addnew";
    //编辑时段
    public static final String REPORT_MODIFY = "/interface/report_modify";
    //删除时段
    public static final String REPORT_DEL = "/interface/report_delete";


    //闹钟列表
    public static final String ALARM_LIST = "/interface/alarm_list";
    //添加闹钟
    public static final String ALARM_ADD = "/interface/alarm_addnew";
    //编辑闹钟
    public static final String ALARM_MODIFY = "/interface/alarm_modify";
    //删除闹钟
    public static final String ALARM_DEL = "/interface/alarm_delete";

    //免打扰时段列表
    public static final String DISTURB_LIST = "/interface/disturb_list";
    //添加时段
    public static final String DISTURB_ADD = "/interface/disturb_addnew";
    //编辑时段
    public static final String DISTURB_MODIFY = "/interface/disturb_modify";
    //删除时段
    public static final String DISTURB_DEL = "/interface/disturb_delete";

    //执行监听
    public static final String TERMINAL_LISTEN = "/interface/terminal_listen";

    //指定拨号
    public static final String TERMINAL_CALL = "/interface/terminal_call";

    //允许呼入
    //号码列表
    public static final String DAIL_LIST = "/interface/dail_list";
    //添加号码
    public static final String DAIL_ADD = "/interface/dail_addnew";
    //删除号码
    public static final String DAIL_DEL = "/interface/dail_delete";


    //定位记录
    public static final String TREMINAL_POSITION = "/interface/terminal_position";
    //删除定位记录
    public static final String TREMINAL_POSITION_DEL = "/interface/terminal_position_delete";

    //立即定位
    public static final String TERMINAL_LOCATE = "/interface/terminal_locate";
    // 用户xml文件名
    public static final String user_xml = "user";

    /**
     * 去掉前后的逗号
     *
     * @param str1 字符串
     * @return 处理后的字符串
     */
    public static String replaceStr(String str1)
    {
        String regex = "^,*|,*$";
        return str1.replaceAll(regex, "");
    }


    /**
     * 得到执行周期1，2，3，4，5，6，7 变为数组
     *
     * @param week 星期字符串12345
     * @return
     */
    public static int[] getWeekStringArrays(String week)
    {
        int[] weekString = new int[7];
        byte[] weeks = week.getBytes();
        Arrays.sort(weeks);
        for (int i = 0; i < weeks.length; i++)
        {
            weekString[i] = weeks[i] - 48;
        }
        return weekString;
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString(long time)
    {
        String str = "";
        long msl = time * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        try
        {
            str = sdf.format(msl);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * 插入方法
     *
     * @param num      插入一个字符串的位置
     * @param splitStr 待指定字符串
     * @param str      原字符串
     * @return 插入指定字符串之后的字符串
     * @throws UnsupportedEncodingException
     */
    public static String addStr(int num, String splitStr, String str)
    {
        if (num > str.length())
        {
            return null;
        }
        String start = str.substring(0, num);
        String end = str.substring(num, str.length());
        String newStr = start + splitStr + end;
        return newStr;
    }


}
