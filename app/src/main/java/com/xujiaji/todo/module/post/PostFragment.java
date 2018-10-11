package com.xujiaji.todo.module.post;

import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.xujiaji.happybubble.BubbleDialog;
import com.xujiaji.todo.R;
import com.xujiaji.todo.base.App;
import com.xujiaji.todo.base.BaseFragment;
import com.xujiaji.todo.helper.InputHelper;
import com.xujiaji.todo.helper.ToastHelper;
import com.xujiaji.todo.helper.ToolbarHelper;
import com.xujiaji.todo.module.main.MainActivity;
import com.xujiaji.todo.repository.bean.TodoTypeBean;
import com.xujiaji.todo.util.SoftKeyUtil;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

import io.doist.datetimepicker.date.DatePicker;

/**
 * author: xujiaji
 * created on: 2018/10/10 22:02
 * description:
 */
public class PostFragment extends BaseFragment<PostPresenter> implements PostContract.View, View.OnClickListener {

    private EditText mEtInput;
    private ImageView mBtnChooseCalendar;
    private ImageView mBtnTypeList;
    private ImageView mBtnContent;
    private ImageView mBtnOk;
    private DatePicker mDatePicker;
    private ProgressBar mProgressBar;
    private BubbleDialog mBubbleDialog;
    private BubbleDialog mEditContentBubbleDialog;

    private @ColorInt int grey = ContextCompat.getColor(App.getInstance(), R.color.grey_500);
    private @ColorInt int green = ContextCompat.getColor(App.getInstance(), R.color.green_500);

    private String mDate;
    private int mCategory = 0;
    private String mContent;

    @Override
    public int layoutId() {
        return R.layout.fragment_post;
    }

    @Override
    public void onInitCircle() {
        super.onInitCircle();
        ToolbarHelper.initPaddingTopDiffBar(getRootView().findViewById(R.id.layoutTop));

        mBtnChooseCalendar = getRootView().findViewById(R.id.btnChooseCalendar);
        mBtnTypeList       = getRootView().findViewById(R.id.btnTypeList);
        mProgressBar       = getRootView().findViewById(R.id.progressBar);
        mBtnContent        = getRootView().findViewById(R.id.btnContent);
        mDatePicker        = getRootView().findViewById(R.id.datePicker);
        mEtInput           = getRootView().findViewById(R.id.etInput);
        mBtnOk             = getRootView().findViewById(R.id.btnOk);

        mDatePicker.setMinDate(System.currentTimeMillis());
        mDatePicker.setMaxDate(System.currentTimeMillis() + 356L * 24L * 60L * 60L * 1000L);

        onVisible();
    }

    @Override
    public void onVisible() {
        super.onVisible();
        mDate = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(System.currentTimeMillis());
        mCategory = 0;
        mContent = null;

        mBtnChooseCalendar.setColorFilter(grey);
        mBtnTypeList.setColorFilter(grey);
        mBtnContent.setColorFilter(grey);

        mEtInput.requestFocus();
        SoftKeyUtil.show(Objects.requireNonNull(getActivity()), mEtInput);

        mBubbleDialog = null;
        mEditContentBubbleDialog = null;
    }

