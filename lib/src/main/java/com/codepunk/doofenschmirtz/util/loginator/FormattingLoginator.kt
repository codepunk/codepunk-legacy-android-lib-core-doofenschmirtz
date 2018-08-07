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

import android.util.Log.*
import com.codepunk.doofenschmirtz.util.topLevelClass

// region Constants

/**
 * An empty tag.
 */

private const val EMPTY_TAG = ""

// endregion Constants

/**
 * A [LoginatorWrapper] that automatically formats `tag` and `msg` arguments sent to the
 * [wrappedLoginator] according to the logic passed in [tagFormatter] and [msgFormatter]. By
 * default, any tag supplied via the [d], [i], [e], [v] and [w] methods will be prepended with the
 * simple name of the top-level class in which the method call occurred, or the filename if the 
 * top-level class cannot be determined. Messages will be prepended with information about the 
 * file, class and line number at which the call occurred. This has the added benefit of being 
 * clickable in Logcat, which will take you directly to that file/class/line.
 *
 * As an example, if the following code is in MainActivity.kt:
 *
 *     43 private val logger = FormattingLoginator(LogcatLoginator())
 *     44
 *     45 override fun onCreate(savedInstanceState: Bundle?) {
 *     46     super.onCreate(savedInstanceState)
 *     47     logger.d("I'm doing something important!")
 *     48 }
 *
 * You would see something like this in Logcat:
 *
 *     D/MainActivity: onCreate(MainActivity.kt:47) I'm doing something important!
 * 
 * If the call at line 47 was changed to
 * 
 *     47     logger.d("NewFeature", "I'm doing something important!")
 * 
 * Then you would see something like this in Logcat:
 * 
 *     D/MainActivity|NewFeature: onCreate(MainActivity.kt:47) I'm doing something important!
 *
 * You can override this default formatting by supplying your own [tagFormatter] and/or
 * [msgFormatter] when creating your [FormattingLoginator].
 *
 * Note that there are some performance issues with generating Throwables and combing through
 * StackTraceElements, so it is recommended that you check whether a Loginator is loggable
 * using the [isLoggable] method before logging your message.
 */
