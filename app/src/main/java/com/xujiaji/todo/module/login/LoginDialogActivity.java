package com.xujiaji.todo.module.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.xujiaji.todo.R;
import com.xujiaji.todo.base.BaseActivity;
import com.xujiaji.todo.helper.InputHelper;
import com.xujiaji.todo.helper.ToastHelper;

/**
 * author: xujiaji
 * created on: 2018/10/9 16:43
 * description:
 */
public class LoginDialogActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    private Button mBtnSwitch;
    private Button mBtnConfirm;
    private EditText mEtAccount;
    private EditText mEtPassword;
    private View mLayoutContainer;
    private ProgressBar mProgressBar;

    @Override
    public void onBeforeCreateCircle() {
        super.onBeforeCreateCircle();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setFinishOnTouchOutside(true);
    }

    @Override
    public int layoutId() {
        return R.layout.dialog_activity_login;
    }

    public static void launch(Context context) {
        context.startActivity(new Intent(context, LoginDialogActivity.class));
    }

    @Override
    public void onInitCircle() {
        super.onInitCircle();
        mBtnSwitch = findViewById(R.id.btnSwitch);
        mBtnConfirm = findViewById(R.id.btnConfirm);
        mProgressBar = findViewById(R.id.progressBar);
        mEtAccount = findViewById(R.id.account);
        mEtPassword = findViewById(R.id.password);
        mLayoutContainer = findViewById(R.id.layoutContainer);
        mBtnSwitch.setPaintFlags(mBtnSwitch.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void onListenerCircle() {
        super.onListenerCircle();
        mBtnSwitch.setOnClickListener(v -> {
            if (isLoginPage()) {
                switchToRegister();
            } else {
                switchToLogin();
            }
        });

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginPage()) {
                    presenter.requestLogin(InputHelper.toString(mEtAccount), InputHelper.toString(mEtPassword));
                } else {
                    presenter.requestRegister(InputHelper.toString(mEtAccount), InputHelper.toString(mEtPassword));
                }
            }
        });
    }

    @Override
    public void displayProgress() {
        mLayoutContainer.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissProgress() {
        mProgressBar.setVisibility(View.GONE);
        mLayoutContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void switchToLogin() {
        mBtnSwitch.setText(R.string.register);
        mBtnConfirm.setText(R.string.login);
    }

    @Override
    public void switchToRegister() {
        mBtnSwitch.setText(R.string.login);
        mBtnConfirm.setText(R.string.register);
    }

    @Override
    public void loginSuccess() {
        ToastHelper.success(getString(R.string.success_login));
        finish();
    }

    @Override
    public boolean isLoginPage() {
        return InputHelper.toString(mBtnSwitch).equals(getString(R.string.register));
    }
}
