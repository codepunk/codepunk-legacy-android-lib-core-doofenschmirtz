package com.codepunk.codepunklib.preference

import android.support.v7.preference.Preference
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

import android.support.v7.preference.PreferenceFragmentCompat
import com.codepunk.codepunklib.preference.PreferenceDialogFragmentFactory.codepunkFactory

const val DIALOG_FRAGMENT_TAG = "android.support.v7.preference.PreferenceFragment.DIALOG"

fun PreferenceFragmentCompat.displayCustomPreferenceDialogFragment(
        pref: Preference?,
        dialogFragmentTag: String = DIALOG_FRAGMENT_TAG,
        vararg factories: PreferenceDialogFragmentFactory = emptyArray()): Boolean {
    // check if dialog is already showing
    if (requireFragmentManager().findFragmentByTag(dialogFragmentTag) != null) {
        return true
    }

    for (factory in arrayOf(*factories, codepunkFactory)) {
        factory.create(pref)?.also {
            it.setTargetFragment(this, 0)
            it.show(requireFragmentManager(), dialogFragmentTag)
            return true
        }
    }

    return false
}