package com.example.learn_new_language.setting

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.example.learn_new_language.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class SettingFragment : Fragment() {

    lateinit var spinnerLanguage :Spinner
    lateinit var locale: Locale
    lateinit var deleteAccount :Button
    lateinit var fireAuth: FirebaseAuth
    lateinit var firebase: Firebase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_setting, container, false)
//        fireAuth = FirebaseAuth.getInstance()
//        firebaseUser= fireAuth.currentUser!!
        deleteAccount  = view.findViewById(R.id.delete_button)
        spinnerLanguage = view.findViewById(R.id.spinner_language)

        deleteAccount.setOnClickListener {

             showAlertdialog ()

        }

        val languageList = ArrayList<String>()
        languageList.add("Select")
        languageList.add("English")
        languageList.add("Arabic")
        val adapterLanguage = ArrayAdapter(requireContext(),R.layout.support_simple_spinner_dropdown_item ,languageList)
        spinnerLanguage.adapter = adapterLanguage
        spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
              when (position){
                0  -> {

                }
                  1 -> selectLocal("en")
                  2 -> selectLocal("ar")


              }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }



        return view


    }


     fun selectLocal(language:String) {
         locale = Locale(language)
         val resources = resources
         val metrics = resources.displayMetrics
         val configuration = resources.configuration
         resources.updateConfiguration(configuration,metrics)

     }

    fun showAlertdialog (){
/* Creates a basic alertdialog
    *  with a message and title
    */
        val builder  = AlertDialog.Builder(requireContext())
        builder.setTitle("Are You Sure ?")
        builder.setMessage("deleting your account will completely remove your account from system and you wont able to access app" )
        // Set the button actions, all of them are optional
        builder.setPositiveButton("YES") { dialog, which ->
            // code to run when YES is pressed
            Log.e("Tag","hi iam in Yes")
            deleteUser ()



            builder.setNegativeButton("NO"){ dialog, which ->
                // code to run when NO is pressed
            }

            builder.setNeutralButton("Cancel"){dialog, which ->
                // code to run when Cancel is pressed
            }
            // create the dialog and show it
            val dialog = builder.create()
            dialog.show()

        }


    }


    private fun deleteUser() {
        val user: FirebaseUser = fireAuth.currentUser!!
        user.delete().addOnSuccessListener {
            Toast.makeText(context, "your account deleted", Toast.LENGTH_LONG).show()
            val action = SettingFragmentDirections.actionSettingFragmentToRegisterFragment2()
            findNavController().navigate(action)

        }?.addOnFailureListener {
            Toast.makeText(context, "something goes wrong", Toast.LENGTH_LONG).show()
            Log.e("Tag","something goes wrong")

        }
    }

}