/*
 * Copyright (C) 2019 Codepunk, LLC
 * Author(s): Scott Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codepunk.doofenschmirtz.util

import androidx.lifecycle.MutableLiveData
import com.codepunk.doofenschmirtz.util.resourceinator.Resource
import com.codepunk.doofenschmirtz.util.resourceinator.PendingResource

/**
 * A convenience method to consume a [MutableLiveData] that wraps a [Resource] back to
 * an instance of [PendingResource].
 */
fun <Progress, Result> MutableLiveData<Resource<Progress, Result>>.consume() {
    value = PendingResource()
}
