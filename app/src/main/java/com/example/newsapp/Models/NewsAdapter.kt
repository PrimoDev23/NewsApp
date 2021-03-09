package com.example.newsapp.Models

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R

internal class NewsAdapter(var context : Context, var list : List<Article>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(){

    internal inner class NewsViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val img : ImageView = view.findViewById(R.id.news_image)
        val manager = Glide.with(view)
        val title : TextView = view.findViewById(R.id.txt_title)
        val shortDesc : TextView = view.findViewById(R.id.txt_shortDesc)
        val time : TextView = view.findViewById(R.id.txt_time)
        val layout : ConstraintLayout = view.findViewById(R.id.layout)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)

        return NewsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = list[position]

        //Set texts accordingly
        holder.title.text = article.title
        holder.shortDesc.text = article.description
        holder.time.text = article.publishedAt.replace('T', ' ').replace("Z", "")

        //Load the image from the web
        holder.manager.load(article.urlToImage).into(holder.img)

        holder.layout.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(article.url)
            startActivity( context, openURL, Bundle.EMPTY)
        }
    }
}