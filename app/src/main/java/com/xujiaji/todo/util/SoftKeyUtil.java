package com.xujiaji.todo.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

/**
 * 软键盘工具类
 * Created by jiaji on 2017/8/1.
 */

public class SoftKeyUtil {
    /**
     * 软键盘打开或关闭开关
     *
     * @param context
     */
    public static void toggle(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘
     *
     * @param context
     */
    public static void hide(Activity context) {
        View curFoc = context.getCurrentFocus();
        if (curFoc != null) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null) return;
            imm.hideSoftInputFromWindow(curFoc.getWindowToken(), 0);
        }
    }

    public static void show(Activity context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        //显示软键盘
        if (context.getCurrentFocus() != null) {
            //有焦点打开
            imm.showSoftInput(context.getCurrentFocus(), 0);
        } else {
            //无焦点打开
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public static void hide(Activity context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;

        if (context.getCurrentFocus() != null) {
            //有焦点关闭
            imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            //无焦点关闭
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }

    }


    /**
     * 软键盘是否显示
     * @param context
     * @return
     */
    public static boolean isActive(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm != null && imm.isActive();
    }

    /**
     * 软键盘是否显示
     * @param context
     * @return
     */
    public static boolean isActive(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm != null && imm.isActive(view);
    }
//
//    /**
//     * 当键盘出现后布局向上偏移
//     *
//     * @param rootView
//     * @param targetView
//     * @param offset
//     */
//    public static void moveTopLayout(final RelativeLayout rootView, final LinearLayout targetView, final int offset) {
//        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) targetView.getLayoutParams();
//        final int initBottom = params.topMargin;
//        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            int lastValue = 0;
//
//            @Override
//            public void onGlobalLayout() {
//                Rect r = new Rect();
//                rootView.getWindowVisibleDisplayFrame(r);//获取可视区域
//                int mLayoutDesignHeight = rootView.getHeight();//布局高度
//                int keyboardHeight = mLayoutDesignHeight - r.bottom;//键盘高度
//                if (lastValue == keyboardHeight) {
//                    return;
//                }
//                lastValue = keyboardHeight;
//                boolean visible = keyboardHeight > mLayoutDesignHeight / 3;
//                if (visible) {
//                    params.topMargin = initBottom - (keyboardHeight - offset);
//
//                } else {
//                    params.topMargin = initBottom;
//                }
//                targetView.setLayoutParams(params);
//            }
//        });
//    }


    public static ViewTreeObserver.OnGlobalLayoutListener doMoveLayout(final View view, final View targetView, final int offset) {
        return doMoveLayout(view, targetView, null, 0);
    }

    public static ViewTreeObserver.OnGlobalLayoutListener doMoveLayout(final View view, final View targetView, final View[] needHideViews, final int offset) {
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) targetView.getLayoutParams();
        final int initBottom = params.bottomMargin;
        return doMonitorSoftKeyWord(view, new OnSoftKeyWordShowListener() {
            boolean showed;
            boolean hided = true;
            @Override
            public void hasShow(boolean isShow, int height) {
                if (isShow && hided) {// 键盘显示
                    showed = true;
                    hided = false;
                    params.bottomMargin = initBottom + (height + offset);
                    if (needHideViews != null && needHideViews.length > 0) {
                        for (View v : needHideViews) {
                            v.setVisibility(View.GONE);
                        }
                    }
                } else if (showed && !isShow) { // 键盘隐藏
                    showed = false;
                    hided = true;
                    params.bottomMargin = initBottom;
                } else {
                    return;
                }
                targetView.setLayoutParams(params);
            }
        });
    }

    public interface OnSoftKeyWordShowListener {
        void hasShow(boolean isShow, int height);
    }

    /**
     * 判断软键盘是否弹出
     * * @param rootView
     *
     * @param listener 备注：在不用的时候记得移除OnGlobalLayoutListener
     */
    public static ViewTreeObserver.OnGlobalLayoutListener doMonitorSoftKeyWord(final View rootView, final OnSoftKeyWordShowListener listener) {
        final ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final Rect rect = new Rect();
                rootView.getWindowVisibleDisplayFrame(rect);
                final int screenHeight = rootView.getRootView().getHeight();
                final int heightDifference = screenHeight - rect.bottom;
                boolean visible = heightDifference > screenHeight / 3;
                if (listener != null)
                    listener.hasShow(visible, heightDifference);
            }
        };
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
        return layoutListener;
    }


    public static void destroyListener(View rootView, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && listener != null) {
            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }
}
