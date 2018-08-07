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

import android.app.ActivityManager
import android.app.Application
import android.os.Process
import android.util.Log.*

/**
 * The basic interface for all extended logging operations in the Codepunk
 * Doofenschmirtz library.
 */
interface Loginator {

    // region Properties

    /**
     * The logging level for this logger, i.e., one of [ASSERT], [DEBUG], [ERROR], [INFO],
     * [VERBOSE], or [WARN].
     */
    var level: Int

    // endregion Properties

    // region Methods

    /**
     * Checks to see whether or not a log is loggable at the specified level.
     */
    fun isLoggable(level: Int): Boolean {
        return level >= this.level
    }

    /**
     * Sends a [DEBUG] log message.
     */
    fun d(tag: String, msg: String): Int

    /**
     * Sends a [DEBUG] log message and logs the exception.
     */
    fun d(tag: String, msg: String, tr: Throwable): Int

    /**
     * Sends an [ERROR] log message.
     */
    fun e(tag: String, msg: String): Int

    /**
     * Sends an [ERROR] log message and logs the exception.
     */
    fun e(tag: String, msg: String, tr: Throwable): Int

    /**
     * Sends an [INFO] log message.
     */
    fun i(tag: String, msg: String): Int

    /**
     * Sends an [INFO] log message and logs the exception.
     */
    fun i(tag: String, msg: String, tr: Throwable): Int

    /**
     * Logs an uncaught exception. [packageName] can optionally be supplied by the calling class.
     * Although obtaining the ID of a process is straightforward (via the [Process.myPid] method,
     * getting the name of a process is more convoluted. As of API 28, the simplest way is via the
     * [Application.getProcessName] method. Prior to that, the best bet might be via the
     * [ActivityManager.getRunningAppProcesses] method:
     *
     *     var processName = null
     *     val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
     *     for (processInfo in activityManager.runningAppProcesses) {
     *         if (pid == processInfo.pid) {
     *             processName = processInfo.processName
     *         }
     *     }
     */
    fun logUncaughtException(t: Thread?, e: Throwable?, packageName: String? = null): Int

    /**
     * Sends a [VERBOSE] log message.
     */
    fun v(tag: String, msg: String): Int

    /**
     * Sends a [VERBOSE] log message and logs the exception.
     */
    fun v(tag: String, msg: String, tr: Throwable): Int

    /**
     * Sends a [WARN] log message.
     */
    fun w(tag: String, msg: String): Int

    /**
     * Sends a [WARN] log message and logs the exception.
     */
    fun w(tag: String, msg: String, tr: Throwable): Int

    // endregion Methods
}