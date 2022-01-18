package com.example.learn_new_language.profiles

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.example.learn_new_language.R
import com.example.learn_new_language.Repository
import com.example.learn_new_language.Repository.Companion.fireStore
import com.example.learn_new_language.Repository.Companion.rating
import com.example.learn_new_language.Repository.Companion.showTeacherProfile
import com.example.learn_new_language.listTeacher.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FragmentShowTeacherProfile : Fragment() {

    private lateinit var teacherName: TextView
    private lateinit var teacherInfo: TextView
    private lateinit var teacherEmail: TextView
    private lateinit var chatIcon: ImageView
    private lateinit var profileImage: ImageView
    lateinit var ratingbarTeacher: RatingBar
    lateinit var rateTv: TextView
    lateinit var videoCallImg: ImageView
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var navController: NavController
    private val profileViewModel: ProfileViewModel by lazy { ViewModelProvider(this)[ProfileViewModel::class.java] }

    val args: FragmentShowTeacherProfileArgs by navArgs()

    var teacherUid = ""
    private var ratingAverage = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = NavController(requireContext())
        readFireData()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_show_teacher_profile, container, false)
        teacherName = view.findViewById(R.id.text_profileName)
        teacherInfo = view.findViewById(R.id.teacher_info)
        teacherEmail = view.findViewById(R.id.teacher_email)
        chatIcon = view.findViewById(R.id.chat_icon)
        profileImage = view.findViewById(R.id.profile_image)
        ratingbarTeacher = view.findViewById(R.id.ratingbar_teacher)
        rateTv = view.findViewById(R.id.rateTv)
        videoCallImg = view.findViewById(R.id.videocall_icon)

        ratingbarTeacher.setOnRatingBarChangeListener { _, rating, _ ->
            val ratingTeachers =
                RatingDataClass(userRating = rating.toString(), userId = auth.currentUser!!.uid )
            addRating(
                teacherId = teacherUid,
                ratingTeachers,
                studentId = auth.currentUser!!.uid
            )


            // counting rating
            val user = User()
            user.rating.forEach {
                ratingAverage += it.userRating.toFloat()
            }
            ratingAverage /= user.rating.size
            ratingbarTeacher.rating = ratingAverage
            rateTv.text = ratingAverage.toString()

        }


        chatIcon.setOnClickListener {
            Log.d("TAG", "onCreateView: $teacherUid")
            val action = FragmentShowTeacherProfileDirections
                .actionFragmentShowTeacherProfileToChatFragment2(teacherUid)
            findNavController().navigate(action)
        }

        videoCallImg.setOnClickListener {
            val action =
                FragmentShowTeacherProfileDirections.actionFragmentShowTeacherProfileToVideoCallFragment()
            findNavController().navigate(action)

      //     navController.navigate(R.id.videoCallFragment)
        }


        return view
    }


    // get information about specific teacher which is the student click on
    private fun readFireData() {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Users") // .whereEqualTo("fullName",Firebase.auth.currentUser.uid)
            .whereEqualTo("email", args.email)
            .addSnapshotListener { value, error ->
                // Log.e("fromProfile" , "the value is ${value?.data}")
                val result = value?.toObjects(User::class.java)?.get(0)
                teacherName.text = result?.fullName
                teacherInfo.text = result?.teacherExperience
                teacherEmail.text = result?.email
                teacherUid = result?.uid!!

            }

    }


    fun addRating(teacherId: String, ratingTeacher: RatingDataClass, studentId: String) {

        fireStore.collection("Users")
            .whereEqualTo("email", args.email)
            .addSnapshotListener { value, error ->
                // Log.e("fromProfile" , "the value is ${value?.data}")
                val result = value?.toObjects(User::class.java)?.get(0)
                teacherUid = result?.uid!!
                rating = result.rating.toString()


                val newRating: MutableList<RatingDataClass> = result.rating.toMutableList()
                    newRating.add(ratingTeacher)
                         fireStore.collection("Users").document(teacherUid)
                                 .update("rating", newRating)


//                if (result.rating == newRating) {
//                    fireStore.collection("Users").document(teacherUid)
//                        .update("rating",FieldValue.arrayUnion(ratingTeacher))
//                } else {
//                    newRating.add(ratingTeacher)
//                    fireStore.collection("Users").document(teacherUid).update("rating", newRating)
//                }

            }

    }


}






