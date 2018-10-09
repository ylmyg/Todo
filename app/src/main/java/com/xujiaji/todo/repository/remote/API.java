package com.xujiaji.todo.repository.remote;

import com.xujiaji.todo.repository.bean.Result;
import com.xujiaji.todo.repository.bean.UserBean;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * author: xujiaji
 * created on: 2018/10/9 18:25
 * description:
 */
public interface API {
    /**
     * 登录
     */
    @POST("user/login")
    @FormUrlEncoded
    Call<Result<UserBean>> postLogin(@Field("username") String username, @Field("password") String password);

    /**
     * 注册
     */
    @POST("user/register")
    @FormUrlEncoded
    Call<Result<UserBean>> postRegister(@Field("username") String username, @Field("password") String password, @Field("repassword") String repassword);

}
