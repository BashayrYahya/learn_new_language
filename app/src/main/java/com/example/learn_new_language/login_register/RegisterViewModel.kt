package com.example.learn_new_language.login_register


import android.content.Context

import androidx.lifecycle.ViewModel
import com.example.learn_new_language.Repository

class RegisterViewModel : ViewModel() {
    private val repo : Repository = Repository.getInstant()


     fun funOfNewRegister(fullName: String, email: String, password: String, phone: String,isAdmin:String,experanse:String, context: Context) {
        return repo.funOfNewRegister(fullName,email,password,phone,isAdmin,experanse,context)
    }


}