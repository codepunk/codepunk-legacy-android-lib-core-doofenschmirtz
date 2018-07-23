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

import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat

/**
 * An extension of [PreferenceFragmentCompat] that checks to see if a given preference
 * implements [PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback] and if so, invokes that
 * callback first in order to allow the Preference to display its own dialog.
 *
 * @author Scott Slater
 */
abstract class DialogDelegatePreferenceFragment: PreferenceFragmentCompat() {

    // region Nested classes

    companion object {
        val DIALOG_FRAGMENT_TAG = DialogDelegatePreferenceFragment::class.java.name + ".DIALOG"
    }

    // endregion Nested classes

    // region Inherited methods

    override fun onDisplayPreferenceDialog(preference: Preference?) {
        if (preference is OnPreferenceDisplayDialogCallback &&
                preference.onPreferenceDisplayDialog(this, preference)) {
            return
        }

        super.onDisplayPreferenceDialog(preference)
    }

    // endregion Inherited methods
}