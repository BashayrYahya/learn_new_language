package com.example.learn_new_language.login_register


import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.example.learn_new_language.MainActivity2
import com.example.learn_new_language.R
import com.example.learn_new_language.Repository


class LoginFragment : Fragment() {


    lateinit var navController: NavController
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginBtn: Button
    private lateinit var gotoRegister: TextView
    private lateinit var fireAuth: FirebaseAuth
    private val repo : Repository = Repository.getInstant()
    private val loginViewModel : LoginViewModel by lazy { ViewModelProvider(this)[LoginViewModel ::class.java ] }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        navController = findNavController()
        email = view.findViewById(R.id.loginEmail)
        password = view.findViewById(R.id.loginPassword)
        loginBtn = view.findViewById(R.id.loginBtn)
        gotoRegister = view.findViewById(R.id.gotoRegiste)
       fireAuth = FirebaseAuth.getInstance()


        // navigate user to register page
        gotoRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


        // login
        loginBtn.setOnClickListener {
            if (repo.validationOfLogin(email.text.toString(), password.text.toString())) {
               fireAuth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())

                        // if login was successful will move user to MainActivity2
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d(ContentValues.TAG, "login is successes")
//                            context?.let { it1 ->
//                                repo.checkLoginIfUserTeacherOrStudent(fireAuth.currentUser!!.uid ,
//                                    it1,
//                                    Activity())
//                            } // =it.user.uid
                            loginViewModel.checkUserLoginData(email.text.toString(),password.text.toString())
                            repo.addUserToDataBase (email.text.toString(), fireAuth.currentUser?.uid!!)
                            val intent = Intent(context, MainActivity2::class.java)
                            startActivity(intent)

                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "login is Failure", e)
                        Toast.makeText(
                            context, "make sure from your email or password", Toast.LENGTH_SHORT
                        ).show()
                    }

            }


        }


        return view

    }




    override fun onStart() {     // check if the user already signed
        super.onStart()
        if (fireAuth.currentUser != null) {
            val intent = (Intent(context, MainActivity2::class.java))
            startActivity(intent)

        }

    }

}



