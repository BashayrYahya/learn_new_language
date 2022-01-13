package com.example.learn_new_language

object  checkLogin {

    var valid :Boolean = true
    fun validationOfLogin ( email: String,
    password: String) :Boolean {
       if (email.isEmpty() || password.isEmpty()){
           return false
       }
        return valid
    }


}

