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

package com.codepunk.doofenschmirtz.util

import android.support.v7.preference.ListPreference

/**
 * Extension method for [ListPreference] that automatically populates the list with values
 * from an [enumClass]. By default this will use [Enum.name] for both
 * [ListPreference.setEntryValues] and [ListPreference.setEntries], but this behavior can be
 * overridden by supplying different logic for [entryValue] and [entry], respectively.
 */
fun <E : Enum<E>> ListPreference.populate(
        enumClass: Class<E>,
        entryValue: (enum: E) -> CharSequence? = { it.name },
        entry: (enum: E) -> CharSequence? = { it.name }) {
    val entryValueList: ArrayList<CharSequence> = ArrayList(enumClass.enumConstants.size)
    val entryList: ArrayList<CharSequence> = ArrayList(enumClass.enumConstants.size)
    for (constant in enumClass.enumConstants) {
        entryValueList.add(entryValue(constant) ?: constant.name)
        entryList.add(entry(constant) ?: constant.name)
    }
    entryValues = entryValueList.toTypedArray()
    entries = entryList.toTypedArray()
}