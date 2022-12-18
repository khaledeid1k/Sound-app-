package com.example.soundapp.Layout.Main.Albums.Alums_details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.soundapp.DataBase.QuaranFiles
import com.example.soundapp.Getphotos.Getphoto.Companion.getalbumsart
import com.example.soundapp.R


class albums_details_Adapter(
    var list_albums_details: ArrayList<QuaranFiles>,
    private val context: Context?
    ) : RecyclerView.Adapter<albums_details_Adapter.CustomAdapter>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter {
        return CustomAdapter(
            LayoutInflater.from(context).inflate
                (R.layout.alurms_details_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CustomAdapter, position: Int) {

        holder.textView.text = list_albums_details[position].title ?: "No one"
        val getalbumsart = getalbumsart(list_albums_details[position].Path)
        if (getalbumsart != null) {
            Glide.with(context!!).asBitmap().load(getalbumsart).into(holder.image)
        } else {
            Glide.with(context!!).asBitmap().load(R.drawable.quran1).into(holder.image)
        }

    }


    override fun getItemCount() = list_albums_details.size

    inner class CustomAdapter(itemView: View) :
        RecyclerView.ViewHolder(itemView){


        val textView: TextView = itemView.findViewById(R.id.text_albums_details)
        val image: ImageView = itemView.findViewById(R.id.image_albums_details)



    }




}






