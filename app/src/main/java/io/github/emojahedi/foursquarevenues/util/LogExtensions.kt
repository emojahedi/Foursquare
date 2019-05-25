package io.github.emojahedi.foursquarevenues.util

import android.util.Log
import io.github.emojahedi.foursquarevenues.BuildConfig

// from https://gist.github.com/paolop/0bd59e49b33d18d6089fb1bf5488e212

/* Convenient wrappers over Android Log.* static methods */

/** Wrapper over [Log.i] */
inline fun <reified T> T.logi(
    message: String,
    tr: Throwable? = null,
    onlyInDebugMode: Boolean = true
) =
    log(onlyInDebugMode) {
        if (tr == null) {
            Log.i(getTagFromClass(), message)
        } else {
            Log.i(getTagFromClass(), message, tr)
        }
    }

/** Lazy wrapper over [Log.i] */
inline fun <reified T> T.logi(
    onlyInDebugMode: Boolean = true,
    lazyMessage: () -> String
) {
    log(onlyInDebugMode) { Log.i(getTagFromClass(), lazyMessage.invoke()) }
}

/** Wrapper over [Log.i] */
inline fun <reified T> T.logd(
    message: String,
    tr: Throwable? = null,
    onlyInDebugMode: Boolean = true
) =
    log(onlyInDebugMode) {
        if (tr == null) {
            Log.i(getTagFromClass(), message)
        } else {
            Log.i(getTagFromClass(), message, tr)
        }
    }

/** Lazy wrapper over [Log.i] */
inline fun <reified T> T.logd(
    onlyInDebugMode: Boolean = true,
    lazyMessage: () -> String
) {
    log(onlyInDebugMode) { Log.d(getTagFromClass(), lazyMessage.invoke()) }
}

inline fun log(onlyInDebugMode: Boolean, logger: () -> Unit) {
    when {
        onlyInDebugMode && BuildConfig.DEBUG -> logger()
        !onlyInDebugMode -> logger()
    }
}

inline fun <reified T> T.getTagFromClass(): String =
    if (T::class.java.simpleName.isNotBlank()) {
        T::class.java.simpleName
    } else {
        val parts = T::class.java.name.split('.')
        parts[parts.size - 1]
    }
