package com.example.pollista.DataAccess.Util

import android.net.Uri
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.suspendCancellableCoroutine

object FirebaseStorageUtil {

    suspend fun uploadImageToFirebase(
        reference: StorageReference,
        imageUri: Uri,
        onException: (Exception) -> Unit
    ): String? = suspendCancellableCoroutine { continuation ->
        val uploadTask = reference.putFile(imageUri)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    continuation.cancel(it) // cancel the coroutine with the exception
                    onException(it) // call the exception handler
                }
            }
            reference.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resumeWith(Result.success(task.result.toString()))
            } else {
                task.exception?.let {
                    continuation.cancel(it) // cancel the coroutine with the exception
                    onException(it) // call the exception handler
                }
            }
        }

        continuation.invokeOnCancellation {
            uploadTask.cancel()
        }
    }
}