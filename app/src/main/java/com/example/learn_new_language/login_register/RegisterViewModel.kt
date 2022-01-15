package com.example.learn_new_language.login_register


import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.learn_new_language.Repository
import com.example.learn_new_language.Repository.Companion.fireAuth

class RegisterViewModel : ViewModel() {
    private val repo : Repository = Repository.getInstant()


     fun funOfNewRegister(
         fullName: String, email: String, password: String,
         phone: String, isAdmin:String, experience: String,uid:String, context:Context
     ) {
        return repo.funOfNewRegister(fullName,email,password,phone,isAdmin,experience, uid,context)
    }


}