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

package com.codepunk.codepunklib.view.animation

import android.animation.TimeInterpolator
import android.view.animation.CycleInterpolator
import android.view.animation.Interpolator

private const val DEFAULT_FREQUENCY = 5

class ShakeInterpolator(
        frequency: Int = DEFAULT_FREQUENCY,
        private val amplitudeInterpolator: Interpolator? = CycleInterpolator(0.5f)):
        TimeInterpolator {

    private val cycleInterpolator = CycleInterpolator(frequency.toFloat())

    override fun getInterpolation(input: Float): Float {
        return cycleInterpolator.getInterpolation(input) *
                (amplitudeInterpolator?.getInterpolation(input) ?: 1.0f)
    }
}