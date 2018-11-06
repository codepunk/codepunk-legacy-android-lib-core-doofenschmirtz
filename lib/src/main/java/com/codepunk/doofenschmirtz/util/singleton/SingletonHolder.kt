/*
 * Copyright (C) 2017 Christophe Beyls aka "@BladeCoder"
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
 *
 *
 * The original work can be found at
 *
 *      https://medium.com/@BladeCoder/kotlin-singletons-with-argument-194ef06edd9e
 */

package com.codepunk.doofenschmirtz.util.singleton

/**
 * A class that implements singleton-with-argument pattern.
 */
@Suppress("UNUSED")
open class SingletonHolder<T, A>(creator: (A) -> T) {

    // region Properties

    /**
     * The creator method that creates the singleton instance.
     */
    private var creator: ((A) -> T)? = creator

    /**
     * The singleton instance of class [T].
     */
    @Volatile
    private var instance: T? = null

    // endregion Properties

    /**
     * Gets an instance of class [T] using argument [A].
     */
    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}
