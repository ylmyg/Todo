/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.doist.datetimepicker.date;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CalendarView;
import android.widget.NumberPicker;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import io.doist.datetimepicker.HapticFeedbackFrameLayout;
import io.doist.datetimepicker.R;
import io.doist.datetimepicker.fragment.DatePickerDialogFragment;

/**
 * Provides a widget for selecting a date.
 * <p>
 * The month and day can be selected using a
 * calendar-style view while the year can be selected separately using a list.
 * </p>
 * <p>
 * See the <a href="{@docRoot}guide/topics/ui/controls/pickers.html">Pickers</a>
 * guide.
 * </p>
 * <p>
 * For a dialog using this view, see {@link DatePickerDialogFragment}.
 * </p>
 *
 * @attr ref R.styleable#DatePicker_startYear
 * @attr ref R.styleable#DatePicker_endYear
 * @attr ref R.styleable#DatePicker_maxDate
 * @attr ref R.styleable#DatePicker_minDate
 * @attr ref R.styleable#DatePicker_dayOfWeekBackground
 * @attr ref R.styleable#DatePicker_dayOfWeekTextAppearance
 * @attr ref R.styleable#DatePicker_headerBackground
 * @attr ref R.styleable#DatePicker_headerMonthTextAppearance
 * @attr ref R.styleable#DatePicker_headerDayOfMonthTextAppearance
 * @attr ref R.styleable#DatePicker_headerYearTextAppearance
 * @attr ref R.styleable#DatePicker_yearListItemTextAppearance
 * @attr ref R.styleable#DatePicker_yearListSelectorColor
 * @attr ref R.styleable#DatePicker_calendarTextColor
 */
public class DatePicker extends HapticFeedbackFrameLayout {
    private final DatePickerDelegate mDelegate;

    /**
     * The callback used to indicate the user changes\d the date.
     */
    public interface OnDateChangedListener {

