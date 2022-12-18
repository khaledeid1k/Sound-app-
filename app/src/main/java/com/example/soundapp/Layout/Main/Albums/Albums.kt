package com.example.soundapp.Layout.Main.Albums

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.soundapp.ClickListener.ClickListener
import com.example.soundapp.ClickListener.RecyclerViewItemClickListener
import com.example.soundapp.Layout.Main.MainActivity.Companion.albums_name
import com.example.soundapp.R
import com.example.soundapp.databinding.FragmentAlbumsBinding


class Albums : Fragment() {

    private lateinit var binding: FragmentAlbumsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_albums, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val FragmentAlbumsBinding = FragmentAlbumsBinding.bind(view)
        binding = FragmentAlbumsBinding
        Put_Recycle()


    }
    fun Put_Recycle(){
        binding.recyclerviewAlbums .layoutManager = GridLayoutManager(context, 2)
        binding.recyclerviewAlbums.adapter =
            Alurms_adapter(albums_name, context)

        binding.recyclerviewAlbums.setHasFixedSize(true)

        binding.recyclerviewAlbums.addOnItemTouchListener(object : RecyclerViewItemClickListener(
            context,    binding.recyclerviewAlbums, object : ClickListener {
                override fun onClick(view: View?, position: Int) {

                    val intent = Intent(context, albums_details::class.java)
                    intent.putExtra("albums_name", albums_name[position].albums)
                    intent.putExtra("albums_song", albums_name[position].albums_ALBUM)
                    intent.putExtra("path", albums_name[position].Path)

                    context?.startActivity(intent)
                }

                override fun onLongClick(view: View?, position: Int) {

                }


            }
        ) {})

    }




}