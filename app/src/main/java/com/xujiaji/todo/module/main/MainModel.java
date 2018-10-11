package com.xujiaji.todo.module.main;

import com.xujiaji.todo.base.PresenterLife;
import com.xujiaji.todo.repository.bean.Result;
import com.xujiaji.todo.repository.bean.TodoTypeBean;
import com.xujiaji.todo.repository.remote.CallbackHandler;
import com.xujiaji.todo.repository.remote.DataCallback;
import com.xujiaji.todo.repository.remote.Net;

/**
 * author: xujiaji
 * created on: 2018/10/10 10:06
 * description:
 */
public class MainModel implements MainContract.Model {
    @Override
    public void catTodo(int type, PresenterLife presenterLife, DataCallback<Result<TodoTypeBean>> callback) {
        Net.getInstance().getTodoByType(type, CallbackHandler.getCallback(presenterLife, callback));
    }

    @Override
    public void catUpdateTodo(TodoTypeBean.TodoListBean.TodoBean todoBean, PresenterLife presenterLife, DataCallback<Result> callback) {
        Net.getInstance().postUpdateTodo(todoBean, CallbackHandler.getCallback(presenterLife, callback));
    }

    @Override
    public void catDelTodo(int id, PresenterLife presenterLife, DataCallback<Result> callback) {
        Net.getInstance().postDelTodo(id, CallbackHandler.getCallback(presenterLife, callback));
    }
}
