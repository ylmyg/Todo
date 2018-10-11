package com.xujiaji.todo.base;

import com.umeng.analytics.MobclickAgent;

import io.xujiaji.xmvp.presenters.XBasePresenter;
import io.xujiaji.xmvp.view.base.v4.XBaseFragment;

/**
 * author: xujiaji
 * created on: 2018/10/10 22:02
 * description:
 */
public class BaseFragment<T extends XBasePresenter> extends XBaseFragment<T> {

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }
}
