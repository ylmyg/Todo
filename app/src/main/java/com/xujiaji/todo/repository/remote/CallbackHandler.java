package com.xujiaji.todo.repository.remote;

import com.xujiaji.todo.base.PresenterLife;
import com.xujiaji.todo.helper.ToastHelper;
import com.xujiaji.todo.repository.bean.Result;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;

import static com.xujiaji.todo.repository.remote.Net.ZERO;

/**
 * author: xujiaji
 * created on: 2018/10/9 21:13
 * description:
 */
public class CallbackHandler {


    public static   <T extends Result> Callback<T> getCallback(final PresenterLife presenterLife, final DataCallback<T> dataCallback) {
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, retrofit2.Response<T> response) {
                if (presenterLife.isEnd()) return;
                dataCallback.finished();

                final Result result = response.body();
                if (result != null) {
                    if (result.getErrorCode() == ZERO) {
                        dataCallback.success(response.body());
                    } else {
                        dataCallback.fail(result.getErrorCode(), result.getErrorMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                if (presenterLife.isEnd()) return;
                dataCallback.finished();
                if (t instanceof UnknownHostException || t instanceof ConnectException) {
                    ToastHelper.warning("请检查网络");
                } else if (t instanceof TimeoutException || t instanceof SocketTimeoutException) {
                    ToastHelper.warning("连接超时");
                } else {
                    ToastHelper.warning(t.getMessage());
                }
            }
        };
    }

}
