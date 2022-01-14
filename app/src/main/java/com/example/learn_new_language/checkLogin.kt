package com.example.learn_new_language


// this is a fun for test
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

