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

import android.os.Bundle
import android.support.v7.preference.PreferenceDialogFragmentCompat
import com.codepunk.codepunklib.android.preference.YesNoPreference

class YesNoPreferenceDialogFragment: PreferenceDialogFragmentCompat() {

    // region Properties

    var value: Boolean = false

    // endregion Properties

    // region Lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        value = savedInstanceState?.getBoolean(SAVE_STATE_VALUE, false) ?: false
    }

    // endregion Lifecycle methods

    // region Inherited methods

    override fun onDialogClosed(positiveResult: Boolean) {
        (preference as? YesNoPreference)?.apply {
            if (callChangeListener(positiveResult)) {
                value = positiveResult
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SAVE_STATE_VALUE, value)
    }

    // endregion Inherited methods

    // region Companion objects

    companion object {
        private val SAVE_STATE_VALUE =
                PreferenceDialogFragmentCompat::class.java.simpleName + ".value"

        fun newInstance(key: String): YesNoPreferenceDialogFragment {
            return YesNoPreferenceDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_KEY, key)
                }
            }
        }
    }

    // endregion Companion objects
}