        /**
         * Called upon a date change.
         *
         * @param view        The view associated with this listener.
         * @param year        The year that was set.
         * @param monthOfYear The month that was set (0-11) for compatibility
         *                    with {@link java.util.Calendar}.
         * @param dayOfMonth  The day of the month that was set.
         */
        void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth);
    }

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.datePickerStyle);
    }

    public DatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DatePicker, defStyleAttr, 0);
        final int firstDayOfWeek = a.getInt(R.styleable.DatePicker_firstDayOfWeek, 0);
        a.recycle();

        mDelegate = onCreateUIDelegate(context, attrs, defStyleAttr);

        if (firstDayOfWeek != 0) {
            setFirstDayOfWeek(firstDayOfWeek);
        }
    }

    protected DatePickerDelegate onCreateUIDelegate(Context context, AttributeSet attrs, int defStyleAttr) {
        return new DatePickerCalendarDelegate(this, context, attrs, defStyleAttr);
    }

    /**
     * Initialize the state. If the provided values designate an inconsistent
     * date the values are normalized before updating the spinners.
     *
     * @param year                  The initial year.
     * @param monthOfYear           The initial month <strong>starting from zero</strong>.
     * @param dayOfMonth            The initial day of the month.
     * @param onDateChangedListener How user is notified date is changed by
     *                              user, can be null.
     */
    public void init(int year, int monthOfYear, int dayOfMonth, OnDateChangedListener onDateChangedListener) {
        mDelegate.init(year, monthOfYear, dayOfMonth, onDateChangedListener);
    }

    public void setOnDateChangedListener(OnDateChangedListener listener) {
        mDelegate.setOnDateChangedListener(listener);
    }

    /**
     * Update the current date.
     *
     * @param year       The year.
     * @param month      The month which is <strong>starting from zero</strong>.
     * @param dayOfMonth The day of the month.
     */
    public void updateDate(int year, int month, int dayOfMonth) {
        mDelegate.updateDate(year, month, dayOfMonth);
    }

    /**
     * @return The selected year.
     */
    public int getYear() {
        return mDelegate.getYear();
    }

    /**
     * @return The selected month.
     */
    public int getMonth() {
        return mDelegate.getMonth();
    }

    /**
     * @return The selected day of month.
     */
    public int getDayOfMonth() {
        return mDelegate.getDayOfMonth();
    }

    /**
     * Gets the minimal date supported by this {@link DatePicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     * <p>
     * Note: The default minimal date is 01/01/1900.
     * <p>
     *
     * @return The minimal supported date.
     */
    public long getMinDate() {
        return mDelegate.getMinDate().getTimeInMillis();
    }

    /**
     * Sets the minimal date supported by this {@link NumberPicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     *
     * @param minDate The minimal supported date.
     */
    public void setMinDate(long minDate) {
        mDelegate.setMinDate(minDate);
    }

    /**
     * Gets the maximal date supported by this {@link DatePicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     * <p>
     * Note: The default maximal date is 12/31/2100.
     * <p>
     *
     * @return The maximal supported date.
     */
    public long getMaxDate() {
        return mDelegate.getMaxDate().getTimeInMillis();
    }

    /**
     * Sets the maximal date supported by this {@link DatePicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     *
     * @param maxDate The maximal supported date.
     */
    public void setMaxDate(long maxDate) {
        mDelegate.setMaxDate(maxDate);
    }

    /**
     * Sets the callback that indicates the current date is valid.
     *
     * @param callback the callback, may be null
     */
    public void setValidationCallback(@Nullable ValidationCallback callback) {
        mDelegate.setValidationCallback(callback);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (mDelegate.isEnabled() == enabled) {
            return;
        }
        super.setEnabled(enabled);
        mDelegate.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return mDelegate.isEnabled();
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return mDelegate.dispatchPopulateAccessibilityEvent(event);
    }

    @Override
    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
        super.onPopulateAccessibilityEvent(event);
        mDelegate.onPopulateAccessibilityEvent(event);
    }

    @Override
    public void onInitializeAccessibilityEvent(@NonNull AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        mDelegate.onInitializeAccessibilityEvent(event);
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(@NonNull AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        mDelegate.onInitializeAccessibilityNodeInfo(info);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDelegate.onConfigurationChanged(newConfig);
    }

    /**
     * Sets the first day of week.
     *
     * @param firstDayOfWeek The first day of the week conforming to the
     *                       {@link CalendarView} APIs.
     * @attr ref R.styleable#DatePicker_firstDayOfWeek
     * @see Calendar#SUNDAY
     * @see Calendar#MONDAY
     * @see Calendar#TUESDAY
     * @see Calendar#WEDNESDAY
     * @see Calendar#THURSDAY
     * @see Calendar#FRIDAY
     * @see Calendar#SATURDAY
     */
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        if (firstDayOfWeek < Calendar.SUNDAY || firstDayOfWeek > Calendar.SATURDAY) {
            throw new IllegalArgumentException("firstDayOfWeek must be between 1 and 7");
        }
        mDelegate.setFirstDayOfWeek(firstDayOfWeek);
    }

    /**
     * Gets the first day of week.
     *
     * @return The first day of the week conforming to the {@link CalendarView}
     * APIs.
     * @attr ref R.styleable#DatePicker_firstDayOfWeek
     * @see Calendar#SUNDAY
     * @see Calendar#MONDAY
     * @see Calendar#TUESDAY
     * @see Calendar#WEDNESDAY
     * @see Calendar#THURSDAY
     * @see Calendar#FRIDAY
     * @see Calendar#SATURDAY
     */
    public int getFirstDayOfWeek() {
        return mDelegate.getFirstDayOfWeek();
    }

    @Override
    protected void dispatchRestoreInstanceState(@NonNull SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return mDelegate.onSaveInstanceState(superState);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        BaseSavedState ss = (BaseSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mDelegate.onRestoreInstanceState(ss);
    }

    /**
     * A delegate interface that defined the public API of the DatePicker. Allows different
     * DatePicker implementations. This would need to be implemented by the DatePicker delegates
     * for the real behavior.
     */
    interface DatePickerDelegate {
        void init(int year, int monthOfYear, int dayOfMonth, OnDateChangedListener onDateChangedListener);

        void updateDate(int year, int month, int dayOfMonth);

        int getYear();

        int getMonth();

        int getDayOfMonth();

        void setFirstDayOfWeek(int firstDayOfWeek);

        int getFirstDayOfWeek();

        void setMinDate(long minDate);

        Calendar getMinDate();

        void setMaxDate(long maxDate);

        Calendar getMaxDate();

        void setEnabled(boolean enabled);

        boolean isEnabled();

        void setOnDateChangedListener(OnDateChangedListener listener);

        void setValidationCallback(ValidationCallback callback);

        void onConfigurationChanged(Configuration newConfig);

        Parcelable onSaveInstanceState(Parcelable superState);

        void onRestoreInstanceState(Parcelable state);

        boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event);

        void onPopulateAccessibilityEvent(AccessibilityEvent event);

        void onInitializeAccessibilityEvent(AccessibilityEvent event);

        void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info);
    }

    /**
     * An abstract class which can be used as a start for DatePicker implementations
     */
    abstract static class AbstractDatePickerDelegate implements DatePickerDelegate {
        // The delegator
        protected DatePicker mDelegator;

        // The context
        protected Context mContext;

        // The current locale
        protected Locale mCurrentLocale;

        // Callbacks
        protected ValidationCallback mValidationCallback;

        public AbstractDatePickerDelegate(DatePicker delegator, Context context) {
            mDelegator = delegator;
            mContext = context;

            // initialization based on locale
            setCurrentLocale(Locale.getDefault());
        }

        protected void setCurrentLocale(Locale locale) {
            if (locale.equals(mCurrentLocale)) {
                return;
            }
            mCurrentLocale = locale;
        }

        @Override
        public void setValidationCallback(ValidationCallback callback) {
            mValidationCallback = callback;
        }

        protected void onValidationChanged(boolean valid) {
            if (mValidationCallback != null) {
                mValidationCallback.onValidationChanged(valid);
            }
        }
    }

    /**
     * A callback interface for updating input validity when the date picker
     * when included into a dialog.
     */
    public interface ValidationCallback {
        void onValidationChanged(boolean valid);
    }
}
