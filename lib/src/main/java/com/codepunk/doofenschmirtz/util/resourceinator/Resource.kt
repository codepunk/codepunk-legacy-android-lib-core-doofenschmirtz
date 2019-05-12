/*
 * Copyright (C) 2018 Codepunk, LLC
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

package com.codepunk.doofenschmirtz.util.resourceinator

import android.os.Bundle
import java.lang.Exception
import java.util.*

/**
 * A sealed class representing the various outputs from a [Resourceinator].
 */
sealed class Resource<Progress, Result>(

    /**
     * Optional additional data to send along with the resource.
     */
    var data: Bundle? = null

) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Resource<*, *>) return false

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        return data?.hashCode() ?: 0
    }

}

/**
 * A [Resource] representing a pending task (i.e. a task that has not been executed yet).
 */
class PendingResource<Progress, Result>(

    /**
     * Optional additional data to send along with the resource.
     */
    data: Bundle? = null

) : Resource<Progress, Result>(data) {

    // region Inherited methods

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PendingResource<*, *>) return false
        if (!super.equals(other)) return false
        return true
    }

    @Suppress("REDUNDANT_OVERRIDING_METHOD")
    override fun hashCode(): Int = super.hashCode()

    override fun toString(): String = "${javaClass.simpleName}(data=$data)"

    // endregion Inherited methods

}

/**
 * A [Resource] correlating to a task that is currently in progress (i.e. a running task).
 */
class ProgressResource<Progress, Result>(

    /**
     * The values indicating progress of the task.
     */
    val progress: Array<out Progress?>,

    /**
     * Optional additional data to send along with the resource.
     */
    data: Bundle? = null

) : Resource<Progress, Result>(data) {

    // region Constructors

    @Suppress("UNUSED")
    constructor(vararg progress: Progress) : this(progress, null)

    // endregion Constructors

    // region Inherited methods

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ProgressResource<*, *>) return false
        if (!super.equals(other)) return false

        if (!progress.contentEquals(other.progress)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + progress.contentHashCode()
        return result
    }

    override fun toString(): String = javaClass.simpleName +
        "(progress=${Arrays.toString(progress)}, data=$data)"

    // endregion Inherited methods

}

/**
 * An abstract class that represents a Resource that contains a result (whether success
 * or failure).
 */
abstract class ResultResource<Progress, Result>(

    /**
     * The result of the operation computed by the task.
     */
    val result: Result?,

    /**
     * Optional additional data to send along with the resource.
     */
    data: Bundle?

) : Resource<Progress, Result>(data)

/**
 * A [Resource] representing a finished task (i.e. a task that has finished without being
 * cancelled).
 */
@SuppressWarnings("EqualsAndHashcode")
class SuccessResource<Progress, Result>(

    /**
     * The result of the operation computed by the task.
     */
    result: Result? = null,

    /**
     * Optional additional data to send along with the resource.
     */
    data: Bundle? = null

) : ResultResource<Progress, Result>(result, data) {

    // region Inherited methods

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SuccessResource<*, *>) return false
        if (!super.equals(other)) return false
        return true
    }

    @Suppress("REDUNDANT_OVERRIDING_METHOD")
    override fun hashCode(): Int = super.hashCode()

    override fun toString(): String =
        "${javaClass.simpleName}(result=$result, data=$data)"

    // endregion Inherited methods

}

/**
 * A [Resource] representing a failed task (i.e. a task that was cancelled or experienced
 * some other sort of error during execution).
 */
class FailureResource<Progress, Result>(

    /**
     * The result, if any, computed by the task. Can be null.
     */
    result: Result? = null,

    /**
     * The [Exception] associated with this failure.
     */
    val e: Exception? = null,

    /**
     * Optional additional data to send along with the resource.
     */
    data: Bundle? = null

) : ResultResource<Progress, Result>(result, data) {

    // region Inherited methods

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FailureResource<*, *>) return false
        if (!super.equals(other)) return false

        if (e != other.e) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (e?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String =
        "${javaClass.simpleName}(result=$result, e=$e, data=$data)"

    // endregion Inherited methods

}
