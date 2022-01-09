package com.example.learn_new_language.listTeacher

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learn_new_language.R
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*
import kotlin.collections.ArrayList


class ListTeacherFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var teacherRecyclerView: RecyclerView
    lateinit var userArrayList: ArrayList<User>
    lateinit var filterUserArrayList: ArrayList<User>
    lateinit var teacherAdapter: TeacherAdapter
    lateinit var firestore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_teacher_list, container, false)
        searchView = view.findViewById(R.id.search_view)
        teacherRecyclerView = view.findViewById(R.id.recycler_teachers)
        teacherRecyclerView.layoutManager = LinearLayoutManager(context)
        teacherRecyclerView.hasFixedSize()
        userArrayList = ArrayList()
        filterUserArrayList = ArrayList()
        teacherAdapter = TeacherAdapter(userArrayList)
        teacherRecyclerView.adapter = teacherAdapter

        //recyclerView.adapter = filterTeacherAdapter
        teacherRecyclerView.setOnClickListener { teacherRecyclerView.startLayoutAnimation() }

        // make a search inside recycler of teachers
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    if (newText.isNotBlank()) {
                        filterUserArrayList.clear()
                        val search = newText.lowercase(Locale.getDefault())
                        userArrayList.forEach {
                            if (it.fullName.lowercase(Locale.getDefault()).contains(search)) {
                                filterUserArrayList.add(it)
                            }
                            teacherRecyclerView.adapter = TeacherAdapter(filterUserArrayList)
                        }

                    } else {
                        filterUserArrayList.clear()
                        teacherRecyclerView.adapter = TeacherAdapter(userArrayList)
                    }
                }
                return false
            }
        })


        eventChangeListener()


        return view
    }


    private fun eventChangeListener() {

        firestore = FirebaseFirestore.getInstance()
        firestore.collection("Users").orderBy("fullName", Query.Direction.ASCENDING)
            .whereEqualTo("admin", "1")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {


                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {


                    // if there errors in fireStore and did not get data
                    if (error != null) {

                        Log.e(
                            "FireStore_error",
                            "${error.message.toString()} , errors in fireStore of teacher list"
                        )
                        return
                    }

                    //get data from fireStore  and keep it in update

                    for (dc: DocumentChange in value?.documentChanges!!) {
                        //if (dc.type == DocumentChange.Type.ADDED) {
                        Log.e("fromChild", "data ${dc.document.data}")
                        userArrayList.add(dc.document.toObject(User::class.java))
                        // }

                    }
                    teacherAdapter.notifyDataSetChanged()
                }

            })

    }


}