/*
 * Copyright (C) 2017 The Android Open Source Project
 * Modifications copyright (C) 2018 Codepunk, LLC
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
 * In the following locations:
 * Project: platform_frameworks_base / file: TwoTargetPreference
 * Project: platform_packages_apps_settings / file: MasterSwitchPreference
 *
 * Modifications:
 * July 2018: Updated to use local copies of internal Android resources.
 *            Updated onClick to suppress click logic from first target.
 *            Updated onBindViewHolder to call click logic from second target.
 */

package com.codepunk.codepunklib.android.settingslib;

import android.content.Context;
import android.support.v7.preference.PreferenceViewHolder;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import com.codepunk.codepunklib.R;

/**
 * A version of [SwitchPreferenceCompat] with two click targets. Left target leads to a subsetting
 * fragment or intent. Right target is a switch toggle, controlling on/off for the entire page.
 */
@SuppressWarnings({ "unused", "WeakerAccess" })
public class TwoTargetSwitchPreference extends SwitchPreferenceCompat {

    public TwoTargetSwitchPreference(Context context, AttributeSet attrs,
            int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public TwoTargetSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TwoTargetSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TwoTargetSwitchPreference(Context context) {
        super(context);
        init();
    }

    private void init() {
        setLayoutResource(R.layout.android_preference_two_target);
        final int secondTargetResId = getSecondTargetResId();
        if (secondTargetResId != 0) {
            setWidgetLayoutResource(secondTargetResId);
        }
    }

    @Override
    public void onBindViewHolder(final PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        final View divider = holder.findViewById(R.id.android_two_target_divider);
        final View widgetFrame = holder.findViewById(android.R.id.widget_frame);
        final boolean shouldHideSecondTarget = shouldHideSecondTarget();
        if (divider != null) {
            divider.setVisibility(shouldHideSecondTarget ? View.GONE : View.VISIBLE);
        }
        if (widgetFrame != null) {
            widgetFrame.setVisibility(shouldHideSecondTarget ? View.GONE : View.VISIBLE);
            widgetFrame.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call super's onClick logic from second target
                    TwoTargetSwitchPreference.super.onClick();
                }
            });
        }
    }

    @Override
    protected void onClick() {
        // Suppress on click logic from first target
    }

    protected boolean shouldHideSecondTarget() {
        return getSecondTargetResId() == 0;
    }

    protected int getSecondTargetResId() {
        return getWidgetLayoutResource();
    }
}
