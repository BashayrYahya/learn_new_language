package com.example.learn_new_language.login_register


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.example.learn_new_language.MainActivity2

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.RadioGroup
import com.example.learn_new_language.R
import com.google.android.material.textfield.TextInputLayout


private const val TAG = "RegisterFragment"
class RegisterFragment : Fragment() {


    private lateinit var fullName: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var phone: EditText
    private lateinit var registerBtn: Button
    private lateinit var goToLogin: Button
    private lateinit var experienceInput: TextInputLayout
    private lateinit var experienceEditText: EditText
    private var valid = true
    lateinit var fireAuth: FirebaseAuth
    lateinit var fireStore: FirebaseFirestore
    lateinit var radioGroup: RadioGroup
    lateinit var isTeacher: RadioButton
    lateinit var isStudent: RadioButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fireAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()



    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        fullName = view.findViewById(R.id.registerName)
        email = view.findViewById(R.id.registerEmail)
        password = view.findViewById(R.id.password)
        phone = view.findViewById(R.id.phoneNumber)
        registerBtn = view.findViewById(R.id.registerBtn)
        goToLogin = view.findViewById(R.id.logintext)
        isTeacher = view.findViewById(R.id.isTeacher)
        isStudent = view.findViewById(R.id.isStudent)
        radioGroup= view.findViewById(R.id.radioGroup)
        experienceEditText = view.findViewById(R.id.experienceEditText)
        experienceInput = view.findViewById(R.id.experienceInput)




        registerBtn.setOnClickListener(funOfNewRegister())

        goToLogin.setOnClickListener {
         val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slide_to_left)
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

        }

        radioGroup.setOnCheckedChangeListener { _, checkedId -> // checkedId is the RadioButton selected
            experienceInput.visibility = if (checkedId == isTeacher.id) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }



        return view

    }

    private fun funOfNewRegister(): (v: View) -> Unit = {


        if (checkField(fullName, email, password, phone)) { // new register
            fireAuth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {

                        val user: FirebaseUser = fireAuth.currentUser!!
                        //val user = User(id = task.user!!.uid, email = email, fullName = fullName)

                        // if register successes user's information
                        Log.d("TAG", "createUserWithEmail:success")
                        Toast.makeText(context, "account created successfully", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(context, MainActivity2::class.java)
                        startActivity(intent)

                        val userInfo = hashMapOf(
                            "fullName" to fullName.text.toString(),
                            "email" to email.text.toString(),
                            "phone" to phone.text.toString(),
                            "teacherExperience" to experienceEditText.text.toString()
                        )

                        // specify if  the user admen

                        if (isTeacher.isChecked)  {
                            userInfo["isAdmin"] = "1"




                        } else if (isStudent.isChecked) {
                            userInfo["isAdmin"] = "0"
                            val intent = Intent(context, MainActivity2::class.java)
                            startActivity(intent)
                        }

                        //save users data on fireBaseStore
                        fireStore.collection("Users").document(user.uid)
                            .set(userInfo).addOnSuccessListener {
                                Log.e(TAG, "done")

                            }.addOnFailureListener {
                                Log.e(TAG, "something gone wrong", it)
                            }


                    } else {
                        // If register failed, display a message to the user.
                        Log.w("TAG", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            context,
                            "Authentication failed ,${task.exception}.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
        }


    }




    private fun checkField(
        fullName: EditText, email: EditText,
        password: EditText, phone: EditText
    ): Boolean {
        if (fullName.text.toString().isEmpty() || (email.text.toString().isEmpty())
            || (password.text.toString().isEmpty()) || (phone.text.toString().isEmpty())
        ) {
            fullName.error = "you should fill your name"
            email.error = "you should fill your email"
            password.error = "ou should fill your password"
            phone.error = "ou should fill your phone"

            valid = false
        } else {
            valid = true
        }
        return valid
    }







}


