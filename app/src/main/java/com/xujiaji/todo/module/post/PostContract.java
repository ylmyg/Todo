package com.xujiaji.todo.module.post;

import com.xujiaji.todo.base.PresenterLife;
import com.xujiaji.todo.repository.bean.Result;
import com.xujiaji.todo.repository.bean.TodoTypeBean;
import com.xujiaji.todo.repository.remote.DataCallback;

import io.xujiaji.xmvp.contracts.XContract;

/**
 * author: xujiaji
 * created on: 2018/10/10 22:02
 * description:
 */
public class PostContract {
    interface View extends XContract.View {
        void hidePage();

        void showChooseCalender();
        void hideChooseCalender();
        void showChooseTodoCategory();
        void hideChooseTodoCategory();

        void showEditContent();
        void hideEditContent();

        void displayAddTodoIng();
        void displayAddTodoFinished();
    }

    interface Presenter extends XContract.Presenter {
        void requestAddTodo(TodoTypeBean.TodoListBean.TodoBean todoBean);
    }

    interface Model extends XContract.Model {
        void catAddTodo(TodoTypeBean.TodoListBean.TodoBean todoBean, PresenterLife presenterLife, DataCallback<Result> callback);
    }
}
