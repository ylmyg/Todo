package com.xujiaji.todo.repository.remote;

import android.text.TextUtils;

import com.xujiaji.todo.helper.PrefHelper;
import com.xujiaji.todo.repository.bean.Result;
import com.xujiaji.todo.repository.bean.UserBean;
import com.xujiaji.todo.util.NetUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author: xujiaji
 * created on: 2018/10/9 18:59
 * description:
 */
public class Net {

    /**
     * request result ok
     */
    public static final int ZERO = 0;

    public static final String BASE_URL = "http://www.wanandroid.com/";

    public static final String SAVE_USER_LOGIN_KEY = "user/login";
    public static final String SAVE_USER_REGISTER_KEY = "user/register";
    public static final String SET_COOKIE_KEY = "set-cookie";
    public static final String COOKIE_NAME = "Cookie";

    public static final int TIME_OUT_READ = 20;
    public static final int TIME_OUT_CONNECT = 5;

    private static Net mInstance;

    private API api;

    private Net() {

        final Interceptor requestInterceptor = new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder();
                String cookie = PrefHelper.getString(Net.SAVE_USER_LOGIN_KEY);
                if (!TextUtils.isEmpty(cookie)) {
                    builder.addHeader(Net.COOKIE_NAME, cookie);
                }
                return chain.proceed(builder.build());
            }
        };

        final Interceptor responseInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                final String requestUrl = request.url().toString();

                if ((requestUrl.contains(Net.SAVE_USER_LOGIN_KEY) || requestUrl.contains(Net.SAVE_USER_REGISTER_KEY))
                        && !response.headers(Net.SET_COOKIE_KEY).isEmpty()) {
                    PrefHelper.set(Net.SAVE_USER_LOGIN_KEY,
                            NetUtil.encodeCookie(
                                    response.headers(Net.SET_COOKIE_KEY)));
                }
                return response;
            }
        };

        final OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(requestInterceptor)
                .addInterceptor(responseInterceptor)
                .connectTimeout(Net.TIME_OUT_CONNECT, TimeUnit.SECONDS)
                .readTimeout(Net.TIME_OUT_READ, TimeUnit.SECONDS)
                .build();

        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
                .create(API.class);

    }

    public static Net getInstance() {
        if (mInstance == null) {
            synchronized (Net.class) {
                mInstance = new Net();
            }
        }

        return mInstance;
    }

    public void postLogin(String username, String password, Callback<Result<UserBean>> callback) {
        api.postLogin(username, password).enqueue(callback);
    }

    public void postRegister(String username, String password, Callback<Result<UserBean>> callback) {
        api.postRegister(username, password, password).enqueue(callback);
    }




}
