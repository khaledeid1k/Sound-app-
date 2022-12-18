package com.example.soundapp.Layout.Main.Albums

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.soundapp.ClickListener.ClickListener
import com.example.soundapp.ClickListener.RecyclerViewItemClickListener
import com.example.soundapp.DataBase.QuaranFiles
import com.example.soundapp.Getphotos.Getphoto
import com.example.soundapp.Layout.Main.Albums.Alums_details.albums_details_Adapter
import com.example.soundapp.Layout.Main.PlayerActivity
import com.example.soundapp.R
import com.example.soundapp.databinding.AlbumsDetailsBinding

class albums_details : AppCompatActivity() {
    var albumSongList = arrayListOf<QuaranFiles>()
    lateinit var positions: String
    lateinit var message: String
    lateinit var path: String
    private lateinit var binding: AlbumsDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AlbumsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        List_Quran()
        Set_Title_Actionbar()
        Put_Image()
        Put_Recycle_Albums_Details()


    }


    private fun Put_Recycle_Albums_Details() {
        binding.contentLayoutBasedActivity.recyclerviewAlbumsDetails
            .layoutManager = LinearLayoutManager(this)
        binding.contentLayoutBasedActivity.recyclerviewAlbumsDetails
            .adapter =
            albums_details_Adapter(albumSongList, this)
        binding.contentLayoutBasedActivity.recyclerviewAlbumsDetails
            .setHasFixedSize(true)
        binding.contentLayoutBasedActivity.recyclerviewAlbumsDetails
            .addOnItemTouchListener(object : RecyclerViewItemClickListener(
                this,
                binding.contentLayoutBasedActivity.recyclerviewAlbumsDetails,
                object : ClickListener {
                    override fun onClick(view: View?, position: Int) {

                        val intent = Intent(
                            this@albums_details,
                            PlayerActivity()::class.java
                        )
                        intent.putExtra("position", position)
                        intent.putParcelableArrayListExtra("list", albumSongList);

                        startActivity(intent)
                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }


                }
            ) {})
    }

    fun Set_Title_Actionbar() {
        message = intent.getStringExtra("albums_name").toString()
        setSupportActionBar(binding.toolbarAlbumsDetails)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.collapsingToolbarLayout.title = message
    }

    fun Put_Image() {
        path = intent.getStringExtra("path").toString()
        val getalbumsart = Getphoto.getalbumsart(path)
        if (getalbumsart != null) {
            Glide.with(this).asBitmap().load(getalbumsart).into(
                binding.ImageAlbumsDetails
            )
        } else {
            Glide.with(this).asBitmap().load(R.drawable.quran1).into(
                binding.ImageAlbumsDetails
            )
        }
    }

    fun List_Quran(): ArrayList<QuaranFiles> {
        positions = intent.getStringExtra("albums_song").toString()
        val uri: Uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI

        var v = ""
        val columns = arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM
        )

        contentResolver?.query(uri, columns, null, null, null)
            ?.use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        v = positions.toString()
                    } while (cursor.moveToNext())
                }
            }

        // I want to list down song in album Rolling Papers (Deluxe Version)
        val column = arrayOf(
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.ALBUM_ID
        )
        val selection = MediaStore.Audio.Media.ALBUM + "= ?"
        val selectionArgs = arrayOf(v)
        val urii = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        contentResolver?.query(
            urii, column, selection,
            selectionArgs, null
        )?.use { cursor ->

            if (cursor.moveToFirst()) {
                do {
                    val name: String = cursor.getString(
                        cursor.getColumnIndex
                            (MediaStore.Audio.Media.DISPLAY_NAME)
                    )


                    val path: String = cursor.getString(
                        cursor.getColumnIndex
                            (MediaStore.Audio.Media.DATA)
                    )
                    albumSongList.add(
                        QuaranFiles(path, name,null,null,null
                    ,null,null,0))
                } while (cursor.moveToNext())
                cursor.close()

            }


        }
        return albumSongList

    }


}