package com.example.soundapp.Layout.Main.Albums

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
import java.util.*

class Alurms_adapter (
    var list_albums: ArrayList<QuaranFiles>,
    private val context: Context?,
) : RecyclerView.Adapter<Alurms_adapter.CustomAdapter>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter {
        return CustomAdapter(
            LayoutInflater.from(context).inflate
                (R.layout.alurms_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CustomAdapter, position: Int) {

        holder.textView.text = list_albums[position].albums ?: "No one"
        val getalbumsart = getalbumsart(list_albums[position].Path)
        if (getalbumsart != null) {
            Glide.with(context!!).asBitmap().load(getalbumsart).into(holder.image)
        } else {
            Glide.with(context!!).asBitmap().load(R.drawable.quran1).into(holder.image)
        }


    }


    override fun getItemCount() = list_albums.size

    inner class CustomAdapter(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.card_text_albums)
        val image: ImageView = itemView.findViewById(R.id.card_image_albums)



    }



}
