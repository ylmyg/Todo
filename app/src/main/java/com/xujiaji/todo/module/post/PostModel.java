package com.xujiaji.todo.module.post;

import com.xujiaji.todo.base.PresenterLife;
import com.xujiaji.todo.repository.bean.Result;
import com.xujiaji.todo.repository.bean.TodoTypeBean;
import com.xujiaji.todo.repository.remote.CallbackHandler;
import com.xujiaji.todo.repository.remote.DataCallback;
import com.xujiaji.todo.repository.remote.Net;

/**
 * author: xujiaji
 * created on: 2018/10/10 22:02
 * description:
 */
public class PostModel implements PostContract.Model {
    @Override
    public void catAddTodo(TodoTypeBean.TodoListBean.TodoBean todoBean, PresenterLife presenterLife, DataCallback<Result> callback) {
        Net.getInstance().postAddTodo(todoBean, CallbackHandler.getCallback(presenterLife, callback));
    }
}
