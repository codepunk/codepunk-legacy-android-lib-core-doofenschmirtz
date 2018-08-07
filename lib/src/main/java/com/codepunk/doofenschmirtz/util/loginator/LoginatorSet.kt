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

import android.util.Log
import android.util.Log.*

/**
 * A special [Loginator] that is actually itself a [Set] of Loginators. Operations carried out
 * on this Loginator are propagated down to all Loginators in the set.
 */
class LoginatorSet : HashSet<Loginator>(), Loginator {

    // region Properties

    override var level: Int
        get() {
            var level = Log.ASSERT
            for (logger in this) {
                level = Math.min(level, logger.level)
            }
            return level
        }
        set(value) {
            for (logger in this) {
                logger.level = value
            }
        }

    // endregion Properties

    // region Implemented methods

    /**
     * Sends a [DEBUG] log message to all Loginators in this set.
     */
    override fun d(tag: String, msg: String): Int {
        var total = 0
        for (logger in this) {
            total += logger.d(tag, msg)
        }
        return total
    }

    /**
     * Sends a [DEBUG] log message including the exception [tr] to all Loginators in this set.
     */
    override fun d(tag: String, msg: String, tr: Throwable): Int {
        var total = 0
        for (logger in this) {
            total += logger.d(tag, msg, tr)
        }
        return total
    }

    /**
     * Sends an [ERROR] log message to all Loginators in this set.
     */
    override fun e(tag: String, msg: String): Int {
        var total = 0
        for (logger in this) {
            total += logger.e(tag, msg)
        }
        return total
    }

    /**
     * Sends an [ERROR] log message including the exception [tr] to all Loginators in this set.
     */
    override fun e(tag: String, msg: String, tr: Throwable): Int {
        var total = 0
        for (logger in this) {
            total += logger.e(tag, msg, tr)
        }
        return total
    }

    /**
     * Sends an [INFO] log message to all Loginators in this set.
     */
    override fun i(tag: String, msg: String): Int {
        var total = 0
        for (logger in this) {
            total += logger.i(tag, msg)
        }
        return total
    }

    /**
     * Sends an [INFO] log message including the exception [tr] to all Loginators in this set.
     */
    override fun i(tag: String, msg: String, tr: Throwable): Int {
        var total = 0
        for (logger in this) {
            total += logger.i(tag, msg, tr)
        }
        return total
    }

    /**
     * Logs an uncaught exception to all Loginators in this set.
     */
    override fun logUncaughtException(t: Thread?, e: Throwable?, packageName: String?): Int {
        var total = 0
        for (logger in this) {
            total += logger.logUncaughtException(t, e, packageName)
        }
        return total
    }

    /**
     * Sends a [VERBOSE] log message to all Loginators in this set.
     */
    override fun v(tag: String, msg: String): Int {
        var total = 0
        for (logger in this) {
            total += logger.v(tag, msg)
        }
        return total
    }

    /**
     * Sends a [VERBOSE] log message including the exception [tr] to all Loginators in this set.
     */
    override fun v(tag: String, msg: String, tr: Throwable): Int {
        var total = 0
        for (logger in this) {
            total += logger.v(tag, msg, tr)
        }
        return total
    }

    /**
     * Sends a [WARN] log message to all Loginators in this set.
     */
    override fun w(tag: String, msg: String): Int {
        var total = 0
        for (logger in this) {
            total += logger.w(tag, msg)
        }
        return total
    }

    /**
     * Sends a [WARN] log message including the exception [tr] to all Loginators in this set.
     */
    override fun w(tag: String, msg: String, tr: Throwable): Int {
        var total = 0
        for (logger in this) {
            total += logger.w(tag, msg, tr)
        }
        return total
    }

    // endregion Implemented methods
}