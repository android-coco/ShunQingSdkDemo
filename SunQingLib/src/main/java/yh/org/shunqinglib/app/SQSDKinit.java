package yh.org.shunqinglib.app;

import android.app.Application;
import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.yh.library.db.YhDBManager;
import org.yh.library.okhttp.OkHttpUtils;
import org.yh.library.okhttp.https.HttpsUtils;
import org.yh.library.okhttp.utils.HeaderInterceptor;
import org.yh.library.okhttp.utils.LoggerInterceptor;
import org.yh.library.utils.Constants;
import org.yh.library.utils.DensityUtils;
import org.yh.library.utils.LogUtils;
import org.yh.library.utils.StringUtils;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import yh.org.shunqinglib.R;


/**
 * 作者：游浩 on 2017/10/26 13:54
 * https://github.com/android-coco/YhLibraryForAndroid
 * 邮箱：yh_android@163.com
 */

public class SQSDKinit
{
    public static final String HOME_HOST = "http://115.159.123.101:8085";//IP地址
    public static final String DEIVER_SN = "123456789012345";
    private static final String TAG = SQSDKinit.class.getSimpleName();
    private static final long cacheSize = 1024 * 1024 * 20;//缓存文件最大限制大小20M
    //默认屏幕宽高
    public static int width = 1080;
    public static int height = 960;
    private static Context mInstance = null;

    private SQSDKinit()
    {
    }


    public static void init(Context app)
    {
        mInstance = app;
        Constants.Config.app = (Application) app;//框架用到上下文对象
        width = DensityUtils.getScreenW(app); //屏幕宽度
        height = DensityUtils.getScreenH(app);//屏幕高度
        if (width == 0)
        {
            width = 1080;
        }

        if (height == 0)
        {
            height = 960;
        }
        LogUtils.e(TAG, "ShunQingApp onCreate() height：" + height + " width：" + width);
        initSystem();
        // 网络框架初始化
        initHttp();
        //初始化小视频录制
    }

    /**
     * 初始化系统信息
     */
    private static void initSystem()
    {

        if (!StringUtils.isEmpty(mInstance))
        {
            // 图片缓存框架初始化
            Constants.placeholderImgID = R.drawable.img_default;//加载中的资源(图片或者自定义图形)
            Constants.errorImgID = -1;//错误的资源(图片或者自定义图形)
            //initImageLoader(mInstance);
            //根据不同的用户生成不同的数据库
            Constants.Config.yhDBManager = YhDBManager.getInstance(mInstance, "yh.db", true);
        }
        // 发布BUG用邮件形式发送
//        if (!LogUtils.isDebug)
//        {
//            CrashHandler.create(mInstance);
//            sendEmail = new SendEmailThread();
//            sendEmail.start();
//        }
    }


    /**
     * 初始化OKHTTP
     */
    public static void initHttp()
    {
        //全局设置请求头  单独设置请求头覆盖全局设置
//        Map<String, String> headers = new LinkedHashMap<>();
//        headers.put("imei", "123123123");
//        headers.put("version", "1.0");
//        headers.put("token", "");
//        headers.put("regid", "123123123");
//        YHRequestFactory.getRequestManger().setHeaders(headers);
        //缓存http
        //Cache cache = new Cache(new File(FileUtils.getSavePath(Constants.httpCachePath)), cacheSize);
        //cookie
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(mInstance));
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null,
                null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60000L, TimeUnit.MILLISECONDS)
                .readTimeout(60000L, TimeUnit.MILLISECONDS)
                .writeTimeout(60000L, TimeUnit.MILLISECONDS)
//                .retryOnConnectionFailure(true)//允许重试
                .addInterceptor(new LoggerInterceptor("", true))//日志拦截 是否显示返回数据
//                .addInterceptor(new RetryInterceptor(3))//重试3次
                .addInterceptor(new HeaderInterceptor())// 统一请求头
//                .addNetworkInterceptor(new Interceptor()//添加网络拦截器缓存用
//                {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException
//                    {
//                        Request request = chain.request();
//                        Response response = chain.proceed(request);
//                        if (NetWorkUtils.isConnectedByState(getInstance()))
//                        {
//                            int maxAge = 60 * 60;// 有网 就1个小时可用 缓存有效时间
//                            return response.newBuilder()
//                                    .header("Cache-Control", "public, max-age=" + maxAge)
//                                    .build();
//                        } else
//                        {
//                            int maxStale = 60 * 60 * 24 * 7;// 没网 就1周可用 缓存有效时间
//                            return response.newBuilder()
//                                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                                    .build();
//                        }
//                    }
//                })
                // .cache(cache)//添加缓存
                .hostnameVerifier(new HostnameVerifier()
                {
                    @Override
                    public boolean verify(String hostname, SSLSession session)
                    {
                        return true;
                    }
                })
                .cookieJar(cookieJar)
//                .cookieJar(new CookieJar()
//                {
//                    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
//
//                    @Override
//                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies)
//                    {
//                        cookieStore.put(url.host(), cookies);
//                        if (!StringUtils.isEmpty(cookieStore.get(url)))
//                        {
//                            LogUtils.e("cookieStore.get(url)", "" + cookieStore.get(url).size());
//                        }
//                        LogUtils.e("saveFromResponse", "" + cookies);
//                    }
//
//                    @Override
//                    public List<Cookie> loadForRequest(HttpUrl url)
//                    {
//                        if (!StringUtils.isEmpty(cookieStore.get(url.host())))
//                        {
//                            LogUtils.e("cookieStore.get(url)", "" + cookieStore.get(url.host())
//                                    .size());
//                        }
//                        List<Cookie> cookies = cookieStore.get(url.host());
//                        LogUtils.e("loadForRequest", "" + cookies);
//                        return cookies != null ? cookies : new ArrayList<Cookie>();
//
//                    }
//                })
                .sslSocketFactory(sslParams.sSLSocketFactory,
                        sslParams.trustManager).build();
        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * 发送邮件线程
     *
     * @author youhao
     */
    private static class SendEmailThread extends Thread
    {
        @Override
        public void run()
        {
            super.run();

            //SystemUtils.sendLogEmail();
        }
    }
}
