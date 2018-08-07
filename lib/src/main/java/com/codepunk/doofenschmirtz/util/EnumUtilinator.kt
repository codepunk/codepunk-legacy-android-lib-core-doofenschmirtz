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

package com.codepunk.doofenschmirtz.util

/**
 * An inline function that returns whether an enum contains any values with the given [name].
 */
inline fun <reified T : Enum<T>> enumContains(name: String): Boolean {
    return enumValues<T>().any { it.name == name }
}

/**
 * An inline function that extends Kotlin's [enumValueOf] to return a [defaultValue] if an enum
 * with the given [name] is not found.
 */
inline fun <reified T : Enum<T>> enumValueOf(name: String?, defaultValue: T): T {
    return name?.let {
        try {
            enumValueOf<T>(it)
        } catch (e: Exception) {
            defaultValue
        }
    } ?: defaultValue
}
