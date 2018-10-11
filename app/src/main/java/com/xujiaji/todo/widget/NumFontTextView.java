package com.xujiaji.todo.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class NumFontTextView extends android.support.v7.widget.AppCompatTextView {

    public NumFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public NumFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NumFontTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "number.ttf");
        setTypeface(tf, 1);

    }
}