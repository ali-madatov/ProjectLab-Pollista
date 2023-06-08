package com.example.pollista.DataAccess.Util

import android.net.Uri
import com.google.firebase.storage.StorageReference

interface StorageUtilInterface {
    suspend fun uploadImageToFirebase(
        reference: StorageReference,
        imageUri: Uri?,
        onException: (Exception) -> Unit
    ): String?
}