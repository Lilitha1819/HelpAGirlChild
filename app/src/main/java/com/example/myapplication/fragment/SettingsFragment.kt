package com.example.myapplication.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.adapter.AvatarAdapter
import com.example.myapplication.data.Avatar
import com.example.myapplication.data.User
import com.example.myapplication.data.UserState
import com.google.firebase.firestore.FirebaseFirestore

class SettingsFragment  : Fragment()  {

    override fun onCreateView(lf: LayoutInflater, viewGroup: ViewGroup?, bundle: Bundle?): View? {
        return lf.inflate(R.layout.fragment_settings, viewGroup, false)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)

        val spinner = view.findViewById<Spinner>(R.id.spinner)
        val nameView = view.findViewById<TextView>(R.id.name_settings)
        val button = view.findViewById<Button>(R.id.save)

        val list : List<Avatar> = mutableListOf(Avatar(1, R.drawable.avatar_1), Avatar(2, R.drawable.avatar_2),
            Avatar(3, R.drawable.avatar_3), Avatar(4, R.drawable.avatar_4))
        setImageSpinner(view.context, list,spinner)

        button.setOnClickListener {
            val selectedId : Long = spinner.selectedItemId
            val avatar : Int = list[selectedId.toInt()].imageId

            val name = nameView.text.toString()
            if (name.isEmpty()) {
                nameView.error = "Insert name"
            } else {
                updateDetails(name, avatar, view)
            }
        }
    }

    private fun updateDetails(name : String, avatar : Int, view : View) {
        val database = FirebaseFirestore.getInstance()
        val docId = UserState.getInstance()?.getUser()?.getDocId()
        val userId = UserState.getInstance()?.getUser()?.getToken()
        val email = UserState.getInstance()?.getUser()?.getEmail()

        database.collection("users").document(docId!!).update(
            "name", name, "image", avatar
        ).addOnSuccessListener {
            val user = User(email, userId, name, avatar, docId)
            UserState.getInstance()?.setUser(user)
            startActivity(Intent(view.context, MainActivity::class.java))
        }
    }

    private fun setImageSpinner(context: Context, myList: List<Avatar>, spinner: Spinner) {
        val arr = AvatarAdapter (
            context,
            R.layout.avatar_layout,
            myList
        )
        spinner.adapter = arr
    }
}