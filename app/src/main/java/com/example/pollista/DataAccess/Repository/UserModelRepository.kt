package com.example.pollista.DataAccess.Repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pollista.DataAccess.Model.UserModel
import com.example.pollista.DataAccess.Util.FirebaseStorageUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserModelRepository {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val firestore = Firebase.firestore
    private val firebaseStorage = Firebase.storage
    private val _user = MutableLiveData<UserModel>()
    val user: LiveData<UserModel> get() = _user

    //constants
    private companion object{
        private const val TAG = "USER_MODEL_REPOSITORY"
        private const val PROFILE_PHOTO_NAME = "profilePhoto"
        private const val USERNAME_FIELD_NAME = "username"
        private const val EMAIL_FIELD_NAME = "email"
        private const val NAME_FIELD_NAME = "name"
        private const val BIO_FIELD_NAME = "bio"
        private const val PHONE_FIELD_NAME = "phoneNumber"
        private const val PHOTO_FIELD_NAME = "photoUrl"
    }

    suspend fun addUserModel(userModel: UserModel) = withContext(Dispatchers.IO) {
        // Get user doc reference in Firestore
        val docRef = firestore.collection("users").document(userModel.userID)
        docRef.set(userModel)

        //Update the Firestore document if the user has profile photo
        userModel.photoUrl?.let {
            val profilePhotoUrl = FirebaseStorageUtil
                .uploadImageToFirebase(
                    firebaseStorage.reference
                        .child("images/${userModel.userID}/${PROFILE_PHOTO_NAME}"),
                    Uri.parse(it),
                    onException = { exception ->
                        Log.d(TAG, "Error on saving image $it to Firebase Storage", exception)
                    }
                )
            docRef.update(mapOf("profilePhoto" to profilePhotoUrl))
        }
    }

    fun fetchUser() {
        val docRef = firestore.collection("users").document(currentUser!!.uid)
        docRef.addSnapshotListener { snapshot, e ->
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

    suspend fun updateUserModel(username: String, email: String, name: String, bio: String, phone: String, profilePhotoUrl: String) = withContext(Dispatchers.IO) {
        val docRef = firestore.collection("users").document(currentUser!!.uid)

        profilePhotoUrl.let {
            val newProfilePhotoUrl = FirebaseStorageUtil
                .uploadImageToFirebase(
                    firebaseStorage.reference
                        .child("images/${currentUser!!.uid}/${PROFILE_PHOTO_NAME}"),
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
            docRef.update(updates).addOnSuccessListener {
                Log.d(TAG, "User data successfully updated!")
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error updating document", e)
            }
        }
    }

    suspend fun updateUserModel(username: String, email: String, name: String, bio: String, phone: String) = withContext(Dispatchers.IO) {
        val docRef = firestore.collection("users").document(currentUser!!.uid)

        val updates = hashMapOf<String, Any>(
            USERNAME_FIELD_NAME to username,
            EMAIL_FIELD_NAME to email,
            NAME_FIELD_NAME to name,
            BIO_FIELD_NAME to bio,
            PHONE_FIELD_NAME to phone
        )
        docRef.update(updates).addOnSuccessListener {
            Log.d(TAG, "User data successfully updated!")
        }.addOnFailureListener { e ->
            Log.w(TAG, "Error updating document", e)
        }
    }

    suspend fun notifyPostAddedChange() = withContext(Dispatchers.IO) {
        val docRef = firestore.collection("users").document(currentUser!!.uid)
        Log.d(TAG, "Updating posts number")
        docRef.update("postsNumber", FieldValue.increment(1))
            .addOnSuccessListener {
                Log.d(TAG, "User data successfully updated!")
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error updating document", e)
            }
    }

}