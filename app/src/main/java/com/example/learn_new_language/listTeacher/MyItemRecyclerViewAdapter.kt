package com.example.learn_new_language.listTeacher

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.learn_new_language.R



class TeacherAdapter(private val userList: ArrayList<User>) : RecyclerView.Adapter<TeacherAdapter.TeacherListHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherListHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.fragment_list_teacher,
            parent ,
        false  )


        return TeacherListHolder(itemView)

    }

    override fun onBindViewHolder(holder: TeacherListHolder, position: Int) {
        val user : User = userList[position]
    holder.teacherName.text = user.fullName
    holder.infoTeacher.text = user.teacherExperience

        holder.bind(user)


    }

    override fun getItemCount(): Int =userList.size



    inner class TeacherListHolder(view:View) : RecyclerView.ViewHolder(view) {
        val teacherName :TextView = view.findViewById(R.id.nameTeacher)
        val infoTeacher :TextView = view.findViewById(R.id.aboutTeacher)
        private lateinit var teacher:User


        fun bind(user: User){
            teacher = user
        }

        init {

                itemView.setOnClickListener {
                        val action = ListTeacherFragmentDirections.actionListTeacherFragmentToFragmentShowTeacherProfile(teacher.email)
                       it.findNavController().navigate(action)




            }
        }


    }






}