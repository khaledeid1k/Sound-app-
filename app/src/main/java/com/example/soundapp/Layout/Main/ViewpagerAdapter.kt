package com.example.soundapp.Layout.Main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.soundapp.R
import java.util.*

class ViewpagerAdapter (
    fragment: FragmentManager,
    lifecycle: Lifecycle,
    listFragment: ArrayList<Fragment>
)
    : FragmentStateAdapter(fragment,lifecycle) {
    companion object {
        private val TAB_TITLES = arrayOf(
            " Quran",
            "Albums"
        )
        private val TAB_ICON = arrayOf(
           R.drawable.quran,
            R.drawable.albums
        )
    }

    private val fragmentlist =listFragment

    override fun getItemCount(): Int =fragmentlist.size

    override fun createFragment(position: Int): Fragment {


        return fragmentlist[position]
    }


    fun getPageTitle(position: Int): String =
        TAB_TITLES[position]


    fun getPageIcon(position: Int): Int =
        TAB_ICON[position]

}