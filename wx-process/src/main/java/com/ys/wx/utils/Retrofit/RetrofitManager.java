package com.ys.wx.utils.Retrofit;

import com.ys.wx.utils.MyHttpLoggingInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * Title :
 * Description :
 * Author : Jerry xu    date : 2017/1/11
 * Update :             date :
 * Version : 1.0.0
 */
public class RetrofitManager {

    private static final int DEFAULT_TIME_OUT = 5;//超时时间 5s
    private static final String api = "https://qyapi.weixin.qq.com/cgi-bin/";//微信接口API前缀
    private Retrofit mRetrofit;

    //构造是有方法
    private RetrofitManager() {
        // 创建 OKHttpClient
        OkHttpClient builder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)//连接超时时间
                .addInterceptor(new MyHttpLoggingInterceptor())//日志拦截
                .build();

        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .client(builder)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//支持RxJava异步处理
                .addConverterFactory(GsonConverterFactory.create())//支持Gson解析转换
                .baseUrl(api)
                .build();
    }

    /**
     * 创建单例
     */
    private static class SingletonHolder {
        private static final RetrofitManager INSTANCE = new RetrofitManager();
    }

    /**
     * 获取单例
     */
    public static RetrofitManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取对应的Service
     *
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }


}
