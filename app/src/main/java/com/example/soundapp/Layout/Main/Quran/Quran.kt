package com.example.soundapp.Layout.Main.Quran

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soundapp.ClickListener.ClickListener
import com.example.soundapp.ClickListener.RecyclerViewItemClickListener
import com.example.soundapp.Layout.Main.MainActivity.Companion.list_Main
import com.example.soundapp.Layout.Main.PlayerActivity
import com.example.soundapp.R
import com.example.soundapp.databinding.FragmentQuranBinding

class Quran : Fragment(), Quranadapter.Onitemclicklisiner {
    lateinit var binding: FragmentQuranBinding





    companion object{

        lateinit var quranadapter:Quranadapter

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }






    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quran, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val FragmentQuranBinding = FragmentQuranBinding.bind(view)
        binding = FragmentQuranBinding
         quranadapter = Quranadapter(list_Main, context, this)
        put_recycle()




    }

    override fun onitemclick(position: Int) {
//
//        val intent = Intent(context, PlayerActivity::class.java)
//        intent.putExtra("position",position)
//        context?.startActivity(intent)
    }


    fun put_recycle() {

        binding.recyclerviewSongs.adapter =quranadapter

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerviewSongs.layoutManager = layoutManager
        binding.recyclerviewSongs.setHasFixedSize(true)

        binding.recyclerviewSongs.addOnItemTouchListener(object : RecyclerViewItemClickListener(
            context, binding.recyclerviewSongs, object : ClickListener {

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onClick(view: View?, position: Int) {
                    val intent = Intent(
                        context,
                        PlayerActivity::class.java
                    )
                    intent.putExtra("position", position)
                    intent.putParcelableArrayListExtra("list", list_Main);
                    context?.startActivity(intent)

                    val intent1 = Intent() // Build the intent for the service
                    context?.startForegroundService(intent)

                }

                override fun onLongClick(view: View?, position: Int) {

                }

            }
        ) {})


    }







}