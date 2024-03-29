package com.example.android.searchdebounce.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.rishabh_challenge.R
import com.example.rishabh_challenge.db.Photos
import com.squareup.picasso.Picasso

class PhotosAdapter(val photos: MutableList<Photos> = mutableListOf()) : RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        return PhotosViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.photo,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    inner class PhotosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(photo: Photos) {
            val imageView = itemView.findViewById<ImageView>(R.id.imageView)
            Picasso.get()
                .load(photo.url)
                .into(imageView)
        }
    }
}
