package com.codepunk.doofenschmirtz.util.taskinator

import android.os.Looper
import androidx.annotation.WorkerThread
import java.lang.Exception
import java.lang.IllegalStateException
import java.util.concurrent.CancellationException

/**
 * A utility class that standardizes the resolution of data between [Local] and [Remote] data
 * sources into a common [Domain] model class.
 */
abstract class DataResolvinator<Params, Local, Remote, Domain> {

    /**
     * A flag that indicates whether this DataResolvinator will attempt to fetch the latest
     * remote data even if cached data already exists. Descendants can set this variable
     * via the [shouldAlwaysFetch] method.
     */
    private var alwaysFetch: Boolean = true

    /**
     * A flag that indicates whether this DataResolvinator has been cancelled. Many implementations
     * will ignore this value but this is particularly helpful if being run inside a
     * [DataTaskinator].
     */
    open val isCancelled: Boolean = false

    /**
     * A method that returns the resolved entity after visiting the [Local] and possibly the
     * [Remote] data source(s). Note that this method can not be called from the main thread.
     */
    @WorkerThread
    @Throws(
        IllegalStateException::class,
        IllegalArgumentException::class,
        CancellationException::class
    )
    fun get(vararg params: Params): Domain {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            throw IllegalStateException(
                "Cannot invoke get from the main thread"
            )
        }

        processParams(params)?.run {
            throw IllegalArgumentException(this)
        }

        val cached: Domain = localToDomain(retrieveLocal(params))
        onCache(cached)

        if (isCancelled) {
            throw CancellationException()
        }

        return when {
            !isValid(cached) || alwaysFetch -> {
                try {
                    val remote: Remote = fetchRemote(params)

                    if (isCancelled) {
                        throw CancellationException()
                    }

                    cacheLocal(remoteToLocal(remote))
                    localToDomain(retrieveLocal(params))
                } catch (e: Exception) {
                    // Only throw the exception if our cached entity is invalid
                    if (isValid(cached)) cached else throw e
                }
            }
            else -> cached
        }
    }

    /**
     * Specifies whether this [DataResolvinator] should attempt to fetch from the [Remote] data
     * source even if a [Local], cached version of the data exists.
     */
    open fun shouldAlwaysFetch(
        alwaysFetch: Boolean
    ): DataResolvinator<Params, Local, Remote, Domain> {
        this.alwaysFetch = alwaysFetch
        return this
    }

    /**
     * An optional method that extracts any needed parameters from the [params] array. Returning
     * a null string from this method means that all parameters were obtained successfully;
     * any non-null value returned will result in an [IllegalArgumentException] being thrown with
     * the returned value as the message.
     */
    protected open fun processParams(params: Array<out Params>): String? = null

    /**
     * A method that retrieves the [Local] version of the data. Any necessary arguments can be
     * pulled from the [params] array; alternately, the [processParams] method can be implemented
     * to pull the necessary arguments at the beginning of the [get] method.
     */
    protected abstract fun retrieveLocal(params: Array<out Params>): Local

    /**
     * A method that converts a [Local] entity to a [Domain] entity.
     */
    protected abstract fun localToDomain(local: Local): Domain

    /**
     * A method that is called when a cached [Domain] entity is obtained. This can be useful,
     * for example, in order to acquire the most recent result value after an exception is thrown.
     */
    protected open fun onCache(cached: Domain) { /* No op */
    }

    /**
     * A method that indicates whether a cached local value is valid. In most cases this will be
     * when that cached value is non-null; however, in other cases it may potentially be a
     * non-empty collection.
     */
    protected open fun isValid(cached: Domain): Boolean = cached != null

    /**
     * A method that fetches the [Remote] version of the data. Any necessary arguments can be
     * pulled from the [params] array; alternately, the [processParams] method can be implemented
     * to pull the necessary arguments at the beginning of the [get] method.
     */
    protected abstract fun fetchRemote(params: Array<out Params>): Remote

    /**
     * A method that converts a [remote] entity to a [Local] entity for caching purposes.
     */
    protected abstract fun remoteToLocal(remote: Remote): Local

    /**
     * A method that caches the [local] entity.
     */
    protected abstract fun cacheLocal(local: Local)

}
