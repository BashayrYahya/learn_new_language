package com.example.learn_new_language.profiles

import android.net.Uri
import android.util.Log
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class Repository  {


    companion object{
       fun getInstant () : Repository = Repository()
        private val fireStore = Firebase.firestore
        private val auth :FirebaseAuth = FirebaseAuth.getInstance()
        private val currentUser = auth.currentUser?.uid
    }


    fun uploadPhotoToFirebaseStorage (imgURI: Uri){
        val imageReference = FirebaseStorage.getInstance().getReference("/photo/$currentUser")
        imageReference.putFile(imgURI)
            .addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener{
                    saveImageToFireStore(it.toString())
                }.addOnFailureListener{
                    Log.d("","there is an error in upload pics")
                }
            }
    }

     private fun saveImageToFireStore(profileImageUri :String){
         fireStore.collection("Users")
             .document(auth.currentUser!!.uid)
             .update("imgProfile",profileImageUri)
     }


    fun getPhotoFromFirebaseStorage (image : CircleImageView, userUri: String?= auth.currentUser?.uid){
         val imageUri = FirebaseStorage.getInstance().getReference("/photos/ $userUri")
             .downloadUrl
        imageUri.addOnSuccessListener {
            image.load(it)
        }

    }




}
