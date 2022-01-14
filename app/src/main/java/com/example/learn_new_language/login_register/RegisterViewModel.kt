package com.example.learn_new_language.login_register


import androidx.lifecycle.ViewModel
import com.example.learn_new_language.Repository

class RegisterViewModel : ViewModel() {
    private val repo : Repository = Repository.getInstant()


     fun funOfNewRegister(fullName: String, email: String, password: String, phone: String, isAdmin:String, experience:String) {
        return repo.funOfNewRegister(fullName,email,password,phone,isAdmin,experience)
    }


}