/*
 * Copyright 2018 Codepunk, LLC
 * Author(s): Scott Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codepunk.doofenschmirtz.util

import kotlin.reflect.KClass

/**
 * A function that creates a key using a fully-qualified [Class] name coupled with the supplied
 * [name].
 */
fun Class<*>.makeKey(name: String): String = "${this.name}.$name"

/**
 * A function that creates a key using a fully-qualified [KClass] name coupled with the supplied
 * [name].
 */
fun KClass<*>.makeKey(name: String): String = "${this.java.name}.$name"

/**
 * Extension function on [Class] that returns the top-level enclosing class.
 */
val Class<*>.topLevelClass: Class<*>
    get() {
        var topLevelClass = this
        while (topLevelClass.enclosingClass != null) {
            topLevelClass.enclosingClass?.apply {
                topLevelClass = this
            }
        }
        return topLevelClass
    }
