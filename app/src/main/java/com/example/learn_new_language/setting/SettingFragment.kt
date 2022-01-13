package com.example.learn_new_language.setting

import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.example.learn_new_language.MainActivity
import com.example.learn_new_language.R
import com.example.learn_new_language.login_register.LoginFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

 const val PREF_CHANGE_LANG_KEY = "PREF_CHANGE_LANG_KEY"
class SettingFragment : Fragment() {

    lateinit var spinnerLanguage: Spinner
    lateinit var deleteAccount: Button
    lateinit var fireAuth: FirebaseAuth
    lateinit var firebase: Firebase
    lateinit var shareText : TextView
    lateinit var logoutText :TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate()


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        deleteAccount = view.findViewById(R.id.delete_button)
        spinnerLanguage = view.findViewById(R.id.spinner_language)
        shareText= view.findViewById(R.id.share_textview)
        logoutText=view.findViewById(R.id.log_out)
        FirebaseAuth.getInstance().also { fireAuth = it }

        deleteAccount.setOnClickListener {
            showDeleteUserDialog()
        }

        logoutText.setOnClickListener {
            fireAuth.signOut()
            findNavController().navigate(R.id.action_settingFragment_to_loginFragment2)
        }

        shareText.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getShareReport())
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    "Share Report"
                )
            }.also {
                val chooserIntent =
                    Intent.createChooser(it, " send_report")
                startActivity(chooserIntent)
            }

        }

        showChangeLang()



        return view

    }



    // spinner of multi languages , users can change tha language of app from here
    // no need to change all mobile language (shared preference)
    private fun showChangeLang () {
        val languageList = ArrayList<String>()
        languageList.add("Select")
        languageList.add("English")
        languageList.add("Arabic")
        val adapterLanguage = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            languageList
        )
        spinnerLanguage.adapter = adapterLanguage
        spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {

                    }
                    1 -> setLocal("en")

                    2 -> setLocal("ar")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

   // create the dialog of delete user from fireStore account and show it
    private fun showDeleteUserDialog() {
        val builder = AlertDialog.Builder(context).apply {
            setTitle("Are You Sure ?")
            setMessage("deleting your account will completely remove your account from system and you wont able to access app")
        }
        // Set the button actions, all of them are optional
        builder.setPositiveButton("YES") { dialog, which ->
            // code to run when YES is pressed
            Log.e("Tag", "hi iam in Yes")
            //deleteUser()
            val user: FirebaseUser = fireAuth.currentUser!!
            user.delete().addOnSuccessListener {
                Toast.makeText(context, "your account deleted", Toast.LENGTH_LONG).show()
                val action = SettingFragmentDirections.actionSettingFragmentToRegisterFragment2()
                findNavController().navigate(action)

            }.addOnFailureListener {
                Toast.makeText(context, "something goes wrong", Toast.LENGTH_LONG).show()
                Log.e("Tag", "something goes wrong", it)

            }

            // create the dialog and show it
            builder.create().also {
                it.show()
            }

            builder.setNegativeButton("NO") { dialog, which ->
                // code to run when NO is pressed

            }
            builder.create().also {
                it.show()
            }
            builder.setNeutralButton("Cancel") { dialog, which ->
                // code to run when Cancel is pressed

            }

        }

        // create the dialog and show it
        val dialog = builder.create()
        dialog.show()
    }


        //change languages
        private fun setLocal(language: String){
            val locale = Locale(language)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale

            context?.resources?.updateConfiguration(config,context?.resources?.displayMetrics)

            getDefaultSharedPreferences(context).edit()
                .putString(PREF_CHANGE_LANG_KEY,language)
                .apply()
        }


     //change languages
    private fun loadLocate(){
        val pref = getDefaultSharedPreferences(context)
        val language = pref.getString(PREF_CHANGE_LANG_KEY,"")!!
        setLocal(language)

    }

    // the message of sharing icon
    private fun getShareReport():String{
        return "Hey my friend ! I want  share you an amazing App  that help us to learn Arabic language in grateful ways"

    }

}



