package com.xujiaji.todo.repository.remote;

import com.xujiaji.todo.repository.bean.Result;
import com.xujiaji.todo.repository.bean.TodoTypeBean;
import com.xujiaji.todo.repository.bean.UserBean;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

    /**
     * 清单
     */
    @GET("/lg/todo/list/{type}/json")
    Call<Result<TodoTypeBean>> getTodoList(@Path("type") int type);

    /**
     * 更新清单
     */
    @POST("lg/todo/update/{id}/json")
    @FormUrlEncoded
    Call<Result> postUpdateTodo(@Path("id")               int id,
                                @Field("title")     String title,
                                @Field("content") String content,
                                @Field("date")       String date,
                                @Field("status")      int status,
                                @Field("type")         int type);

    /**
     * 新增一个todo
     */
    @POST("lg/todo/add/json")
    @FormUrlEncoded
    Call<Result> postAddTodo(
            @Field("title")     String title,
            @Field("content") String content,
            @Field("date")       String date,
            @Field("type")         int type);

    /**
     * 删除一个todo
     */
    @POST("lg/todo/delete/{id}/json")
    Call<Result> postDelTodo(@Path("id") int id);
}
