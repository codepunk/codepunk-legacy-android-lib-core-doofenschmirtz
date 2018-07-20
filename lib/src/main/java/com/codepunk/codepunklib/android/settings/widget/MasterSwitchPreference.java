/*
 * Copyright (C) 2017 The Android Open Source Project
 * Modifications copyright (C) 2018 Codepunk, LLC/Scott Slater
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
 * The original work can be found at
 *
 *      https://android.googlesource.com/platform/packages/apps/Settings/+/master/src/com/android/settings/widget/MasterSwitchPreference.java
 *
 * Modifications:
 * July 2018: Updated to reference SwitchCompat.
 *            Updated to use local copies of internal Android resources.
 *            Removed setDisabledByAdmin method.
 *            Added onGetDefaultValue/onSetInitialValue.
 */

package com.codepunk.codepunklib.android.settings.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.preference.PreferenceViewHolder;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import com.codepunk.codepunklib.R;
import com.codepunk.codepunklib.android.settingslib.TwoTargetPreference;

/**
 * A custom preference that provides inline switch toggle. It has a mandatory field for title, and
 * optional fields for icon and sub-text.
 */
public class MasterSwitchPreference extends TwoTargetPreference {

    private SwitchCompat mSwitch;
    private boolean mChecked;
    private boolean mEnableSwitch = true;

    public MasterSwitchPreference(Context context, AttributeSet attrs,
                                  int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MasterSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MasterSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MasterSwitchPreference(Context context) {
        super(context);
    }

    @Override
    protected int getSecondTargetResId() {
        return R.layout.android_preference_widget_master_switch;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        final View widgetView = holder.findViewById(android.R.id.widget_frame);
        if (widgetView != null) {
            widgetView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSwitch != null && !mSwitch.isEnabled()) {
                        return;
                    }
                    setChecked(!mChecked);
                    if (!callChangeListener(mChecked)) {
                        setChecked(!mChecked);
                    } else {
                        persistBoolean(mChecked);
                    }
                }
            });
        }

        mSwitch = (SwitchCompat) holder.findViewById(R.id.android_switch_widget);
        if (mSwitch != null) {
            mSwitch.setContentDescription(getTitle());
            mSwitch.setChecked(mChecked);
            mSwitch.setEnabled(mEnableSwitch);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getBoolean(index, false);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setChecked(restorePersistedValue ? getPersistedBoolean(mChecked) : (Boolean) defaultValue);
    }

    public boolean isChecked() {
        return mSwitch != null && mSwitch.isEnabled() && mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
        if (mSwitch != null) {
            mSwitch.setChecked(checked);
        }
    }

    public void setSwitchEnabled(boolean enabled) {
        mEnableSwitch = enabled;
        if (mSwitch != null) {
            mSwitch.setEnabled(enabled);
        }
    }

    public SwitchCompat getSwitch() {
        return mSwitch;
    }
}