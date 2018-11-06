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

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceDialogFragmentCompat
import com.codepunk.doofenschmirtz.borrowed.android.preference.YesNoPreference

/**
 * The [PreferenceDialogFragmentCompat] associated with the [YesNoPreference] class. Displays a
 * simple "Yes/No" dialog.
 */
class YesNoPreferenceDialogFragment : PreferenceDialogFragmentCompat() {

    // region Inherited methods

    /**
     * Updates the Boolean value in the [Preference] associated with this dialog fragment.
     */
    override fun onDialogClosed(positiveResult: Boolean) {
        (preference as? YesNoPreference)?.apply {
            if (callChangeListener(positiveResult)) {
                value = positiveResult
            }
        }
    }

    // endregion Inherited methods

    // region Companion object

    companion object {

        // region Methods

        /**
         * Creates a new [YesNoPreferenceDialogFragment], passing [key] as an argument.
         */
        fun newInstance(key: String): YesNoPreferenceDialogFragment {
            return YesNoPreferenceDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_KEY, key)
                }
            }
        }

        // endregion Methods
    }

    // endregion Companion object
}
