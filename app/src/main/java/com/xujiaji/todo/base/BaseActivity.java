package com.xujiaji.todo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.umeng.analytics.MobclickAgent;
import com.xujiaji.todo.helper.ToolbarHelper;

import io.xujiaji.xmvp.presenters.XBasePresenter;
import io.xujiaji.xmvp.view.base.XBaseActivity;

/**
 * author: xujiaji
 * created on: 2018/10/7 21:16
 * description:
 */
public abstract class BaseActivity<T extends XBasePresenter> extends XBaseActivity<T> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ToolbarHelper.initTranslucent(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
