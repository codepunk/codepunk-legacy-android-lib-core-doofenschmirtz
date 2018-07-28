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

import android.app.Activity
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceDialogFragmentCompat
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback

/**
 * A copy of Android's private internal DIALOG_FRAGMENT_TAG constant in [PreferenceFragmentCompat].
 * As with Android's version, its value is hardcoded to
 * "android.support.v7.preference.PreferenceFragment.DIALOG".
 */
const val DIALOG_FRAGMENT_TAG = "android.support.v7.preference.PreferenceFragment.DIALOG"

/**
 * Extension function that allows for a reusable, configurable way to display custom
 * [PreferenceDialogFragmentCompat]s from a [PreferenceFragmentCompat].
 *
 * The typical approach to this involves either overriding the
 * [PreferenceFragmentCompat.onDisplayPreferenceDialog] function in every instance of
 * [PreferenceFragmentCompat], or to implement
 * [OnPreferenceDisplayDialogCallback] in the activity containing your
 * preference fragment.
 *
 * Regardless of your approach, you can use this function to simplify the process. If the only
 * custom [PreferenceDialogFragmentCompat]s are a part of Codepunk's Doofenschmirtz library, you
 * can pass just the [pref] argument as follows:
 *
 * From [PreferenceFragmentCompat.onDisplayPreferenceDialog]:
 *
 *     override fun onDisplayPreferenceDialog(preference: Preference?) {
 *         if (!displayCustomPreferenceDialogFragment(preference)) {
 *             super.onDisplayPreferenceDialog(preference)
 *         }
 *     }
 *
 * From your settings [Activity]&mdash;or any other class&mdash;that has been defined to implement
 * [OnPreferenceDisplayDialogCallback]:
 *
 *     override fun onPreferenceDisplayDialog(
 *             caller: PreferenceFragmentCompat,
 *             pref: Preference?): Boolean {
 *         return caller.displayCustomPreferenceDialogFragment(pref)
 *     }
 *
 * By default, this function will tag dialog fragments with [DIALOG_FRAGMENT_TAG] (which is a copy
 * of Android's internal DIALOG_FRAGMENT_TAG found in [PreferenceFragmentCompat]). You can override
 * this behavior by supplying your own [dialogFragmentTag] to this function.
 *
 * If you have your own custom [PreferenceDialogFragmentCompat]s, you can incorporate them into
 * this function by creating a class that extends [PreferenceDialogFragmentFactory]. This class
 * has a single function, [PreferenceDialogFragmentFactory.create], that creates and returns an
 * instance of [PreferenceDialogFragmentCompat] based on the supplied [Preference].
 *
 * In fact, this method allows you to pass zero or more [PreferenceDialogFragmentFactory] instances
 * via the vararg [factories] parameter:
 *
 *     private val myFactory1 by lazy {
 *         MyFactory1()
 *     }
 *
 *     private val myFactory2 by lazy {
 *         MyFactory2()
 *     }
 *
 *     override fun onPreferenceDisplayDialog(
 *             caller: PreferenceFragmentCompat,
 *             pref: Preference?): Boolean {
 *         return caller.displayCustomPreferenceDialogFragment(
 *                 pref,
 *                 DIALOG_FRAGMENT_TAG,
 *                 myFactory1, myFactory2)
 *     }
 *
 * Or, alternatively:
 *
 *     override fun onPreferenceDisplayDialog(
 *             caller: PreferenceFragmentCompat,
 *             pref: Preference?): Boolean {
 *         return caller.displayCustomPreferenceDialogFragment(
 *                 pref = pref,
 *                 factories = *arrayOf(myFactory1, myFactory2))
 *     }
 */
fun PreferenceFragmentCompat.displayCustomPreferenceDialogFragment(
        pref: Preference?,
        dialogFragmentTag: String = DIALOG_FRAGMENT_TAG,
        vararg factories: PreferenceDialogFragmentFactory): Boolean {
    // Check if dialog is already showing
    if (requireFragmentManager().findFragmentByTag(dialogFragmentTag) != null) {
        return true
    }

    for (factory in factories) {
        factory.create(pref)?.also { dialogFragment ->
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(requireFragmentManager(), dialogFragmentTag)
            return true
        }
    }

    // Always try codepunkFactory as a last resort
    PreferenceDialogFragmentFactory.codepunkFactory.create(pref)?.also { dialogFragment ->
        dialogFragment.setTargetFragment(this, 0)
        dialogFragment.show(requireFragmentManager(), dialogFragmentTag)
        return true
    }

    return false
}