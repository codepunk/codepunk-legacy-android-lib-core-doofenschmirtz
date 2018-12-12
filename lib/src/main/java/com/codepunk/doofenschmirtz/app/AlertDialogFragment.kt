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

package com.codepunk.doofenschmirtz.app

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_NEUTRAL
import android.content.DialogInterface.BUTTON_POSITIVE
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment

/**
 * A generic [AppCompatDialogFragment] that displays an alert dialog.
 */
@Suppress("UNUSED")
open class AlertDialogFragment :
    AppCompatDialogFragment(),
    OnClickListener {

    // region Properties

    /**
     * The result code that will be sent to the target fragment via [Fragment.onActivityResult].
     */
    @Suppress("WEAKER_ACCESS")
    protected var resultCode: Int = RESULT_CANCELED

    // endregion Properties

    // region Inherited methods

    /**
     * Builds the [Dialog] that confirms that the user wishes to disable developer options.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        onBuildAlertDialog(builder)
        (targetFragment as? OnAlertDialogBuildListener)?.onBuildAlertDialog(this, builder)
        return builder.create()
    }

    /**
     * Sets [resultCode] to [Activity.RESULT_CANCELED] so it can be passed to the target fragment
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
        // Don't notify target fragment if the dialog is not attached. This will catch the
        // difference between a dialog dismissing due to configuration change vs. a user action.
        if (isAdded) {
            targetFragment?.run {
                onNotifyTargetFragment(this)
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
     * Calls [Fragment.onActivityResult] on the supplied [targetFragment] with the appropriate
     * request code and result code. Descendants of this fragment can include other data in
     * the call (via the data argument to onActivityResult) if needed.
     */
    open fun onNotifyTargetFragment(targetFragment: Fragment) {
        targetFragment.onActivityResult(targetRequestCode, resultCode, null)
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
         * Convenience method to show this [AlertDialogFragment].
         */
        fun show(
            tag: String,
            targetFragment: Fragment,
            requestCode: Int,
            arguments: Bundle? = null
        ): AlertDialogFragment = AlertDialogFragment().apply {
            setTargetFragment(targetFragment, requestCode)
            this.arguments = arguments
            show(targetFragment.requireFragmentManager(), tag)
        }

        // endregion Methods

    }

    // endregion Companion object

    // region Nested/inner classes

    /**
     * An interface that allows the target fragment of this [AlertDialogFragment] to customize
     * the [AlertDialog.Builder] before the dialog is built. If the target fragment implements
     * [OnAlertDialogBuildListener], then [onBuildAlertDialog] will be called on the target
     * fragment before the dialog is built.
     */
    interface OnAlertDialogBuildListener {

        // region Methods

        /**
         * Called from [onCreateDialog] before the dialog is built.
         */
        fun onBuildAlertDialog(fragment: AlertDialogFragment, builder: AlertDialog.Builder)

        // endregion Methods
    }

    // endregion Nested/inner classes

}
