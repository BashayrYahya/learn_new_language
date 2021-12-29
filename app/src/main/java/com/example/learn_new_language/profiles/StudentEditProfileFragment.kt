package com.example.learn_new_language.profiles

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learn_new_language.MainActivity2
import com.example.learn_new_language.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception

private const val TAG = "StudentEditProfileFragm"
private const val PICK_PHOTO = 0


class StudentEditProfileFragment : Fragment() {
    private val userProfileViewModel :ProfileViewModel by lazy { ViewModelProvider(this)[ProfileViewModel ::class.java ] }
    private lateinit var imgProfile: ImageView
    private lateinit var fullName: EditText
    private lateinit var phoneNumEdit: EditText
    private lateinit var updateBtn: Button
    private lateinit var emailEdit: EditText
    private lateinit var teacherExperienceEdit : EditText
    private lateinit var imgUri : Uri



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readFireStoreData()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =
            inflater.inflate(R.layout.fragment_student_edit_profile_fragment, container, false)

        imgProfile = view.findViewById(R.id.photoProfile)
        fullName= view.findViewById(R.id.fullNameEdit)
        phoneNumEdit=view.findViewById(R.id.phoneNumEdit)
        emailEdit = view.findViewById(R.id.emailEdit)
        teacherExperienceEdit =view.findViewById(R.id.teacherExperienceEdit)
        updateBtn=view.findViewById(R.id.updateBtn)


        //open studio of user to take pic profile
        imgProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type ="*/*"
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_PHOTO)

        }


        updateBtn.setOnClickListener {
            val studentFullName =fullName.text.toString()
            val phoneNumEdit = phoneNumEdit.text.toString()
            val emailEdit = emailEdit.text.toString()
            val teacherExperienceEdit = teacherExperienceEdit.text.toString()

            saveFireStore(studentFullName, phoneNumEdit,emailEdit,teacherExperienceEdit)

        }


        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PHOTO && requestCode ==RESULT_OK){
            if (data!=null){
                imgUri = data.data!!
                try {
                    val bitmap :Bitmap = MediaStore.Images.Media.getBitmap(
                        activity?.contentResolver, imgUri
                    )
                    imgProfile.setImageBitmap(bitmap)
                    userProfileViewModel.uploadPhotoToFirebaseStorage(imgUri)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }


    private fun saveFireStore(fullName: String, emailEdit: String, phoneNum:String, teacherExperience:String) {

        val user: MutableMap<String, Any> = HashMap()

        user["fullName"] = fullName
        user["phone"] = phoneNum
        user["email"] = emailEdit
        user["teacherExperience"] = teacherExperience

         FirebaseFirestore.getInstance().collection("Users")
            .document(Firebase.auth.currentUser?.uid!!).set(user).addOnSuccessListener {
                 val intent = Intent(context, MainActivity2::class.java)
                 startActivity(intent)
             }.addOnFailureListener {
                 Log.e(TAG, "saveFireStore: error", it)
             }



    }

// get information of users from fireStore to edit
private fun readFireStoreData() {
        val fireStore = FirebaseFirestore.getInstance()
    fireStore.collection("Users")
            .document(Firebase.auth.currentUser?.uid!!)
            .addSnapshotListener { value, error ->
                Log.e("fromProfile" , "the value is ${value?.data}")
                 value?.data?.forEach {
                    when (it.key) {
                        "fullName" -> fullName.setText(it.value.toString())
                        "phone" -> phoneNumEdit.setText(it.value.toString())
                            "email" ->emailEdit.setText(it.value.toString())
                        "teacherExperience"-> teacherExperienceEdit.setText(it.value.toString())




                    }

                }

            }


    }


}








