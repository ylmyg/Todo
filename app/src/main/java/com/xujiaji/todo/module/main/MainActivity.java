package com.xujiaji.todo.module.main;

import android.view.View;

import com.xujiaji.todo.R;
import com.xujiaji.todo.base.App;
import com.xujiaji.todo.base.BaseActivity;
import com.xujiaji.todo.module.login.LoginDialogActivity;

public class MainActivity extends BaseActivity {

    @Override
    public int layoutId() {
        return R.layout.activity_main;
    }


    public void onClickHomeFab(View view) {
        if (App.Login.isOK()) {

        } else {
            LoginDialogActivity.launch(this);
        }
    }
}
