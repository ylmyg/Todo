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

package io.doist.datetimepicker;

import android.app.Service;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Settings;

/**
 * A simple utility class to handle haptic feedback.
 */
public class HapticFeedbackController {
    private static final int VIBRATE_DELAY_MS = 125;
    private static final int VIBRATE_LENGTH_MS = 5;

    private static boolean checkGlobalSetting(Context context) {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.HAPTIC_FEEDBACK_ENABLED, 0) == 1;
    }

    private final Context mContext;
    private final ContentObserver mContentObserver;

    private Vibrator mVibrator;
    private boolean mIsGloballyEnabled;
    private long mLastVibrate;

    public HapticFeedbackController(Context context) {
        mContext = context;
        mContentObserver = new ContentObserver(null) {
            @Override
            public void onChange(boolean selfChange) {
                mIsGloballyEnabled = checkGlobalSetting(mContext);
            }
        };
    }

    /**
     * Call to setup the controller.
     */
    public void start() {
        if (mVibrator == null) {
            mVibrator = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);

            if (mVibrator != null) {
                // Setup a listener for changes in haptic feedback settings
                mIsGloballyEnabled = checkGlobalSetting(mContext);
                Uri uri = Settings.System.getUriFor(Settings.System.HAPTIC_FEEDBACK_ENABLED);
                mContext.getContentResolver().registerContentObserver(uri, false, mContentObserver);
            }
        }
    }

    /**
     * Call this when you don't need the controller anymore.
     */
    public void stop() {
        if (mVibrator != null) {
            mVibrator = null;
            mContext.getContentResolver().unregisterContentObserver(mContentObserver);
        }
    }

    /**
     * Try to vibrate. To prevent this becoming a single continuous vibration, nothing will
     * happen if we have vibrated very recently.
     */
    public void tryVibrate() {
        if (mVibrator != null && mIsGloballyEnabled) {
            long now = SystemClock.uptimeMillis();
            // We want to try to vibrate each individual tick discretely.
            if (now - mLastVibrate >= VIBRATE_DELAY_MS) {
                mVibrator.vibrate(VIBRATE_LENGTH_MS);
                mLastVibrate = now;
            }
        }
    }
}
