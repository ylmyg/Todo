package com.xujiaji.todo.module.login;

import com.xujiaji.todo.base.App;
import com.xujiaji.todo.base.BasePresenter;
import com.xujiaji.todo.repository.bean.Result;
import com.xujiaji.todo.repository.bean.UserBean;
import com.xujiaji.todo.repository.remote.DataCallbackImp;

/**
 * author: xujiaji
 * created on: 2018/10/9 20:47
 * description:
 */
public class LoginPresenter extends BasePresenter<LoginContract.View,LoginModel> implements LoginContract.Presenter {
    @Override
    public void requestLogin(String account, String password) {
        view.displayProgress();
        model.catLogin(account, password, this, new DataCallbackImp<Result<UserBean>>() {

            @Override
            public void finished() {
                super.finished();
                view.dismissProgress();
            }

            @Override
            public void success(Result<UserBean> bean) {
                App.Login.in(bean.getData());
                view.loginSuccess();
            }
        });
    }

    @Override
    public void requestRegister(String account, String password) {
        view.displayProgress();
        model.catRegister(account, password, this, new DataCallbackImp<Result<UserBean>>() {

            @Override
            public void finished() {
                super.finished();
                view.dismissProgress();
            }


            @Override
            public void success(Result<UserBean> bean) {
                App.Login.in(bean.getData());
                view.loginSuccess();
            }
        });
    }
}
