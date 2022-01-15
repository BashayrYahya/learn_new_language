package com.example.learn_new_language.listTeacher

import com.example.learn_new_language.profiles.RatingDataClass


data class User(
    var fullName : String ="",
    var teacherExperience: String = "" ,
    var phone: String = "",
    var email:String = "" ,
    var uid : String = "",
    var isAdmin:String = "",
    var rating :List<RatingDataClass> = listOf()
)