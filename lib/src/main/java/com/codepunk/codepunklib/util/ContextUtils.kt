/*
 * Copyright (C) 2018 Codepunk, LLC
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
 */

package com.codepunk.codepunklib.util

import android.content.Context
import android.content.Intent

/**
 * A value indicating the default flags for starting the launch activity using the
 * [startLaunchActivity] method.
 */
const val START_LAUNCH_ACTIVITY_DEFAULT_FLAGS = Intent.FLAG_ACTIVITY_CLEAR_TOP

/**
 * Extension method on [Context] that starts the launch activity. Depending on the flags passed,
 * this can clear all other activities on the back stack when (re)starting the launch activity.
 */
fun Context.startLaunchActivity(flags: Int = START_LAUNCH_ACTIVITY_DEFAULT_FLAGS) {
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    intent.flags = flags
    return startActivity(intent)
}