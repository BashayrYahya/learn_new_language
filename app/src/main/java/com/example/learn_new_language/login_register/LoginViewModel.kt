package com.example.learn_new_language.login_register

import androidx.lifecycle.ViewModel
import com.example.learn_new_language.Repository

class LoginViewModel : ViewModel (){
    private val repo : Repository = Repository.getInstant()



    fun checkUserLoginData(
        email:String,
        password: String

    ){
        return repo.checkUserLoginData(email,password)
    }







}