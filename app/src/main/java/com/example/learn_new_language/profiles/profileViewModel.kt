package com.example.learn_new_language.profiles

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.learn_new_language.Repository
import de.hdodenhof.circleimageview.CircleImageView

class ProfileViewModel : ViewModel () {

    private val repo : Repository = Repository.getInstant()

    fun uploadPhotoToFirebaseStorage(imageUri : Uri) {
        repo.uploadPhotoToFirebaseStorage(imageUri)
    }

    fun getPhotoFromFirebaseStorage(imageView: CircleImageView){
        repo.getPhotoFromFirebaseStorage(imageView)
    }

}