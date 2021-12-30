package com.example.learn_new_language.profiles

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs

import com.example.learn_new_language.R
import com.example.learn_new_language.listTeacher.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class FragmentShowTeacherProfile : Fragment() {

    private lateinit var teacherName : TextView
    private lateinit var teacherInfo : TextView
    private lateinit var teacherEmail:TextView

    private val args:FragmentShowTeacherProfileArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readFireData()
        }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_show_teacher_profile, container, false)
        teacherName = view.findViewById(R.id.text_profileName)
        teacherInfo= view.findViewById(R.id.teacher_info)
        teacherEmail = view.findViewById(R.id.teacher_email)


        return view
    }




    private fun readFireData() {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Users") // .whereEqualTo("fullName",Firebase.auth.currentUser.uid)
            .whereEqualTo("email",args.email )
            .addSnapshotListener { value, error ->
               // Log.e("fromProfile" , "the value is ${value?.data}")
                    val result = value?.toObjects(User::class.java)?.get(0)
                    teacherName.text = result?.fullName
                    teacherInfo.text = result?.teacherExperience
                    teacherEmail.text = result?.email

                }

            }


    }




