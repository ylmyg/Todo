package com.xujiaji.todo.base;

import io.xujiaji.xmvp.contracts.XContract;
import io.xujiaji.xmvp.presenters.XBasePresenter;

/**
 * author: xujiaji
 * created on: 2018/10/9 21:00
 * description:
 */
public class BasePresenter<T extends XContract.View, E extends XContract.Model>  extends XBasePresenter<T, E> implements PresenterLife {

    @Override
    public boolean isEnd() {
        return view == null;
    }
}
