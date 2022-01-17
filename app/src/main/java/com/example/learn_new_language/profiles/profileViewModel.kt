package com.example.learn_new_language.profiles

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learn_new_language.Repository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel () {

    private val repo : Repository = Repository.getInstant()

    fun uploadPhotoToFirebaseStorage(imageUri : Uri) {
        viewModelScope.launch {
            repo.uploadPhotoToFirebaseStorage(imageUri)
        }
    }

//      suspend fun addRating (teacherId:String, ratingTeacher: RatingDataClass, studentId:String ) {
//
//             repo.addRating(teacherId, ratingTeacher, studentId)
//
//     }

//    fun getPhotoFromStorage(): LiveData<Uri> {
//            return repo.getPhotoFromStorage()
//    }


}