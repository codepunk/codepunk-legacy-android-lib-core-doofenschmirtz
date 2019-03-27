/*
 * Copyright 2019 Codepunk, LLC
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

package com.codepunk.doofenschmirtz.app

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_NEUTRAL
import android.content.DialogInterface.BUTTON_POSITIVE
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.codepunk.doofenschmirtz.BuildConfig

/**
 * A generic [AppCompatDialogFragment] that displays an alert dialog.
 */
@Suppress("UNUSED")
open class AlertDialogFragment :
    AppCompatDialogFragment(),
    OnClickListener {

    // region Properties

    private var listenerSource: ListenerSource = ListenerSource.CUSTOM

    /**
     * The [OnBuildAlertDialogListener] that will listen for events related to this
     * AlertDialogFragment.
     */
    var listener: OnBuildAlertDialogListener? = null

    /**
     * The integer request code originally supplied to [show], allowing you to identify who this
     * result came from.
     */
    protected var requestCode: Int = -1
        private set

    /**
     * The result code that will be sent to the listener via
     * [OnBuildAlertDialogListener.onAlertDialogResult].
     */
    @Suppress("WEAKER_ACCESS")
    protected var resultCode: Int = RESULT_CANCELED

    // endregion Properties

    // region Lifecycle methods

    /**
     * Restores the instance state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.also {
            listenerSource = it.getSerializable(BuildConfig.KEY_LISTENER_SOURCE) as ListenerSource
            requestCode = it.getInt(BuildConfig.KEY_REQUEST_CODE, -1)
        }
        listener = when (listenerSource) {
            ListenerSource.ACTIVITY -> activity as? OnBuildAlertDialogListener
            ListenerSource.TARGET_FRAGMENT -> targetFragment as? OnBuildAlertDialogListener
            else -> null
        }
    }

    /**
     * Saves the instance state.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(BuildConfig.KEY_LISTENER_SOURCE, listenerSource)
        outState.putInt(BuildConfig.KEY_REQUEST_CODE, requestCode)
    }

    // endregion Lifecycle methods

    // region Inherited methods

    /**
     * Builds the [Dialog] that confirms that the user wishes to disable developer options.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        onBuildAlertDialog(builder)
        listener?.onBuildAlertDialog(requestCode, builder)
        return builder.create()
    }

    /**
     * Sets [resultCode] to [Activity.RESULT_CANCELED] so it can be passed to the listener
     * when the dialog is dismissed.
     */
    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        resultCode = Activity.RESULT_CANCELED
    }

    /**
     * Called when the dialog is dismissing.
     */
    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        // Don't notify listener if the dialog is not attached. This will catch the
        // difference between a dialog dismissing due to configuration change vs. a user action.
        if (isAdded) {
            listener?.also {
                onNotifyListener(it)
            }
        }
    }

    // endregion Inherited methods

    // region Implemented methods

    /**
     * Sets [resultCode] to the appropriate value based on the selected button.
     */
    override fun onClick(dialog: DialogInterface?, which: Int) {
        resultCode = when (which) {
            BUTTON_POSITIVE -> RESULT_POSITIVE
            BUTTON_NEGATIVE -> RESULT_NEGATIVE
            BUTTON_NEUTRAL -> RESULT_NEUTRAL
            else -> return
        }
    }

    // endregion Implemented methods

    // region Methods

    /**
     * Convenience method that allows descendants of this dialog fragment to customize the
     * [AlertDialog.Builder] before the dialog is built.
     */
    open fun onBuildAlertDialog(builder: AlertDialog.Builder) {
        // No op
    }

    /**
     * Calls [OnBuildAlertDialogListener.onAlertDialogResult] on the supplied [listener] with the
     * appropriate request code and result code. Descendants of this fragment can include other data
     * in the call (via the data argument to onAlertDialogResult) if needed.
     */
    open fun onNotifyListener(listener: OnBuildAlertDialogListener) {
        listener.onAlertDialogResult(requestCode, resultCode, null)
    }

    // endregion Methods

    // region Companion object

    companion object {

        // region Properties

        /**
         * A result code corresponding to a positive button click.
         */
        @JvmStatic
        val RESULT_POSITIVE: Int = Activity.RESULT_OK

        /**
         * A result code corresponding to a negative button click.
         */
        @JvmStatic
        val RESULT_NEGATIVE: Int = Activity.RESULT_FIRST_USER

        /**
         * A result code corresponding to a neutral button click.
         */
        @JvmStatic
        val RESULT_NEUTRAL: Int = Activity.RESULT_FIRST_USER + 1

        /**
         * A result code corresponding to the dialog being canceled.
         */
        @JvmStatic
        val RESULT_CANCELED: Int = Activity.RESULT_CANCELED

        // endregion Properties

        // region Methods

        /**
         * Convenience method to show this AlertDialogFragment from an activity.
         */
        fun show(
            activity: FragmentActivity,
            tag: String,
            requestCode: Int = -1,
            arguments: Bundle? = null,
            listener: OnBuildAlertDialogListener? = activity as? OnBuildAlertDialogListener
        ): AlertDialogFragment =
            activity.supportFragmentManager.findFragmentByTag(tag) as? AlertDialogFragment
                ?: AlertDialogFragment().apply {
                    listenerSource = when (listener) {
                        activity -> ListenerSource.ACTIVITY
                        else -> ListenerSource.CUSTOM
                    }
                    this.listener = listener
                    this.requestCode = requestCode
                    this.arguments = arguments
                    show(activity.supportFragmentManager, tag)
                }

        /**
         * Convenience method to show this AlertDialogFragment from a fragment.
         */
        fun show(
            targetFragment: Fragment,
            tag: String,
            requestCode: Int = -1,
            arguments: Bundle? = null,
            listener: OnBuildAlertDialogListener? = targetFragment as? OnBuildAlertDialogListener
        ): AlertDialogFragment =
            targetFragment.requireFragmentManager().findFragmentByTag(tag) as? AlertDialogFragment
                ?: AlertDialogFragment().apply {
                    listenerSource = when (listener) {
                        targetFragment -> ListenerSource.TARGET_FRAGMENT
                        else -> ListenerSource.CUSTOM
                    }
                    this.listener = listener
                    this.requestCode = requestCode
                    this.arguments = arguments
                    setTargetFragment(targetFragment, requestCode)
                    show(targetFragment.requireFragmentManager(), tag)
                }

        // endregion Methods

    }

    // endregion Companion object

    // region Nested/inner classes

    private enum class ListenerSource {
        ACTIVITY,
        TARGET_FRAGMENT,
        CUSTOM
    }

    /**
     * An interface that allows a listener of this [AlertDialogFragment] to customize the
     * [AlertDialog.Builder] before the dialog is built. [onBuildAlertDialog] will be called on
     * the listener before the dialog is built.
     */
    interface OnBuildAlertDialogListener {

        // region Methods

        /**
         * Called from [onCreateDialog] before the dialog is built.
         */
        fun onBuildAlertDialog(requestCode: Int, builder: AlertDialog.Builder)

        /**
         * Called when an AlertDialogFragment you launched exits, giving you the requestCode you
         * started it with, the resultCode it returned, and any additional data from it.
         */
        fun onAlertDialogResult(requestCode: Int, resultCode: Int, data: Intent? = null)

        // endregion Methods
    }

    // endregion Nested/inner classes

}
