/*
 * Copyright (C) 2017 The Android Open Source Project
 * Modifications copyright (C) 2018 Codepunk, LLC
 *               Author(s): Scott Slater
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
 *
 *
 * The original work can be found at The Android Open Source Project at
 *
 *      https://github.com/aosp-mirror
 *
 * In the following location:
 *
 *      platform_frameworks_base/packages/SettingsLib/src/com/android/settingslib/TwoTargetPreference.java
 *
 * Modifications:
 * July 2018: Updated to use local copies of internal Android resources
 *            Updated onBindViewHolder to capture clicks
 */

package com.codepunk.doofenschmirtz.borrowed.android.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.codepunk.doofenschmirtz.R;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

/**
 * A [Preference] class that has two separate targets, for example, a [Preference] that contains
 * a switch widget but which is itself also clickable.
 */
@SuppressWarnings("unused")
public class TwoTargetPreference extends Preference {

    // region Constructors

    @SuppressWarnings("unused")
    public TwoTargetPreference(Context context, AttributeSet attrs,
            int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @SuppressWarnings("unused")
    public TwoTargetPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings("unused")
    public TwoTargetPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @SuppressWarnings("unused")
    public TwoTargetPreference(Context context) {
        super(context);
        init();
    }

    // endregion Constructors

    // region Inherited methods

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        final View divider = holder.findViewById(R.id.android_two_target_divider);
        final View widgetFrame = holder.findViewById(android.R.id.widget_frame);
        final boolean shouldHideSecondTarget = shouldHideSecondTarget();
        if (divider != null) {
            divider.setVisibility(shouldHideSecondTarget ? View.GONE : View.VISIBLE);
        }
        if (widgetFrame != null) {
            widgetFrame.setVisibility(shouldHideSecondTarget ? View.GONE : View.VISIBLE);
        }
    }

    // endregion Inherited methods

    // region Methods

    /**
     * Initializes the preference.
     */
    private void init() {
        setLayoutResource(R.layout.android_preference_two_target);
        final int secondTargetResId = getSecondTargetResId();
        if (secondTargetResId != 0) {
            setWidgetLayoutResource(secondTargetResId);
        }
    }

    /**
     * Returns whether the second target should be hidden.
     * @return Whether the second target should be hidden.
     */
    @SuppressWarnings("WeakerAccess")
    protected boolean shouldHideSecondTarget() {
        return getSecondTargetResId() == 0;
    }

    /**
     * Returns the resource id of the second target.
     * @return The resource id of the second target.
     */
    @SuppressWarnings("WeakerAccess")
    protected int getSecondTargetResId() {
        return 0;
    }

    // endregion Methods

}
