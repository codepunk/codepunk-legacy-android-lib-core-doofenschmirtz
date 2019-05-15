/*
 * Copyright (C) 2019 Codepunk, LLC
 * Author(s): Scott Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codepunk.doofenschmirtz.util

import android.content.Context
import androidx.annotation.StringRes
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap

/**
 * A utility class that attempts to translate messages, errors and other strings returned from the
 * network.
 */
class Translatinator(

    /**
     * A [Context] that will be used to get resource strings.
     */
    private val context: Context,

    /**
     * A map of string-to-resource-string translations.
     */
    private val translations: Map<String, Int>,

    /**
     * A map of regEx [Pattern]-to-resource-string translations.
     */
    private val regExTranslations: Map<Pattern, Int>,

    /**
     * Whether this [Translatinator] should operate in debug mode.
     */
    private val debug: Boolean = false

) {

// region Methods

    /**
     * Attempts to translate a [string] based on maps of known strings and regex patterns. The
     * [translations] map is consulted first and if an exact match is found, returns the translated
     * string. Next, the [regExTranslations] is traversed to find a matching pattern. If one is
     * found, then the translated string with mapped arguments is returned. Note that an attempt is
     * also made to translate the arguments themselves before insertion into the translated
     * string. If no matches are found, the original string is returned unchanged.
     */
    fun translate(string: String?): String? = when {
        string == null -> null
        translations.containsKey(string) -> translations[string]?.let { resId ->
            format(context.getString(resId), true)
        }
        else -> {
            val formatArgs = ArrayList<String?>()
            regExTranslations.entries.find { entry ->
                val matcher = entry.key.matcher(string)
                val matches = matcher.matches()
                if (matches) {
                    val groupCount = matcher.groupCount()
                    for (group in 1..groupCount) {
                        formatArgs.add(translate(matcher.group(group)))
                    }
                }
                matches
            }?.let { found ->
                format(context.getString(found.value, *formatArgs.toArray()), true)
            } ?: format(string, false)
        }
    }

    /**
     * Utility method that optionally modifies any successfully-translated string. This allows
     * for debugging to see what strings were translated and what strings were not translated.
     */
    private fun format(string: String, translated: Boolean): String = when {
        !debug -> string
        translated -> "[[$string]]"
        else -> "<<$string>>"
    }

    // endregion Methods

    // region Nested/inner classes

    /**
     * A builder class used to build a [Translatinator].
     */
    class Builder(private val context: Context) {

        // region Properties

        /**
         * A [HashMap] of string-to-resource-string translations.
         */
        private val translations = HashMap<String, Int>()

        /**
         * A [HashMap] of regEx [Pattern]-to-resource-string translations.
         */
        private val regExTranslations = HashMap<Pattern, Int>()

        /**
         * Whether this [Translatinator] should operate in debug mode.
         */
        private var debug: Boolean = false

        // endregion Properties

        // region Methods

        /**
         * Builds a [Translatinator] instance using the translations mapped using this Builder's
         * [map] and/or [mapRegEx] methods.
         */
        fun build(): Translatinator {
            return Translatinator(
                context,
                Collections.unmodifiableMap(translations),
                Collections.unmodifiableMap(regExTranslations),
                debug
            )
        }

        /**
         * Specifies whether the [Translatinator] should operate in debug mode. When in debug mode,
         * successfully-translated strings appear in double brackets (i.e. like \[\[this\]\]
         * whereas unsuccessfully-translated strings appear in double carats (i.e. like <<this>>).
         */
        fun debug(debug: Boolean): Builder {
            this.debug = debug
            return this
        }

        /**
         * Maps a regular expression string with a corresponding string resource ID. This string
         * can have "groups" that are also independently translated at runtime. For example, a
         * regEx string of "The (\\w+) must be at least (\\d+) characters." can be mapped to a
         * string resource such as "The %1$s must be at least %2$s characters." and the various
         * groups of the regular expression will be filled in appropriately.
         */
        @Suppress("WEAKER_ACCESS")
        fun mapRegEx(inRegex: String, @StringRes outResId: Int): Builder {
            regExTranslations[Pattern.compile(inRegex)] = outResId
            return this
        }

        /**
         * Maps a regular expression string resource with a corresponding string resource ID. This
         * string can have "groups" that are also independently translated at runtime. For example,
         * a regEx string of "The (\\w+) must be at least (\\d+) characters." can be mapped to a
         * string resource such as "The %1$s must be at least %2$s characters." and the various
         * groups of the regular expression will be filled in appropriately.
         */
        fun mapRegEx(@StringRes inRegexResId: Int, @StringRes outResId: Int): Builder =
            mapRegEx(context.getString(inRegexResId), outResId)

        /**
         * Maps a string with a corresponding (translated) string resource ID.
         */
        @Suppress("WEAKER_ACCESS")
        fun map(inStr: String, @StringRes outResId: Int): Builder {
            translations[inStr] = outResId
            return this
        }

        /**
         * Maps a string resource ID with a corresponding (translated) string resource ID.
         */
        fun map(@StringRes inResId: Int, @StringRes outResId: Int): Builder =
            map(context.getString(inResId), outResId)

        // endregion Methods

    }

    // endregion Nested/inner classes

}
