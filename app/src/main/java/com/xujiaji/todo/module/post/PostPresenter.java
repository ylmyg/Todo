package com.xujiaji.todo.module.post;

import com.xujiaji.todo.R;
import com.xujiaji.todo.base.App;
import com.xujiaji.todo.base.BasePresenter;
import com.xujiaji.todo.helper.ToastHelper;
import com.xujiaji.todo.repository.bean.Result;
import com.xujiaji.todo.repository.bean.TodoTypeBean;
import com.xujiaji.todo.repository.remote.DataCallbackImp;

/**
 * author: xujiaji
 * created on: 2018/10/10 22:02
 * description:
 */
public class PostPresenter extends BasePresenter<PostContract.View,PostModel> implements PostContract.Presenter {


    @Override
    public void requestAddTodo(TodoTypeBean.TodoListBean.TodoBean todoBean) {
        view.displayAddTodoIng();
        model.catAddTodo(todoBean, this, new DataCallbackImp<Result>() {

            @Override
            public void finished() {
                super.finished();
                view.displayAddTodoFinished();
            }

            @Override
            public void success(Result bean) {
                ToastHelper.success(App.getInstance().getString(R.string.success_add));
            }
        });
    }
}
