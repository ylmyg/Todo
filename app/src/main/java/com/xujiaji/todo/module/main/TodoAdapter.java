package com.xujiaji.todo.module.main;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xujiaji.library.RippleCheckBox;
import com.xujiaji.library.RippleCheckBoxUtil;
import com.xujiaji.todo.R;
import com.xujiaji.todo.repository.bean.TodoTypeBean;
import com.xujiaji.todo.repository.remote.Net;
import com.xujiaji.todo.util.DateFormatUtil;

import java.util.ArrayList;

/**
 * author: xujiaji
 * created on: 2018/10/10 10:33
 * description:
 */
public class TodoAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_TODO_TYPE = 0;
    public static final int TYPE_TODO_DATE = 1;
    public static final int TYPE_TODO      = 2;
    private MainPresenter mMainPresenter;

    public TodoAdapter(MainPresenter mainPresenter) {
        super(new ArrayList<>());
        mMainPresenter = mainPresenter;
        addItemType(TYPE_TODO_TYPE, R.layout.item_todo_type_ing_or_finished);
        addItemType(TYPE_TODO_DATE, R.layout.item_todo_time);
        addItemType(TYPE_TODO     , R.layout.item_todo);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_TODO_TYPE:
                TodoTypeBean todoTypeBean = (TodoTypeBean) item;
                if (mContext.getString(R.string.ing).equals(todoTypeBean.getTitle())) {
                    helper
                            .setTextColor(R.id.tvTitle, ContextCompat.getColor(mContext, R.color.purple_500))
                            .setBackgroundColor(R.id.line, ContextCompat.getColor(mContext, R.color.purple_500));
                } else {
                    helper
                            .setTextColor(R.id.tvTitle, ContextCompat.getColor(mContext, R.color.grey_500))
                            .setBackgroundColor(R.id.line, ContextCompat.getColor(mContext, R.color.grey_500));
                }

                helper.setText(R.id.tvTitle, todoTypeBean.getTitle());
                break;
            case TYPE_TODO_DATE:
                final TodoTypeBean.TodoListBean todoListBean = (TodoTypeBean.TodoListBean) item;
                helper.setText(R.id.tvDate, DateFormatUtil.getInstance().format(todoListBean.getDate()));
                break;
            case TYPE_TODO:
                final TodoTypeBean.TodoListBean.TodoBean todoBean = (TodoTypeBean.TodoListBean.TodoBean) item;
                helper.setText(R.id.text, todoBean.getTitle());
                RippleCheckBox checkBox = helper.getView(R.id.rippleCheckBox);
                checkBox.setChecked(todoBean.getStatus() == Net.RIGHT);

                TextView textView = helper.getView(R.id.text);
                if (TextUtils.isEmpty(todoBean.getContent())) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_content_small, 0);
                }

                View l = helper.getView(R.id.line);
                TextView tv = helper.getView(R.id.text);

                if (todoBean.getStatus() == Net.RIGHT) {
                    if (tv.getHeight() == 0) {
                        tv.measure(0, 0);
                    }
                    l.setTranslationY(- (RippleCheckBoxUtil.dp2px(mContext, 12) + tv.getMeasuredHeight() / 2));
                    tv.setAlpha(0.3F);
                } else {
                    l.setTranslationY(0);
                    tv.setAlpha(1F);
                }

                checkBox.setOnCheckedChangeListener(new RippleCheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RippleCheckBox checkBox, boolean isChecked) {
                        if (isChecked) {
                            todoBean.setStatus(Net.RIGHT);
                            l.animate()
                                    .setDuration(400)
                                    .translationY(- (RippleCheckBoxUtil.dp2px(mContext, 12) + tv.getHeight() / 2))
                                    .start();
                            tv.animate()
                                    .setDuration(400)
                                    .alpha(0.3F)
                                    .start();
                        } else {
                            todoBean.setStatus(Net.NO);
                            l.animate()
                                    .setDuration(400)
                                    .translationY(0)
                                    .start();
                            tv.animate()
                                    .setDuration(400)
                                    .alpha(1F)
                                    .start();
                        }
                        mMainPresenter.requestUpdateTodo(todoBean);
                    }
                });

                break;
        }

    }
}
