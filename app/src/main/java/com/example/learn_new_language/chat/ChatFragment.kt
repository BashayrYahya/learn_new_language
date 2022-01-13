package com.example.learn_new_language.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learn_new_language.R
import com.example.learn_new_language.listTeacher.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore


class ChatFragment : Fragment() {
    val senderId = FirebaseAuth.getInstance().currentUser?.uid

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var textName : TextView
    private var teacherUid = ""


    private val args : ChatFragmentArgs by navArgs()

    lateinit var messageArrayList: ArrayList<MessageData>
    lateinit var messageAdapter: MessageAdapter
    lateinit var databaseRef : DatabaseReference
    private lateinit var fireAuth: FirebaseAuth
    var receiverRoom : String? = null
    var senderRoom :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_list, container, false)


        // initializes items
        chatRecyclerView = view.findViewById(R.id.recycler_chat)
        messageBox = view.findViewById(R.id.send_msg_editText)
        sendButton = view.findViewById(R.id.send_image)
        fireAuth = FirebaseAuth.getInstance()
        messageArrayList = ArrayList()
        messageAdapter = MessageAdapter(requireContext() ,messageArrayList )
        chatRecyclerView.layoutManager = LinearLayoutManager(context)
        chatRecyclerView.adapter = messageAdapter
        databaseRef = FirebaseDatabase.getInstance().getReference()
        textName =  view.findViewById(R.id.textNamee)
        receiveUserUid()


        //adding data(messages) to database (real time)
        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            val messageObj = MessageData(message, senderId)
            databaseRef.child("chats").child(senderRoom)
                .child("messages").push()
                .setValue(messageObj).addOnSuccessListener {
                    databaseRef.child("chats").child(receiverRoom!!)
                        .child("messages").push()
                        .setValue(messageObj)
                }
        messageBox.setText("")

        }

//        databaseRef.child("chats").addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                messageArrayList.clear()
//                for (postSnapshot in snapshot.children ){
//                    val currentUser =  postSnapshot.getValue(MessageData::class.java)
//                    if (fireAuth.currentUser?.uid != fireAuth.currentUser?.uid ){
//                        messageArrayList.add(currentUser!!)
//
//                    }
//                    messageArrayList.add(currentUser!!)
//
//                }
//                messageAdapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//
//        })

        return view
    }


    // get uid of the teacher from recycler to do chat with him
    private fun receiveUserUid() {

        val fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Users")
            .whereEqualTo("uid",args.uid )
            .addSnapshotListener { value, error ->
                // Log.e("fromProfile" , "the value is ${value?.data}")
                if (value != null)
                    if (value.toObjects(User::class.java).isNotEmpty()) {
                        val result = value.toObjects(User::class.java)[0]

                        textName.text = result?.fullName
                        teacherUid = result?.uid!!
                        (activity as AppCompatActivity?)!!.supportActionBar!!.setSubtitle(R.id.textNamee)

                    }

                // make room to connect receiver with sender
                //val senderId = FirebaseAuth.getInstance().currentUser?.uid
                senderRoom = args.uid + senderId
                receiverRoom = args.uid + senderId

                databaseRef.child("chats").child(senderRoom)
                    .child("messages").addValueEventListener(object : ValueEventListener{

                        override fun onDataChange(snapshot: DataSnapshot) {

                            messageArrayList.clear()
                            for (postSnapshot in snapshot.children){
                                val message = postSnapshot.getValue(MessageData::class.java)
                                messageArrayList.add(message!!)


                            }

                            messageAdapter = MessageAdapter(requireContext(),messageArrayList)
                            chatRecyclerView.adapter = messageAdapter



                        }

                        override fun onCancelled(error: DatabaseError) {

                        }


                    })

            }

    }

}