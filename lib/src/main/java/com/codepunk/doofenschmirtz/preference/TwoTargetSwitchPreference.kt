/*
 * Copyright (C) 2018 Codepunk, LLC
 * Author(s): Scott Slater
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

package com.codepunk.doofenschmirtz.preference

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import androidx.preference.PreferenceViewHolder
import androidx.preference.SwitchPreferenceCompat
import com.codepunk.doofenschmirtz.R

/**
 * A class that offers a two-target implementation of [SwitchPreferenceCompat]. Clicking on the
 * switch itself will toggle the underlying Boolean value, while clicking the preference itself
 * will generally lead to a sub-setting fragment or intent.
 */
class TwoTargetSwitchPreference @JvmOverloads constructor(
    context: Context? = null,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.twoTargetSwitchPreferenceStyle,
    defStyleRes: Int = R.style.TwoTargetSwitchPreference
) :
    SwitchPreferenceCompat(context, attrs, defStyleAttr, defStyleRes) {

    // region Inherited methods

    /**
     * Binds the created View to the data for this Preference. Additionally, sets an
     * [OnClickListener] to catch when the user clicks the secondary target.
     *
     */
    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        val secondaryVisibility = if (widgetLayoutResource == 0) View.GONE else View.VISIBLE
        holder?.findViewById(R.id.android_two_target_divider)?.visibility = secondaryVisibility
        holder?.findViewById(android.R.id.widget_frame)?.apply {
            visibility = secondaryVisibility
            setOnClickListener {
                // Call default onClick functionality here rather than from the primary target
                onSecondaryTargetClick()
            }
        }
    }

    /**
     * Suppresses normal onClick processing for this preference, as the logic to toggle to
     * underlying value will now be handled in the [onSecondaryTargetClick] method.
     */
    override fun onClick() {
        // Suppress onClick processing when clicking the primary target. Instead we will handle
        // it when the user clicks the secondary target. Note that this logic is contingent upon
        // there being no crucial logic in Preference.onClick.
    }

    // endregion Inherited methods

    // region Methods

    /**
     * Triggered when the user clicks the secondary target. Toggles the underlying value for this
     * preference.
     */
    fun onSecondaryTargetClick() {
        super.onClick()
    }

    // endregion Methods
}
