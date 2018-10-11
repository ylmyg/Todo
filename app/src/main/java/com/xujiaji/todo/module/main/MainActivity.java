package com.xujiaji.todo.module.main;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xujiaji.happybubble.BubbleDialog;
import com.xujiaji.todo.R;
import com.xujiaji.todo.base.App;
import com.xujiaji.todo.base.BaseActivity;
import com.xujiaji.todo.helper.EmptyViewHelper;
import com.xujiaji.todo.helper.ToolbarHelper;
import com.xujiaji.todo.module.login.LoginDialogActivity;
import com.xujiaji.todo.module.post.PostFragment;
import com.xujiaji.todo.repository.bean.TodoTypeBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends  BaseActivity<MainPresenter> implements MainContract.View, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mRefresh;
    private RecyclerView mTodoListView;
    private TodoAdapter mTodoAdapter;
    private PostFragment mPostFragment;
    private FrameLayout mFragmentContainerView;
    private FloatingActionButton mFab;
    private BubbleDialog mBubbleDialog;
    private BubbleDialog mContentBubbleDialog;
    private TextView mContentBubbleDialogText;
    private View mHeadView;
    private TextView mHeadViewText;
    private int mCategory;
    private final SparseArray<TodoTypeBean> mTypeBeanMap = new SparseArray<>();

    @Override
    public int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onBeforeCreateCircle() {
        super.onBeforeCreateCircle();
        presenter.checkAppUpdate(this);
    }

    @Override
    public void onInitCircle() {
        super.onInitCircle();
        mPostFragment = new PostFragment();

        mFragmentContainerView = findViewById(R.id.fragmentContainer);
        mTodoListView          = findViewById(R.id.todoListView);
        mRefresh               = findViewById(R.id.refresh);
        mFab                   = findViewById(R.id.fab);

        mTodoListView.setAdapter(mTodoAdapter = new TodoAdapter(presenter));
        EmptyViewHelper.initEmpty(mTodoListView);

        mTodoAdapter.addHeaderView(mHeadView = LayoutInflater.from(this).inflate(R.layout.layout_home_head, mTodoListView, false));
        mHeadViewText = mHeadView.findViewById(R.id.tvCategory);

        ToolbarHelper.initPaddingTopDiffBar(mHeadView);

        View view = getLayoutInflater().inflate(R.layout.layout_content, null);
        mContentBubbleDialogText = view.findViewById(R.id.tvContent);
        mContentBubbleDialog = new BubbleDialog(this)
                .addContentView(view)
                .setRelativeOffset(-16)
                .setPosition(BubbleDialog.Position.TOP, BubbleDialog.Position.BOTTOM);


        mRefresh.setColorSchemeResources(R.color.colorAccent, R.color.green_500, R.color.purple_500, R.color.grey_500);
        mRefresh.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onListenerCircle() {
        super.onListenerCircle();
        mRefresh.setOnRefreshListener(this);
        mHeadView.setOnClickListener(v -> showChooseTodoCategory());
        mTodoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultiItemEntity entity = mTodoAdapter.getData().get(position);
                if (entity.getItemType() == TodoAdapter.TYPE_TODO) {
                    TodoTypeBean.TodoListBean.TodoBean todoBean = (TodoTypeBean.TodoListBean.TodoBean) entity;
                    final String content = todoBean.getContent();
                    if (!TextUtils.isEmpty(content)) {
                        mContentBubbleDialogText.setText(content);
                        mContentBubbleDialog.setClickedView(view);
                        mContentBubbleDialog.show();
                    }
                }

            }
        });

        mTodoAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                MultiItemEntity entity = mTodoAdapter.getData().get(position);
                if (entity.getItemType() == TodoAdapter.TYPE_TODO) {
                    TodoTypeBean.TodoListBean.TodoBean todoBean = (TodoTypeBean.TodoListBean.TodoBean) entity;
                    showDeleteTip(position, todoBean);
                    return true;
                }
                return false;
            }
        });
    }



    public void onClickHomeFab(View view) {
        if (App.Login.isOK()) {
            showEnterPost();
        } else {
            LoginDialogActivity.launch(this);
        }
    }

    private void showEnterPost() {
        mFragmentContainerView.setVisibility(View.VISIBLE);
        mFab.hide();
        PostFragment fragment = (PostFragment) getSupportFragmentManager().findFragmentByTag("PostFragment");
        if (fragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, mPostFragment, "PostFragment")
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(fragment)
                    .commit();
        }
    }

    @Override
    public void displayList(TodoTypeBean todoTypeBean) {
        mRefresh.setRefreshing(false);

        mTypeBeanMap.put(todoTypeBean.getType(), todoTypeBean);
        List<MultiItemEntity> src = new ArrayList<>();
        src.add(todoTypeBean);
        todoTypeBean.setTitle(getString(R.string.ing));
        todoTypeBean.setSubItems(todoTypeBean.getTodoList());
        for (TodoTypeBean.TodoListBean todoListBean : todoTypeBean.getTodoList()) {
            todoListBean.setSubItems(todoListBean.getTodoList());
        }

        TodoTypeBean doneBean = new TodoTypeBean();
        doneBean.setTitle(getString(R.string.finished));
        doneBean.setType(todoTypeBean.getType());
        doneBean.setDoneList(todoTypeBean.getDoneList());
        doneBean.setSubItems(todoTypeBean.getDoneList());
        src.add(doneBean);
        for (TodoTypeBean.TodoListBean todoListBean : doneBean.getDoneList()) {
            todoListBean.setSubItems(todoListBean.getTodoList());
        }

        mTodoAdapter.setNewData(src);
        mTodoAdapter.expandAll();
    }

    @Override
    public void showChooseTodoCategory() {
        if (mBubbleDialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_choose_category, null);
            mBubbleDialog = new BubbleDialog(this)
                    .setPosition(BubbleDialog.Position.BOTTOM)
                    .addContentView(view)
                    .setRelativeOffset(-24);
            if (mBubbleDialog.getWindow() != null)
                mBubbleDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            RadioGroup group = view.findViewById(R.id.rgGroup);
            group.check(R.id.rbUseOne);
            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    hideChooseTodoCategory();
                    switch (checkedId) {
                        case R.id.rbUseOne:
                            mHeadViewText.setText(R.string.use_one);
                            mCategory = 0;
                            break;
                        case R.id.rbWork:
                            mHeadViewText.setText(R.string.work);
                            mCategory = 1;
                            break;
                        case R.id.rbLearn:
                            mHeadViewText.setText(R.string.learn);
                            mCategory = 2;
                            break;
                        case R.id.rbLife:
                            mHeadViewText.setText(R.string.life);
                            mCategory = 3;
                            break;
                    }
                    mRefresh.setRefreshing(true);
                    onRefresh();
                }
            });
        }
        mBubbleDialog.setClickedView(mHeadView);
        mBubbleDialog.show();
    }

    @Override
    public void hideChooseTodoCategory() {
        mBubbleDialog.dismiss();
    }

    @Override
    public void showDeleteTip(int position, TodoTypeBean.TodoListBean.TodoBean todoBean) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete)
                .setMessage(todoBean.getTitle())
                .setNegativeButton(R.string.cancel, null)
                .setNeutralButton(R.string.confirm, (dialog, which) -> {
                    mTodoAdapter.remove(position);
                    presenter.requestDelTodo(todoBean.getId());
                })
                .show();
    }

    @Override
    public void onRefresh() {
        presenter.requestTodo(mCategory, mRefresh);
    }


    @Override
    public void onBackPressed() {
        if (mPostFragment.isVisible()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(mPostFragment)
                    .commit();
            mFab.show();
            return;
        }
        super.onBackPressed();
    }
}
