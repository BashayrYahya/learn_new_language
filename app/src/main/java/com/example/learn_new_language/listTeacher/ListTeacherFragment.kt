package com.example.learn_new_language.listTeacher

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.ActionOnlyNavDirections
import com.example.learn_new_language.R
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.*


class ListTeacherFragment : Fragment() {


    private lateinit var recyclerView: RecyclerView
    lateinit var userArrayList: ArrayList<User>
    lateinit var
            teacherAdapter: TeacherAdapter
    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_teacher_list, container, false)

        recyclerView = view.findViewById(R.id.recycler_teachers)
        recyclerView.layoutManager= LinearLayoutManager(context)
        recyclerView.hasFixedSize()
        userArrayList = ArrayList()
        teacherAdapter = TeacherAdapter(userArrayList)
     recyclerView.adapter = teacherAdapter


      recyclerView.setOnClickListener { recyclerView.startLayoutAnimation() }




        eventChangeListener ()



        return view
    }


    private fun  eventChangeListener (){

        firestore = FirebaseFirestore.getInstance()
        firestore.collection("Users").orderBy("fullName",Query.Direction.ASCENDING).whereEqualTo("isAdmin","1")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {



                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {


                    // if there errors in fireStore and did not get data
                    if (error!= null) {

                        Log.e("FireStore_error","${error.message.toString()} , errors in fireStore of teacher list")
                        return
                    }

                    //get data from fireStore  and keep it in update

                    for (dc:DocumentChange in value?.documentChanges!!) {
                        //if (dc.type == DocumentChange.Type.ADDED) {
                            Log.e("fromChild" , "data ${dc.document.data}")
                            userArrayList.add(dc.document.toObject(User::class.java))
                       // }

                    }
                    teacherAdapter.notifyDataSetChanged()
                }

            })

    }





}