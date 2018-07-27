/*
 * Copyright (C) 2018 Codepunk, LLC
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

package com.codepunk.codepunklib.preference

import android.content.Context
import android.support.v7.preference.PreferenceViewHolder
import android.support.v7.preference.SwitchPreferenceCompat
import android.util.AttributeSet
import android.view.View
import com.codepunk.codepunklib.R

class TwoTargetSwitchPreference @JvmOverloads constructor (
        context: Context? = null,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.twoTargetSwitchPreferenceStyle,
        defStyleRes: Int = R.style.TwoTargetSwitchPreference) :
        SwitchPreferenceCompat(context, attrs, defStyleAttr, defStyleRes) {

    // region Inherited methods

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        val secondaryVisibility = if (widgetLayoutResource == 0) View.GONE else View.VISIBLE
        holder?.findViewById(R.id.android_two_target_divider)?.visibility = secondaryVisibility
        holder?.findViewById(android.R.id.widget_frame)?.apply {
            visibility = secondaryVisibility
            setOnClickListener {
                // Call default onClick functionality here rather than from the primary target
                super@TwoTargetSwitchPreference.onClick()
            }
        }
    }

    override fun onClick() {
        // Suppress onClick processing when clicking the primary target. Instead we will handle
        // it when the user clicks the secondary target. Note that this logic is contingent upon
        // there being no crucial logic in Preference.onClick.
    }

    // endregion Inherited methods
}