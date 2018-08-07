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
import android.text.TextUtils

private const val UNCAUGHT_EXCEPTION_TAG = "AndroidRuntime"
private const val FATAL_EXCEPTION = "FATAL_EXCEPTION"
private const val PROCESS_LABEL = "Process: "
private const val PID_LABEL = "PID: "

/**
 * A [Loginator] with basic logic for formatting and logging an uncaught exception.
 */
abstract class BaseLoginator : Loginator {

    // region Properties

    /**
     * Controls whether uncaught exceptions should be logged. In some environments, for example,
     * even uncaught exceptions are sent to Logcat, so a Loginator that works with Logcat might
     * want to suppress logging uncaught exceptions.
     */
    open var logUncaughtExceptions: Boolean = true

    // endregion Properties

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
    override fun logUncaughtException(t: Thread?, e: Throwable?, packageName: String?): Int {
        return when (logUncaughtExceptions) {
            false -> 0
            else -> {
                val builder = StringBuilder(FATAL_EXCEPTION)
                if (!TextUtils.isEmpty(t?.name)) {
                    builder.append(": ${t?.name}")
                }
                builder.append("\n")
                if (!TextUtils.isEmpty(packageName)) {
                    builder.append("$PROCESS_LABEL$packageName, ")
                }
                builder.append("$PID_LABEL${Process.myPid()}")
                when (e) {
                    null -> this.e(UNCAUGHT_EXCEPTION_TAG, builder.toString())
                    else -> this.e(UNCAUGHT_EXCEPTION_TAG, builder.toString(), e)
                }
            }
        }
    }
}