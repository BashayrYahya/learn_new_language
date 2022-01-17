package com.example.learn_new_language

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.learn_new_language.listTeacher.User
import com.example.learn_new_language.login_register.RegisterFragment
import com.example.learn_new_language.profiles.FragmentShowTeacherProfile
import com.example.learn_new_language.profiles.RatingDataClass
import com.example.learn_new_language.videoCall.uid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

const val REPOSITORY_TAG = "REPOSITORY_TAG"

class Repository {


    companion object {
        fun getInstant(): Repository = Repository()
        val fireStore = FirebaseFirestore.getInstance()
        private val auth: FirebaseAuth = FirebaseAuth.getInstance()
        private val currentUser = auth.currentUser?.uid
        private var valid: Boolean = true
        val fireAuth = FirebaseAuth.getInstance()
        lateinit var databaseRef: DatabaseReference
        val registerFragment = RegisterFragment()
        val showTeacherProfile: FragmentShowTeacherProfile = FragmentShowTeacherProfile()
        var rating = ""
        var teacherUid = ""


    }


    suspend fun uploadPhotoToFirebaseStorage(imgURI: Uri) {
        val imageRef = FirebaseStorage.getInstance().getReference("/photos/$currentUser")

        val o = imageRef.putFile(imgURI).await()

        if (o.task.isComplete) {
            val j = o.storage.downloadUrl.await()

            fireStore.collection("users")
                .document(auth.currentUser!!.uid)
                .update("profileImage", j.toString())


        }
    }

    fun getPhotoFromStorage(userUrl: String? = auth.currentUser?.uid): LiveData<Uri> {
        val imageUrl = FirebaseStorage.getInstance().getReference("/photos/$userUrl").downloadUrl
        fireStore.collection("users")
        val uriLiveData: MutableLiveData<Uri> = MutableLiveData()
        imageUrl.addOnSuccessListener {
            uriLiveData.value = it
        }.addOnFailureListener {
            Log.e(REPOSITORY_TAG, " fail", it)
        }
        return uriLiveData
    }


//    fun getPhotoFromFirebaseStorage (image : CircleImageView, userUri: String?= auth.currentUser?.uid){
//         val imageUri = FirebaseStorage.getInstance().getReference("/photos/ $userUri")
//             .downloadUrl
//        imageUri.addOnSuccessListener {
//            image.load(it)
//        }
//
//    }

    // checked if the all editText is valid
    fun validationOfLogin(email: String, password: String): Boolean {
        valid = !(email.isEmpty() || password.isEmpty())
        return valid
    }

//    fun checkUserLoginData(email: String, password: String) {
//
//        val userClassData = User()
//        userClassData.email = email
//
//
//    }

//     fun checkLoginIfUserTeacherOrStudent(uid: String, context: Context, activity: Activity) {
//        val df: DocumentReference =
//            fireStore.collection("Users")
//                .document(uid)
//
//        //  if the user is Student  intent him to fragments
//        df.get().addOnSuccessListener { documentSnapshot ->
//            if (documentSnapshot.getString("isAdmen") != "1") {
//
//                val intent = Intent(context, MainActivity2::class.java)
//                activity.startActivity(intent)
//
//
//
//                // check if the user is teacher
//            } else if (documentSnapshot.getString("isAdmen") != "0") {
//                val intent = Intent(context, MainActivity2::class.java)
//               activity.startActivity(intent)
//
//
//            }
//        }
//    }


    fun addUserToDataBase(email: String, uid: String) {
        val userDataClass = User()
        userDataClass.uid = uid
        userDataClass.email = email
        databaseRef = FirebaseDatabase.getInstance().getReference()
        databaseRef.child("Users").child(uid)
            .setValue(User(email, uid))

    }


    //  proses of new registration users
    fun funOfNewRegister(
        fullName: String, email: String, password: String,
        phone: String, isAdmin: String, experience: String, uid: String, context: Context
    ) {

        val userClassData = User()


        if (checkValidationOfRegister(fullName, email, password, phone)) { // new register
            userClassData.fullName = fullName
            userClassData.email = email
            userClassData.phone = phone
            userClassData.isAdmin = isAdmin
            userClassData.teacherExperience = experience
            userClassData.uid = uid


        }

        fireAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userClassData.uid = fireAuth.currentUser!!.uid
                    Log.d("TAG", "createUserWithEmail:success")
                    Toast.makeText(context, "account created successfully", Toast.LENGTH_SHORT)
                        .show()
                    //save users data on fireBaseStore

                    fireStore.collection("Users")
                        .document(auth.currentUser?.uid!!)
                        .set(userClassData)
                        .addOnSuccessListener {
                            Log.e("TAG1", "add user in fireStore successfully")


                        }.addOnFailureListener {
                            Log.e("TAG1", " Error while adding user in fireStore", it)
                        }


                } else {
                    // If register failed, show a message to the user.
                    Log.w("TAG1", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed ,${task.exception}.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }


    }


    private fun checkValidationOfRegister(
        fullName: String, email: String, password: String, phone: String
    ): Boolean {
        valid = !(fullName.isEmpty() || (email.isEmpty())
                || (password.isEmpty()) || (phone.isEmpty()))
        return valid
    }



}





//            fullName.error = "you should fill your name"
//            email.error = "you should fill your email"
//            password.error = "ou should fill your password"
//            phone.error = "ou should fill your phone"
