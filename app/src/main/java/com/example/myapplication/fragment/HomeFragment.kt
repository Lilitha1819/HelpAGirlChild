package com.example.myapplication.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.HistoryActivity
import com.example.myapplication.R
import com.example.myapplication.adapter.RecycleViewAdapter
import com.example.myapplication.data.History
import com.example.myapplication.data.News
import com.example.myapplication.data.User
import com.example.myapplication.data.UserState
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {
    override fun onCreateView(lf: LayoutInflater, viewGroup: ViewGroup?, bundle: Bundle?): View? {
        return lf.inflate(R.layout.fragment_home, viewGroup, false)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        var itemList : MutableList<News> = ArrayList()
        val user : User? = UserState.getInstance()?.getUser()

        itemList.add(News(1,"Founders of Help A Girl Child","",R.drawable.founders1))
        itemList.add(News(2,"Toiletries","",R.drawable.toiletries2))
        addRecyclerView(view, itemList)
        getHistory(view)
    }

    private fun addRecyclerView(view: View, news: List<News>) {
        val recyclerView: RecyclerView = view.findViewById<View>(R.id.recyclerview) as RecyclerView
        val recyclerViewLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(view.context, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = recyclerViewLayoutManager
        val recycleViewAdaptor = RecycleViewAdapter(news, view.context, R.layout.my_card_view)
        recyclerView.adapter = recycleViewAdaptor
    }

    private fun addList(view: View, stringArr : MutableList<String>) {
        val arr: ArrayAdapter<String> = ArrayAdapter<String>(view.context, R.layout.custom_list, R.id.listName, stringArr.toTypedArray())
        val listView: ListView? = view.findViewById<View>(R.id.listview) as ListView?
        listView!!.adapter = arr
    }

    private fun getHistory(view : View) {
        val database = FirebaseFirestore.getInstance()
        val stringArr : MutableList<String> = ArrayList()
        val user = UserState.getInstance()?.getUser()

        database.collection("donations").whereEqualTo("id", user?.getToken())
            .get().addOnCompleteListener {task ->
                if(task.isSuccessful) {
                    for (doc in task.result) {
                        stringArr.add(doc.data["description"].toString())
                    }
                    addList(view, stringArr)
                } else {
                    Toast.makeText(view.context, "Internal server error", Toast.LENGTH_LONG).show()
                }
            }
    }
}