package com.example.learn_new_language.login_register

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
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.example.learn_new_language.MainActivity2
import com.example.learn_new_language.R
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class LoginFragment : Fragment() {


    lateinit var navController: NavController
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginBtn: Button
    private lateinit var gotoRegister: TextView
    private var valid: Boolean = true
    private lateinit var fireAuth: FirebaseAuth
    lateinit var fireStore: FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fireAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()


    }


    private fun checkIfUserTeacherOrStudent(uid: String) {
        val df: DocumentReference =
            fireStore.collection("Users")
                .document(uid)

        // check if the user is Student
        df.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.getString("isAdmen") != "1") {

                val intent = Intent(context, MainActivity2::class.java)
                startActivity(intent)


                // check if the user is teacher
            } else if (documentSnapshot.getString("isAdmen") != "0") {
                val intent = Intent(context, MainActivity2::class.java)
                startActivity(intent)


            }
        }
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

        gotoRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)

        }


        loginBtn.setOnClickListener {

            if (checkField(email, password)) {
                fireAuth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())

                    .addOnCompleteListener {

                       if (it.isSuccessful) {
                            Log.d(ContentValues.TAG, "login is successes")
                            checkIfUserTeacherOrStudent(fireAuth.currentUser!!.uid)  // =it.user.uid


                       }

                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "login is Failure", e)
                        Toast.makeText(context, "make sure from your email or password", Toast.LENGTH_SHORT
                        ).show()
                    }

            }


        }




        return view

    }


    private fun checkField(email: EditText, password: EditText): Boolean {
        if (email.text.toString().isEmpty() || password.text.toString().isEmpty()) {
            email.error = "you should fill your email "
            password.error = "you should fill your password"
            valid = false
        } else {
            valid = true
        }
        return valid
    }


    override fun onStart() {     // check if the user already signed
        super.onStart()
        if (fireAuth.currentUser != null) {
            val intent = (Intent(context, MainActivity2::class.java))
            startActivity(intent)

        }

    }


}



