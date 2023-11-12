package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.example.myapplication.R
import com.example.myapplication.data.Avatar

class AvatarAdapter (
    private val context: Context,
    private val resource: Int,
    private val myList: List<Avatar>
) : ArrayAdapter<Avatar?>(
    context, resource, myList
) {
    override fun getView(position: Int, contentView: View?, parent: ViewGroup): View {
        return getCustomView(position, contentView, parent)
    }

    override fun getDropDownView(position: Int, contentView: View?, parent: ViewGroup): View {
        return getCustomView(position, contentView, parent)
    }

    fun getCustomView(position: Int, contentView: View?, parent: ViewGroup) : View {

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(resource, null)
        val imageView = view.findViewById<ImageView>(R.id.image_avatar)
        val bean = myList[position]
        imageView.setImageResource(bean.imageId)
        return view
    }
}