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

package com.codepunk.doofenschmirtz.util.loginator

/**
 * Proxying implementation of [Loginator] that simply delegates all of its calls to another
 * Loginator. Can be subclassed to modify behavior without changing the original Loginator.
 */
open class LoginatorWrapper(

    /**
     * The [Loginator] being wrapped by this LoginatorWrapper.
     */
    protected val wrappedLoginator: Loginator

) : Loginator {

    // region Properties

    override var level: Int
        get() = wrappedLoginator.level
        set(value) {
            wrappedLoginator.level = value
        }

    // endregion Properties

    // region Implemented methods

    override fun d(tag: String, msg: String): Int {
        return wrappedLoginator.d(tag, msg)
    }

    override fun d(tag: String, msg: String, tr: Throwable): Int {
        return wrappedLoginator.d(tag, msg, tr)
    }

    override fun e(tag: String, msg: String): Int {
        return wrappedLoginator.e(tag, msg)
    }

    override fun e(tag: String, msg: String, tr: Throwable): Int {
        return wrappedLoginator.e(tag, msg, tr)
    }

    override fun i(tag: String, msg: String): Int {
        return wrappedLoginator.i(tag, msg)
    }

    override fun i(tag: String, msg: String, tr: Throwable): Int {
        return wrappedLoginator.i(tag, msg, tr)
    }

    override fun logUncaughtException(t: Thread?, e: Throwable?, packageName: String?): Int {
        return wrappedLoginator.logUncaughtException(t, e, packageName)
    }

    override fun v(tag: String, msg: String): Int {
        return wrappedLoginator.v(tag, msg)
    }

    override fun v(tag: String, msg: String, tr: Throwable): Int {
        return wrappedLoginator.v(tag, msg, tr)
    }

    override fun w(tag: String, msg: String): Int {
        return wrappedLoginator.w(tag, msg)
    }

    override fun w(tag: String, msg: String, tr: Throwable): Int {
        return wrappedLoginator.w(tag, msg, tr)
    }

    // endregion Implemented methods
}
