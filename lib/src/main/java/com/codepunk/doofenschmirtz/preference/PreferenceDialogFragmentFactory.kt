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

import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceDialogFragmentCompat
import com.codepunk.doofenschmirtz.borrowed.android.preference.YesNoPreference

/**
 * A class that handles the creation of [PreferenceDialogFragmentCompat]s.
 */
abstract class PreferenceDialogFragmentFactory {

    // region Methods

    /**
     * Creates a new instance of [PreferenceDialogFragmentCompat] according to the supplied
     * [Preference].
     */
    abstract fun create(preference: Preference?): PreferenceDialogFragmentCompat?

    // endregion Methods

    // region Nested classes

    /**
     * [PreferenceDialogFragmentFactory] that creates instances of [PreferenceDialogFragmentCompat]
     * according to known [Preference] types in the Codepunk Doofenschmirtz library.
     */
    object CodepunkFactory : PreferenceDialogFragmentFactory() {

        // region Inherited methods

        override fun create(preference: Preference?): PreferenceDialogFragmentCompat? {
            return when (preference) {
                is YesNoPreference -> YesNoPreferenceDialogFragment.newInstance(preference.key)
                else -> null
            }
        }

        // endregion Inherited methods
    }

    // endregion Nested classes
}