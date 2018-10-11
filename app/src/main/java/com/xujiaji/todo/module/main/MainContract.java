package com.xujiaji.todo.module.main;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;

import com.xujiaji.todo.base.PresenterLife;
import com.xujiaji.todo.repository.bean.Result;
import com.xujiaji.todo.repository.bean.TodoTypeBean;
import com.xujiaji.todo.repository.remote.DataCallback;

import java.util.List;

import io.xujiaji.xmvp.contracts.XContract;

/**
 * author: xujiaji
 * created on: 2018/10/10 10:06
 * description:
 */
public class MainContract {
    interface View extends XContract.View {
        void displayList(TodoTypeBean todoTypeBean);
        void showChooseTodoCategory();
        void hideChooseTodoCategory();
        void showDeleteTip(int position, TodoTypeBean.TodoListBean.TodoBean todoBean);
    }

    interface Presenter extends XContract.Presenter {
        void requestTodo(int type, SwipeRefreshLayout refreshLayout);
        void requestUpdateTodo(TodoTypeBean.TodoListBean.TodoBean todoBean);
        void requestDelTodo(int id);

        void checkAppUpdate(Activity activity);
    }

    interface Model extends XContract.Model {
        void catTodo(int type, PresenterLife presenterLife, DataCallback<Result<TodoTypeBean>> callback);
        void catUpdateTodo(TodoTypeBean.TodoListBean.TodoBean todoBean, PresenterLife presenterLife, DataCallback<Result> callback);
        void catDelTodo(int id, PresenterLife presenterLife, DataCallback<Result> callback);
    }
}
