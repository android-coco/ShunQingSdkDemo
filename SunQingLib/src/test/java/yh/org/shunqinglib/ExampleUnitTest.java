package yh.org.shunqinglib;

import org.junit.Test;

import yh.org.shunqinglib.utils.GlobalUtils;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest
{
    @Test
    public void addition_isCorrect() throws Exception
    {
        assertEquals(4, 2 + 2);
        String x= "该啊的撒法撒旦法昂达拍卖公司阿德法";
        String x1 = GlobalUtils.addStr(12,"\n",x);
        System.out.print(x1);
    }
}