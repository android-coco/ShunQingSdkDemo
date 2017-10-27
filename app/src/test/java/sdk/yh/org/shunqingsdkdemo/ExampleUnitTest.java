package sdk.yh.org.shunqingsdkdemo;

import org.junit.Test;
import org.yh.library.utils.StringUtils;

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
        String keynum = "1,2,2,34,7,";
        if (!StringUtils.isEmpty(keynum) && keynum.endsWith(","))
        {
            keynum = keynum.substring(0, keynum.length()-1);
        }
        System.out.print(keynum);
    }
}