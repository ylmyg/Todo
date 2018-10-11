package com.xujiaji.todo.base;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.xujiaji.todo.BuildConfig;
import com.xujiaji.todo.helper.PrefHelper;
import com.xujiaji.todo.listener.GenericEventListener;
import com.xujiaji.todo.repository.bean.UserBean;
import com.xujiaji.todo.repository.remote.Net;

import java.util.ArrayList;
import java.util.List;

/**
 * author: xujiaji
 * created on: 2018/10/9 19:06
 * description:
 */
public class App extends Application {

    private static App instance;

    @NonNull
    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Login.isOK = PrefHelper.isExist(Net.SAVE_USER_LOGIN_KEY);
        initUM();
    }

    private void initUM() {
        UMConfigure.init(this, "5bbf2736f1f55691100000c2", null, UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

    }

    /**
     * 登录状态
     */
    public static class Login {

        private static final List<GenericEventListener<UserBean>> liveEventList = new ArrayList<>();

        private static boolean isOK;

        /**
         * 订阅登录成功事件
         */
        public static void subscribe(GenericEventListener<UserBean> listener) {
            liveEventList.add(listener);
        }

        /**
         *  当前是否登录
         */
        public static boolean isOK() {
            return isOK;
        }

        /**
         * 已登录
         */
        public static void in(UserBean userBean) {
            isOK = true;
            PrefHelper.set(PrefHelper.USER_INFO, new Gson().toJson(userBean));

            for (GenericEventListener<UserBean> le : liveEventList) {
                le.event(userBean);
            }
        }

        /**
         * 登出
         */
        public static void out() {
            PrefHelper.clearKey(Net.SAVE_USER_LOGIN_KEY);
            PrefHelper.clearKey(PrefHelper.USER_INFO);
            for (GenericEventListener<UserBean> le : liveEventList) {
                le.event(null);
            }
            isOK = false;
        }
    }

}
