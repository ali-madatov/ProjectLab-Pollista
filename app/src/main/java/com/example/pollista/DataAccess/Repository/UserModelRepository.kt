package com.example.pollista.DataAccess.Repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pollista.DataAccess.Model.UserModel
import com.example.pollista.DataAccess.Util.FirebaseStorageUtil
import com.example.pollista.DataAccess.Util.StorageUtilInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class UserModelRepository(private val userDocument: DocumentReference,
                               private val storageReference: StorageReference,
                               private val userID: String,
                                private val utilInterface: StorageUtilInterface,
                               private val coroutineDispatcher: CoroutineDispatcher
) {

    private val _user = MutableLiveData<UserModel>()
    val user: LiveData<UserModel> get() = _user

    constructor(): this(Firebase.firestore.collection("users")
        .document(FirebaseAuth.getInstance().currentUser!!.uid),
        Firebase.storage.reference,
        FirebaseAuth.getInstance().currentUser!!.uid,
        FirebaseStorageUtil,
        Dispatchers.IO)

    //constants
    private companion object{
        private const val TAG = "USER_MODEL_REPOSITORY"
        private const val PROFILE_PHOTO_NAME = "profilePhoto"
        const val USERNAME_FIELD_NAME = "username"
        private const val EMAIL_FIELD_NAME = "email"
        private const val NAME_FIELD_NAME = "name"
        private const val BIO_FIELD_NAME = "bio"
        private const val PHONE_FIELD_NAME = "phoneNumber"
        private const val PHOTO_FIELD_NAME = "photoUrl"
    }

    open suspend fun addUserModel(userModel: UserModel) = withContext(coroutineDispatcher) {
        userDocument.set(userModel)

        //Update the Firestore document if the user has profile photo
        userModel.photoUrl?.let {
            val profilePhotoUrl = utilInterface
                .uploadImageToFirebase(
                    storageReference
                        .child("images/${userModel.userID}/${PROFILE_PHOTO_NAME}"),
                    Uri.parse(it),
                    onException = { exception ->
                        Log.d(TAG, "Error on saving image $it to Firebase Storage", exception)
                    }
                )
            userDocument.update(mapOf("profilePhoto" to profilePhotoUrl))
        }
    }

    open fun fetchUser() {
        userDocument.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val user = snapshot.toObject(UserModel::class.java)
                _user.postValue(user!!)
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }

    open suspend fun updateUserModel(username: String, email: String, name: String, bio: String, phone: String, profilePhotoUrl: String) = withContext(coroutineDispatcher) {

        profilePhotoUrl.let {
            val newProfilePhotoUrl = utilInterface
                .uploadImageToFirebase(
                    storageReference
                        .child("images/${userID}/${PROFILE_PHOTO_NAME}"),
                    Uri.parse(it),
                    onException = { exception ->
                        Log.d(TAG, "Error on saving image $it to Firebase Storage", exception)
                    }
                )

            // Update the Firestore document
            val updates = hashMapOf<String, Any>(
                USERNAME_FIELD_NAME to username,
                EMAIL_FIELD_NAME to email,
                NAME_FIELD_NAME to name,
                BIO_FIELD_NAME to bio,
                PHONE_FIELD_NAME to phone,
                PHOTO_FIELD_NAME to newProfilePhotoUrl.orEmpty()
            )
            userDocument.update(updates).addOnSuccessListener {
                Log.d(TAG, "User data successfully updated!")
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error updating document", e)
            }
        }
    }

    open suspend fun updateUserModel(username: String, email: String, name: String, bio: String, phone: String) = withContext(coroutineDispatcher) {

        val updates = hashMapOf<String, Any>(
            USERNAME_FIELD_NAME to username,
            EMAIL_FIELD_NAME to email,
            NAME_FIELD_NAME to name,
            BIO_FIELD_NAME to bio,
            PHONE_FIELD_NAME to phone
        )
        userDocument.update(updates).addOnSuccessListener {
            Log.d(TAG, "User data successfully updated!")
        }.addOnFailureListener { e ->
            Log.w(TAG, "Error updating document", e)
        }
    }

    open suspend fun notifyPostAddedChange() = withContext(coroutineDispatcher) {

        Log.d(TAG, "Updating posts number")
        userDocument.update("postsNumber", FieldValue.increment(1))
            .addOnSuccessListener {
                Log.d(TAG, "User data successfully updated!")
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error updating document", e)
            }
    }

}