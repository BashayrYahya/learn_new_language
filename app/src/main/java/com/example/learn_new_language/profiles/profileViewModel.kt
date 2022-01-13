package com.example.learn_new_language.profiles

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learn_new_language.Repository
import com.google.firebase.firestore.auth.User
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel () {

    private val repo : Repository = Repository.getInstant()

    fun uploadPhotoToFirebaseStorage(imageUri : Uri) {
        viewModelScope.launch {
            repo.uploadPhotoToFirebaseStorage(imageUri)
        }
    }

    fun getPhotoFromStorage(): LiveData<Uri> {
            return repo.getPhotoFromStorage()
    }


}