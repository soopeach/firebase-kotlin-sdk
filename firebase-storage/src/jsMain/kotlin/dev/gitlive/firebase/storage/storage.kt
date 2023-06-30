/*
 * Copyright (c) 2020 GitLive Ltd.  Use of this source code is governed by the Apache 2.0 license.
 */

package dev.gitlive.firebase.storage

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseApp
import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.firebase


actual val Firebase.storage get() =
    rethrow { dev.gitlive.firebase.firestore; FirebaseStorage(firebase.storage()) }

actual fun Firebase.storage(app: FirebaseApp) =
    rethrow { dev.gitlive.firebase.firestore; FirebaseStorage(firebase.app().storage()) }

actual class FirebaseStorage(val js: firebase.storage.Storage) {

    actual fun getMaxOperationRetryTimeMillis(): Long = js.maxOperationRetryTime.toLong()

    actual fun getMaxUploadRetryTimeMillis(): Long = js.maxUploadRetryTime.toLong()

    actual fun setMaxOperationRetryTimeMillis(maxOperationRetryTimeMillis: Long) {
        js.setMaxOperationRetryTime(maxOperationRetryTimeMillis.toInt())
    }

    actual fun setMaxUploadRetryTimeMillis(maxUploadRetryTimeMillis: Long) {
        js.setMaxUploadRetryTime(maxUploadRetryTimeMillis.toInt())
    }

    actual fun useEmulator(host: String, port: Int) {
        js.useEmulator(host, port)
    }

}

inline fun <T, R> T.rethrow(function: T.() -> R): R = dev.gitlive.firebase.storage.rethrow { function() }

inline fun <R> rethrow(function: () -> R): R {
    try {
        return function()
    } catch (e: Exception) {
        throw e
    } catch(e: dynamic) {
        throw StorageException(e.code as String, e)
    }
}

actual open class StorageException(code: String, cause: Throwable) :
    FirebaseException(code, cause)
