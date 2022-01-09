package com.example.learn_new_language.login_register



import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.RadioGroup
import androidx.lifecycle.ViewModelProvider
import com.example.learn_new_language.R
import com.google.android.material.textfield.TextInputLayout

private const val TAG = "RegisterFragment"

class RegisterFragment : Fragment() {

    private val registerViewModel : RegisterViewModel by lazy { ViewModelProvider(this)[RegisterViewModel ::class.java ] }
    private lateinit var fullName: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var phone: EditText
    private lateinit var registerBtn: Button
    private lateinit var goToLogin: Button
    private lateinit var experienceInput: TextInputLayout
    lateinit var experienceEditText: EditText
    private lateinit var fireAuth: FirebaseAuth
    lateinit var fireStore: FirebaseFirestore
    private lateinit var radioGroup: RadioGroup
    lateinit var isTeacher: RadioButton
    lateinit var isStudent: RadioButton

    private var isAdmin = ""

    private var experience = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseAuth.getInstance().also { fireAuth = it }
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
        radioGroup = view.findViewById(R.id.radioGroup)
        experienceEditText = view.findViewById(R.id.experienceEditText)
        experienceInput = view.findViewById(R.id.experienceInput)









        registerBtn.setOnClickListener{
            experience = experienceEditText.text.toString()
            registerViewModel.funOfNewRegister(fullName.text.toString(),email.text.toString(),password.text.toString(),phone.text.toString(),isAdmin,experience,requireContext())
        }

        goToLogin.setOnClickListener {

            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

        }

        radioGroup.setOnCheckedChangeListener { _, checkedId -> // checkedId is the RadioButton selected

            when(checkedId){
                isTeacher.id ->{
                    Log.d(TAG, "onCreateView: isAdmin 1")
                    isAdmin = "1"
                }


                isStudent.id ->{
                    isAdmin = "0"

                    Log.d(TAG, "onCreateView: isAdmin 0")}
            }

            experienceInput.visibility = if (checkedId == isTeacher.id) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }



        return view

    }













}


