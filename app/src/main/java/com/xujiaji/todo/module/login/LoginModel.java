package com.xujiaji.todo.module.login;

import com.xujiaji.todo.base.PresenterLife;
import com.xujiaji.todo.helper.InputHelper;
import com.xujiaji.todo.helper.ToastHelper;
import com.xujiaji.todo.repository.bean.Result;
import com.xujiaji.todo.repository.bean.UserBean;
import com.xujiaji.todo.repository.remote.CallbackHandler;
import com.xujiaji.todo.repository.remote.DataCallback;
import com.xujiaji.todo.repository.remote.Net;

/**
 * author: xujiaji
 * created on: 2018/10/9 20:47
 * description:
 */
public class LoginModel implements LoginContract.Model {
    @Override
    public void catLogin(String account, String password, PresenterLife presenterLife, DataCallback<Result<UserBean>> callback) {
        if (!isPassInput(account, password)) {
            return;
        }

        Net.getInstance().postLogin(account, password, CallbackHandler.getCallback(presenterLife, callback));
    }

    @Override
    public void catRegister(String account, String password, PresenterLife presenterLife, DataCallback<Result<UserBean>> callback) {
        if (!isPassInput(account, password)) {
            return;
        }
        Net.getInstance().postRegister(account, password, CallbackHandler.getCallback(presenterLife, callback));
    }

    private boolean isPassInput(String account, String password) {
        if (InputHelper.isEmpty(account)) {
            ToastHelper.error("请输入账号");
            return false;
        } else if (InputHelper.isEmpty(password)) {
            ToastHelper.error("请输入密码");
            return false;
        }
        return true;
    }
}
