/*
 * Copyright 2018 Codepunk, LLC
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
 * The maximum tag length.
 */
private const val MAX_TAG_LENGTH = 23

/**
 * A [Loginator] that uses the standard Logcat as its output, and set to be loggable
 * according to the supplied [level].
 */
open class LogcatLoginator(override var level: Int = Log.INFO) : BaseLoginator() {

    // region Properties

    override var logUncaughtExceptions: Boolean = false

    // endregion Properties

    // region Implemented methods
    
    /**
     * Sends a [DEBUG] log message to Logcat.
     */
    override fun d(tag: String, msg: String): Int {
        return Log.d(trimTag(tag), msg)
    }

    /**
     * Sends a [DEBUG] log message to Logcat and logs the exception.
     */
    override fun d(tag: String, msg: String, tr: Throwable): Int {
        return Log.d(trimTag(tag), msg, tr)
    }

    /**
     * Sends an [ERROR] log message to Logcat.
     */
    override fun e(tag: String, msg: String): Int {
        return Log.e(trimTag(tag), msg)
    }

    /**
     * Sends an [ERROR] log message to Logcat and logs the exception.
     */
    override fun e(tag: String, msg: String, tr: Throwable): Int {
        return Log.e(trimTag(tag), msg, tr)
    }

    /**
     * Sends an [INFO] log message to Logcat.
     */
    override fun i(tag: String, msg: String): Int {
        return Log.i(trimTag(tag), msg)
    }

    /**
     * Sends an [INFO] log message to Logcat and logs the exception.
     */
    override fun i(tag: String, msg: String, tr: Throwable): Int {
        return Log.i(trimTag(tag), msg, tr)
    }

    /**
     * Sends a [VERBOSE] log message to Logcat.
     */
    override fun v(tag: String, msg: String): Int {
        return Log.v(trimTag(tag), msg)
    }

    /**
     * Sends a [VERBOSE] log message to Logcat and logs the exception.
     */
    override fun v(tag: String, msg: String, tr: Throwable): Int {
        return Log.v(trimTag(tag), msg, tr)
    }

    /**
     * Sends a [WARN] log message to Logcat.
     */
    override fun w(tag: String, msg: String): Int {
        return Log.w(trimTag(tag), msg)
    }

    /**
     * Sends a [WARN] log message to Logcat and logs the exception.
     */
    override fun w(tag: String, msg: String, tr: Throwable): Int {
        return Log.w(trimTag(tag), msg, tr)
    }
    
    // endregion Implemented methods

    // region Companion object
    
    companion object {
        
        // region Methods
        
        protected fun trimTag(tag: String): String {
            return when {
                tag.length > MAX_TAG_LENGTH -> tag.substring(0, MAX_TAG_LENGTH)
                else -> tag
            }
        }
        
        // endregion Methods
    }
    
    // endregion Companion object
}