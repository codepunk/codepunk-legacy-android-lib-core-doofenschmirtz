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
import android.support.v7.preference.PreferenceDialogFragmentCompat
import android.support.v7.preference.PreferenceFragmentCompat
import com.codepunk.codepunklib.android.preference.YesNoPreference

class DialogFragmentCompatFactory private constructor() {

    companion object: PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback {
        val DIALOG_FRAGMENT_TAG = DialogFragmentCompatFactory::class.java.simpleName + ".DIALOG"

        fun create(preference: Preference?): PreferenceDialogFragmentCompat? {
            return when (preference) {
                is YesNoPreference -> {
                    YesNoPreferenceDialogFragment.newInstance(preference.key)
                }
                else -> null
            }
        }

        override fun onPreferenceDisplayDialog(
                caller: PreferenceFragmentCompat,
                pref: Preference?): Boolean {
            if (caller.requireFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
                return true
            }

            return create(pref)?.run {
                    setTargetFragment(caller, 0)
                    show(caller.requireFragmentManager(), DIALOG_FRAGMENT_TAG)
                    true
                } ?: false
        }
    }
}