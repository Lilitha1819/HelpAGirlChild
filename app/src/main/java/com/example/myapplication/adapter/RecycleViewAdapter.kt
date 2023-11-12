package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.NewsActivity
import com.example.myapplication.R
import com.example.myapplication.data.News

class RecycleViewAdapter(
    private val news: List<News>,
    private val context: Context,
    private val resource: Int
) : RecyclerView.Adapter<RecycleViewAdapter.CustomView?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomView {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(resource, null)
        return CustomView(view)
    }

    override fun onBindViewHolder(holder: CustomView, @SuppressLint("RecyclerView") position: Int) {
        holder.heading.text = news[position].heading
        holder.image.setImageResource(news[position].image)
        holder.cardView.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, NewsActivity::class.java)
            intent.putExtra("heading", news[position].heading)
            context.startActivity(intent)
        })
    }

    inner class CustomView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var heading: TextView
        var image: ImageView
        var cardView: CardView

        init {
            heading = itemView.findViewById<View>(R.id.heading) as TextView
            image = itemView.findViewById<View>(R.id.background_image) as ImageView
            cardView = itemView.findViewById<View>(R.id.card_view) as CardView
        }
    }

    override fun getItemCount(): Int {
        return news.size
    }
}