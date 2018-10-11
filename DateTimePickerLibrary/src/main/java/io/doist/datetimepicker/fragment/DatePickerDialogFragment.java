/*
 * Copyright (C) 2013 The Android Open Source Project
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

package io.doist.datetimepicker.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;

import io.doist.datetimepicker.date.DatePicker;
import io.doist.datetimepicker.date.OnDateSetListener;

/**
 * Dialog allowing users to select a date.
 */
public class DatePickerDialogFragment extends DialogFragment {
    public static final String TAG = DatePickerDialogFragment.class.getName();

    private DatePickerDialogFragmentDelegate mDelegate;

    public DatePickerDialogFragment() {
        mDelegate = onCreateDatePickerDialogFragmentDelegate();
    }

    protected DatePickerDialogFragmentDelegate onCreateDatePickerDialogFragmentDelegate() {
        return new DatePickerDialogFragmentDelegate();
    }

    public static DatePickerDialogFragment newInstance(OnDateSetListener listener, int year, int monthOfYear,
                                                       int dayOfMonth) {
        DatePickerDialogFragment fragment = new DatePickerDialogFragment();
        fragment.setArguments(DatePickerDialogFragmentDelegate.createArguments(year, monthOfYear, dayOfMonth));
        fragment.setOnDateSetListener(listener);
        return fragment;
    }

    @SuppressWarnings("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mDelegate.onCreateDialog(getActivity(), savedInstanceState, getArguments());
    }

    public void setOnDateSetListener(OnDateSetListener listener) {
        mDelegate.setOnDateSetListener(listener);
    }

    /**
     * Gets the {@link DatePicker} contained in this dialog.
     *
     * @return The calendar view.
     */
    public DatePicker getDatePicker() {
        return mDelegate.getDatePicker();
    }

    /**
     * Sets the current date.
     *
     * @param year        The date year.
     * @param monthOfYear The date month.
     * @param dayOfMonth  The date day of month.
     */
    public void updateDate(int year, int monthOfYear, int dayOfMonth) {
        mDelegate.updateDate(year, monthOfYear, dayOfMonth);
    }
}