    @Override
    public void onListenerCircle() {
        super.onListenerCircle();
        getRootView().findViewById(R.id.btnBack)      .setOnClickListener(this);
        getRootView().findViewById(R.id.rootContainer).setOnClickListener(this);

        final View v = getRootView().findViewById(R.id.rootContainer);
        mEtInput.post(() -> SoftKeyUtil.doMoveLayout( v,  v, new View[]{mDatePicker}, 0));

        mBtnChooseCalendar.setOnClickListener(this);
        mBtnContent.setOnClickListener(this);
        mBtnTypeList.setOnClickListener(this);
        mBtnOk.setOnClickListener(this);

        mDatePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mBtnChooseCalendar.setColorFilter(green);
                mDate = String.format("%s-%s-%s", year, monthOfYear + 1, dayOfMonth);
                hideChooseCalender();
            }
        });

        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (InputHelper.isEmpty(mEtInput)) {
                    mBtnOk.setColorFilter(grey);
                } else {
                    mBtnOk.setColorFilter(green);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChooseCalendar:
                showChooseCalender();
                break;
            case R.id.btnTypeList:
                showChooseTodoCategory();
                break;
            case R.id.btnContent:
                showEditContent();
                break;
            case R.id.btnBack:
            case R.id.rootContainer:
                hidePage();
                break;
            case R.id.btnOk:
                addTodo();
                break;
        }
    }

    private void addTodo() {
        final String title = InputHelper.toString(mEtInput);
        if (TextUtils.isEmpty(title)) {
            ToastHelper.info(getString(R.string.please_input_todo));
            return;
        }
        TodoTypeBean.TodoListBean.TodoBean todoBean = new TodoTypeBean.TodoListBean.TodoBean();
        todoBean.setTitle(title);
        todoBean.setType(mCategory);
        todoBean.setContent(mContent);
        todoBean.setDateStr(mDate);
        presenter.requestAddTodo(todoBean);
    }

    @Override
    public void hidePage() {
        mEtInput.setText("");
        SoftKeyUtil.hide(Objects.requireNonNull(getActivity()));
        Objects.requireNonNull(getActivity()).onBackPressed();
    }

    @Override
    public void showChooseCalender() {
        SoftKeyUtil.hide(Objects.requireNonNull(getActivity()), mEtInput);
        mDatePicker.postDelayed(() -> mDatePicker.setVisibility(View.VISIBLE), 200);
    }

    @Override
    public void hideChooseCalender() {
        mDatePicker.setVisibility(View.GONE);
    }

    @Override
    public void showChooseTodoCategory() {
        if (mBubbleDialog == null) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_choose_category, null);
            mBubbleDialog = new BubbleDialog(getActivity())
                    .setPosition(BubbleDialog.Position.TOP)
                    .addContentView(view);
            RadioGroup group = view.findViewById(R.id.rgGroup);
            group.check(R.id.rbUseOne);
            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    hideChooseTodoCategory();
                    mBtnTypeList.setColorFilter(green);
                    switch (checkedId) {
                        case R.id.rbUseOne:
                            mCategory = 0;
                            break;
                        case R.id.rbWork:
                            mCategory = 1;
                            break;
                        case R.id.rbLearn:
                            mCategory = 2;
                            break;
                        case R.id.rbLife:
                            mCategory = 3;
                            break;
                    }
                }
            });
        }

        mBubbleDialog.setClickedView(mBtnTypeList);
        mBubbleDialog.show();

        if (mBubbleDialog.getWindow() != null)
            mBubbleDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void hideChooseTodoCategory() {
        mBubbleDialog.dismiss();
    }

    @Override
    public void showEditContent() {
        if (mEditContentBubbleDialog == null) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_add_content, null);
            mEditContentBubbleDialog = new BubbleDialog(Objects.requireNonNull(getActivity()))
                    .addContentView(view)
                    .setPosition(BubbleDialog.Position.TOP);

            EditText editText = view.findViewById(R.id.etEditContent);
            view.findViewById(R.id.btnOk).setOnClickListener(v -> {
                mContent = InputHelper.toString(editText);
                if (TextUtils.isEmpty(mContent)) {
                    mBtnContent.setColorFilter(grey);
                } else {
                    mBtnContent.setColorFilter(green);
                }
                hideEditContent();
            });
        }

        mEditContentBubbleDialog.setClickedView(mBtnContent);
        mEditContentBubbleDialog.show();
        if (mEditContentBubbleDialog.getWindow() != null)
            mEditContentBubbleDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void hideEditContent() {
        mEditContentBubbleDialog.dismiss();
    }

    @Override
    public void displayAddTodoIng() {
        mBtnOk.setEnabled(false);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayAddTodoFinished() {
        mBtnOk.setEnabled(true);
        mEtInput.setText("");
        mProgressBar.setVisibility(View.GONE);
        hidePage();
        if (getActivity() != null)
            ((MainActivity) getActivity()).onRefresh();
    }
}
