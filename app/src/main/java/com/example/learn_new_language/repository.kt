package com.example.learn_new_language

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import coil.load
import com.example.learn_new_language.listTeacher.User
import com.example.learn_new_language.login_register.RegisterFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView


private const val TAG1 = "RegisterFragment"
class Repository  {



    companion object{
       fun getInstant () : Repository = Repository()
        private val fireStore = FirebaseFirestore.getInstance()
        private val auth :FirebaseAuth = FirebaseAuth.getInstance()
        private val currentUser = auth.currentUser?.uid
        private var valid: Boolean = true
        private val fireAuth = FirebaseAuth.getInstance()
        lateinit var    databaseRef :DatabaseReference
        val registerFragment = RegisterFragment ()






    }


    fun uploadPhotoToFirebaseStorage (imgURI: Uri){
        val imageReference = FirebaseStorage.getInstance().getReference("/photo/$currentUser")
        imageReference.putFile(imgURI)
            .addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener{
                    saveImageToFireStore(it.toString())
                }.addOnFailureListener{
                    Log.d("","there is an error in upload pics")
                }
            }
    }

     private fun saveImageToFireStore(profileImageUri :String){
         fireStore.collection("Users")
             .document(auth.currentUser!!.uid)
             .update("imgProfile",profileImageUri)
     }


    fun getPhotoFromFirebaseStorage (image : CircleImageView, userUri: String?= auth.currentUser?.uid){
         val imageUri = FirebaseStorage.getInstance().getReference("/photos/ $userUri")
             .downloadUrl
        imageUri.addOnSuccessListener {
            image.load(it)
        }

    }

    // checked if the all editText is field
     fun checkFieldLogin(email: String, password: String): Boolean {
        valid = !(email.isEmpty() || password.isEmpty())
      //  email.error = "you should fill your email "
//            password.error = "you should fill your password"
        return valid
    }

    fun checkUserLoginData(
        email:String,
        password: String,
        context:Context) {

        if (checkFieldLogin(email, password)) {
            fireAuth.signInWithEmailAndPassword(email, password)

                .addOnCompleteListener {

                    if (it.isSuccessful) {
                        Log.d(ContentValues.TAG, "login is successes")
                        checkIfUserTeacherOrStudent(fireAuth.currentUser!!.uid , context, Activity()) // =it.user.uid
                        addUserToDataBase (email, auth.currentUser?.uid!!)



                    }

                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "login is Failure", e)
                    Toast.makeText(
                        context, "make sure from your email or password", Toast.LENGTH_SHORT
                    ).show()
                }

        }
        val userClassData = User()
        userClassData.email = email


    }

     fun checkIfUserTeacherOrStudent(uid: String,context: Context,activity: Activity) {
        val df: DocumentReference =
            fireStore.collection("Users")
                .document(uid)

        //  if the user is Student  intent him to fragments
        df.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.getString("isAdmen") != "1") {

                val intent = Intent(context, MainActivity2::class.java)
                activity.startActivity(intent)



                // check if the user is teacher
            } else if (documentSnapshot.getString("isAdmen") != "0") {
                val intent = Intent(context, MainActivity2::class.java)
               activity.startActivity(intent)


            }
        }
    }


     fun  addUserToDataBase (email: String, uid : String){
        val userDataClass = User()
        userDataClass.uid = uid
        userDataClass.email = email
        databaseRef = FirebaseDatabase.getInstance().getReference()
        databaseRef.child("Users").child(uid)
            .setValue(User(email,uid))

    }


     fun funOfNewRegister(fullName: String, email: String, password: String,
                          phone: String, isAdmin:String, experince:String, context: Context )
        {

         val userClassData = User()





        if (checkField(fullName, email, password, phone)) { // new register
            userClassData.fullName =fullName
            userClassData.email = email
            userClassData.phone = phone
            userClassData.isAdmin = isAdmin
            userClassData.teacherExperience = experince


            fireAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {

                        val user: FirebaseUser = fireAuth.currentUser!!
                        //val user = User(id = task.user!!.uid, email = email, fullName = fullName)

                        // if register successes user's information
                        Log.d("TAG", "createUserWithEmail:success")
                        Toast.makeText(context, "account created successfully", Toast.LENGTH_SHORT)
                            .show()


                        userClassData.uid = user.uid

                        // specify if  the user admen




                        //save users data on fireBaseStore
                        fireStore.collection("Users").document(user.uid)
                            .set(userClassData).addOnSuccessListener {
                                Log.e(TAG1, "done")

                            }.addOnFailureListener {
                                Log.e(TAG1, "something gone wrong", it)
                            }


                    } else {
                        // If register failed, display a message to the user.
                        Log.w(TAG1, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            context,
                            "Authentication failed ,${task.exception}.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
        }


    }


     fun checkField(
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