open class FormattingLoginator(
        baseLoginator: Loginator = LogcatLoginator(),
        val tagFormatter: (element: StackTraceElement, tag: String) -> String = { element, tag ->
            formatTag(element, tag)
        },
        val msgFormatter: (element: StackTraceElement, msg: String) -> String = { element, msg ->
            formatMsg(element, msg)
        }
) : LoginatorWrapper(baseLoginator) {

    // region Inherited methods

    /**
     * Sends a [DEBUG] log message using [tag] formatted via [tagFormatter] and [msg] formatted
     * via [msgFormatter].
     */
    override fun d(tag: String, msg: String): Int {
        return d(tag, msg, LoginatorThrowable())
    }

    /**
     * Sends a [DEBUG] log message using [tag] formatted via [tagFormatter] and [msg] formatted
     * via [msgFormatter], and logs the exception.
     */
    override fun d(tag: String, msg: String, tr: Throwable): Int {
        return getSignificantStackTraceElement(tr).run {
            val formattedTag = tagFormatter(this, tag)
            val formattedMsg = msgFormatter(this, msg)
            if (tr is LoginatorThrowable) wrappedLoginator.d(formattedTag, formattedMsg)
            else wrappedLoginator.d(formattedTag, formattedMsg, tr)
        }
    }

    /**
     * Sends an [ERROR] log message using [tag] formatted via [tagFormatter] and [msg] formatted
     * via [msgFormatter].
     */
    override fun e(tag: String, msg: String): Int {
        return e(tag, msg, LoginatorThrowable())
    }

    /**
     * Sends an [ERROR] log message using [tag] formatted via [tagFormatter] and [msg] formatted
     * via [msgFormatter], and logs the exception.
     */
    override fun e(tag: String, msg: String, tr: Throwable): Int {
        return getSignificantStackTraceElement(tr).run {
            val formattedTag = tagFormatter(this, tag)
            val formattedMsg = msgFormatter(this, msg)
            if (tr is LoginatorThrowable) wrappedLoginator.e(formattedTag, formattedMsg)
            else wrappedLoginator.e(formattedTag, formattedMsg, tr)
        }
    }

    /**
     * Sends an [INFO] log message using [tag] formatted via [tagFormatter] and [msg] formatted
     * via [msgFormatter].
     */
    override fun i(tag: String, msg: String): Int {
        return i(tag, msg, LoginatorThrowable())
    }

    /**
     * Sends an [INFO] log message using [tag] formatted via [tagFormatter] and [msg] formatted
     * via [msgFormatter], and logs the exception.
     */
    override fun i(tag: String, msg: String, tr: Throwable): Int {
        return getSignificantStackTraceElement(tr).run {
            val formattedTag = tagFormatter(this, tag)
            val formattedMsg = msgFormatter(this, msg)
            if (tr is LoginatorThrowable) wrappedLoginator.i(formattedTag, formattedMsg)
            else wrappedLoginator.i(formattedTag, formattedMsg, tr)
        }
    }

    /**
     * Sends a [VERBOSE] log message using [tag] formatted via [tagFormatter] and [msg] formatted
     * via [msgFormatter].
     */
    override fun v(tag: String, msg: String): Int {
        return v(tag, msg, LoginatorThrowable())
    }

    /**
     * Sends a [VERBOSE] log message using [tag] formatted via [tagFormatter] and [msg] formatted
     * via [msgFormatter], and logs the exception.
     */
    override fun v(tag: String, msg: String, tr: Throwable): Int {
        return getSignificantStackTraceElement(tr).run {
            val formattedTag = tagFormatter(this, tag)
            val formattedMsg = msgFormatter(this, msg)
            if (tr is LoginatorThrowable) wrappedLoginator.v(formattedTag, formattedMsg)
            else wrappedLoginator.v(formattedTag, formattedMsg, tr)
        }
    }

    /**
     * Sends a [WARN] log message using [tag] formatted via [tagFormatter] and [msg] formatted
     * via [msgFormatter].
     */
    override fun w(tag: String, msg: String): Int {
        return w(tag, msg, LoginatorThrowable())
    }

    /**
     * Sends a [WARN] log message using [tag] formatted via [tagFormatter] and [msg] formatted
     * via [msgFormatter], and logs the exception.
     */
    override fun w(tag: String, msg: String, tr: Throwable): Int {
        return getSignificantStackTraceElement(tr).run {
            val formattedTag = tagFormatter(this, tag)
            val formattedMsg = msgFormatter(this, msg)
            if (tr is LoginatorThrowable) wrappedLoginator.v(formattedTag, formattedMsg)
            else wrappedLoginator.v(formattedTag, formattedMsg, tr)
        }
    }

    // endregion Inherited methods

    // region Methods

    /**
     * Sends a [DEBUG] log message using an empty string passed to [tagFormatter] and [msg]
     * formatted via [msgFormatter], and logs the exception [tr] if one was supplied.
     */
    fun d(msg: String, tr: Throwable = LoginatorThrowable()): Int {
        return d(EMPTY_TAG, msg, tr)
    }

    /**
     * Sends an [ERROR] log message using an empty string passed to [tagFormatter] and [msg]
     * formatted via [msgFormatter], and logs the exception [tr] if one was supplied.
     */
    fun e(msg: String, tr: Throwable = LoginatorThrowable()): Int {
        return e(EMPTY_TAG, msg, tr)
    }

    /**
     * Sends an [INFO] log message using an empty string passed to [tagFormatter] and [msg]
     * formatted via [msgFormatter], and logs the exception [tr] if one was supplied.
     */
    fun i(msg: String, tr: Throwable = LoginatorThrowable()): Int {
        return i(EMPTY_TAG, msg, tr)
    }

    /**
     * Sends a [VERBOSE] log message using an empty string passed to [tagFormatter] and [msg]
     * formatted via [msgFormatter], and logs the exception [tr] if one was supplied.
     */
    fun v(msg: String, tr: Throwable = LoginatorThrowable()): Int {
        return v(EMPTY_TAG, msg, tr)
    }

    /**
     * Sends a [WARN] log message using an empty string passed to [tagFormatter] and [msg]
     * formatted via [msgFormatter], and logs the exception [tr] if one was supplied.
     */
    fun w(msg: String, tr: Throwable = LoginatorThrowable()): Int {
        return w(EMPTY_TAG, msg, tr)
    }

    // endregion Methods

    // region Companion object
    
    companion object {

        // region Methods

        /**
         * The default message formatter. Prepends [msg] with information about where the call
         * occurred, using data from [element].
         */
        protected fun formatMsg(element: StackTraceElement, msg: String): String {
            return "${element.methodName}(${element.fileName}:${element.lineNumber})".let {
                if (msg.isEmpty()) it else "$it $msg"
            }
        }

        /**
         * The default tag formatter. Prepends [tag] with simple name of the top-level class in
         * which the call occurred according to [element], or the name of the file if the top-level
         * class could not be determined.
         */
        protected fun formatTag(element: StackTraceElement, tag: String): String {
            return try {
                Class.forName(element.className).topLevelClass.simpleName
            } catch (e: ClassNotFoundException) {
                element.fileName.split('.')[0]
            }.let {
                if (tag.isEmpty()) it else "$it|$tag"
            }
        }

        /**
         * A convenience function to return the significant [StackTraceElement] from the supplied
         * [Throwable] [tr]. If the throwable is an instance of [LoginatorThrowable], we know it was
         * created from within this class and need to pop the extra element from the stack trace
         * generated by [tr].
         */
        private fun getSignificantStackTraceElement(tr: Throwable): StackTraceElement {
            return (tr.cause ?: tr).stackTrace[if (tr is LoginatorThrowable) 1 else 0]
        }

        // endregion Methods
    }
    
    // endregion Companion object
    
    // region Nested classes

    /**
     * A local version of [Throwable] that will be used to determine if [Throwable] instances
     * were created inside this class.
     */
    protected class LoginatorThrowable : Throwable()
    
    // endregion Nested classes
}