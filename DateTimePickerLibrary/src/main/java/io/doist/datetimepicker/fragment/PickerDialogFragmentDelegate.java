package io.doist.datetimepicker.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;

import io.doist.datetimepicker.R;

abstract class PickerDialogFragmentDelegate {
    private int mDefThemeAttr;

    protected View mView;
    protected AlertDialog mDialog;

    PickerDialogFragmentDelegate(int defThemeAttr) {
        mDefThemeAttr = defThemeAttr;
    }

    int resolveDialogTheme(Context context, int resId) {
        if (resId >= 0x01000000) { // Start of real resource ids.
            return resId;
        } else {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(mDefThemeAttr, outValue, true);
            if (outValue.resourceId >= 0x01000000) {
                return outValue.resourceId;
            } else {
                context.getTheme().resolveAttribute(R.attr.alertDialogTheme, outValue, true);
                return outValue.resourceId;
            }
        }
    }

    @SuppressWarnings("InflateParams")
    @NonNull
    public final Dialog onCreateDialog(Context context, Bundle savedInstanceState, Bundle arguments) {
        mView = onCreateDialogView(LayoutInflater.from(context), savedInstanceState, arguments);
        mDialog = onBindDialogBuilder(onCreateDialogBuilder(context, 0), mView).create();
        return mDialog;
    }

    protected abstract View onCreateDialogView(LayoutInflater inflater, Bundle savedInstanceState, Bundle arguments);

    /**
     * Allows sub-classes to easily customize AlertDialog. Like passing a custom {@code themeResId}.
     * <p>
     * Check {@link #onBindDialogBuilder(AlertDialog.Builder, View)}} to bind view, title, buttons, etc., without
     * changing the builder.
     */
    protected AlertDialog.Builder onCreateDialogBuilder(Context context, int themeResId) {
        return new AlertDialog.Builder(
                new ContextThemeWrapper(context, resolveDialogTheme(context, themeResId)),
                R.style.Theme_Window_NoMinWidth);
    }

    /**
     * Bind view, title, buttons, etc., to the created dialog builder.
     */
    protected AlertDialog.Builder onBindDialogBuilder(AlertDialog.Builder builder, View view) {
        builder.setView(view);
        return builder;
    }

    public View getView() {
        return mView;
    }

    public AlertDialog getDialog() {
        return mDialog;
    }
}
