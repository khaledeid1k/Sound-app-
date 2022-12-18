package com.example.soundapp.Layout.Main.Quran

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.soundapp.DataBase.QuaranFiles
import com.example.soundapp.Getphotos.Getphoto.Companion.getalbumsart
import com.example.soundapp.R
import java.util.*


class Quranadapter(
    var list: ArrayList<QuaranFiles>,
    private val context: Context?,
    private val listener: Onitemclicklisiner
) : RecyclerView.Adapter<Quranadapter.CustomAdapter>(),Filterable {

    var exampleListFull: ArrayList<QuaranFiles> = ArrayList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter {
        return CustomAdapter(
            LayoutInflater.from(context).inflate
                (R.layout.quran_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CustomAdapter, position: Int) {

        holder.textView.text = list[position].title ?: "No one"
        val getalbumsart = getalbumsart(list[position].Path)
        if (getalbumsart != null) {
            Glide.with(context!!).asBitmap().load(getalbumsart).into(holder.image)
        } else {
            Glide.with(context!!).asBitmap().load(R.drawable.quran1).into(holder.image)
        }

    }


    override fun getItemCount() = list.size

    inner class CustomAdapter(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        val textView: TextView = itemView.findViewById(R.id.text_items)
        val image: ImageView = itemView.findViewById(R.id.image_items)
        override fun onClick(p0: View?) {
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onitemclick(position)
            }

        }


    }


    interface Onitemclicklisiner {
        fun onitemclick(position: Int)

    }


    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: ArrayList<QuaranFiles> = ArrayList()
            if (constraint == null) {
                filteredList.addAll(exampleListFull)
            } else {
                val filterPattern: String = constraint.toString().toLowerCase().trim()
                for (item in exampleListFull) {
                    if (item.title?.toLowerCase()?.contains(filterPattern) == true) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(p0: CharSequence?, results: FilterResults?) {
            list.clear()
            list.addAll(results?.values as Collection<QuaranFiles>)
            notifyDataSetChanged()
        }

    }
    override fun getFilter(): Filter {
        return filter
    }




}






