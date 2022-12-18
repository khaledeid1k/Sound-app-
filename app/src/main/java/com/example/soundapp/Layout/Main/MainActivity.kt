package com.example.soundapp.Layout.Main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.soundapp.DataBase.QuaranFiles
import com.example.soundapp.Layout.Main.Albums.Albums
import com.example.soundapp.Layout.Main.Quran.Quran
import com.example.soundapp.Layout.Main.Quran.Quran.Companion.quranadapter
import com.example.soundapp.Layout.Main.Sort.Sort_QuranViewModel
import com.example.soundapp.R
import com.example.soundapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


     val quran= Quran
     lateinit var viewModel: Sort_QuranViewModel
      private var Sorted_By : String ="Name"

    companion object {
        var list_Main = arrayListOf<QuaranFiles>()
        var albums_name = arrayListOf<QuaranFiles>()


    }

    var duplicate_albums = arrayListOf<String>()

    val RESULTCODE = 1
    val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    //دي الداتا الي هعوزها
    val projection = arrayOf(
        //    MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL).toString(),
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Albums._ID,
        MediaStore.Audio.Albums.ALBUM,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
         viewModel= Sort_QuranViewModel(application)

        lifecycleScope.launch(Dispatchers.Main) {
            Sorted_By = viewModel.getSort?.first()!!
            checkPermissionforexternalstorage()
        }


        setSupportActionBar(binding.toolbar)


    }

    private fun getAllAudio(): ArrayList<QuaranFiles> {
        var order = MediaStore.MediaColumns.DISPLAY_NAME + " ASC"
        when(Sorted_By){
            "Name"->{
                order= MediaStore.MediaColumns.DISPLAY_NAME + " ASC"

            } "Date"->{
                order=  MediaStore.MediaColumns.DATE_ADDED + " ASC"

            } "Size"->{
                order= MediaStore.MediaColumns.SIZE + " ASC"

            }
        }

        contentResolver.query(uri, projection, null, null, order)
            ?.use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                        //   val path = Uri.parse(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString() + File.separator + id)
                        //val path =ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,id)
                        val path =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                        val title =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                        val duration =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                        val artist =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                        val albums =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                        val albums_id =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                        val albums_ALBUM =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))

                        list_Main.add(
                            QuaranFiles(
                                path,
                                title,
                                duration,
                                artist,
                                albums,
                                albums_id,
                                albums_ALBUM,
                                id
                            )
                        )

                        // to prevent duplicated albums
                        if (!duplicate_albums.contains(albums)) {
                            albums_name.add(
                                QuaranFiles(
                                    path,
                                    title,
                                    duration,
                                    artist,
                                    albums,
                                    albums_id,
                                    albums_ALBUM,
                                    id
                                )
                            )
                            duplicate_albums.add(albums)
                        }

                    } while (cursor.moveToNext())
                    cursor.close()
                }
            }


        return list_Main
    }

    @SuppressLint("ShowToast")
    private fun checkPermissionforexternalstorage() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ) {
// هنا هبدأ اظهر الشرح للمستخدم بعد اول مره عشان يعرف انا محتاج الاذن ليه عن
                // طريق ي Toast او snackbar وبعد كده اطلب منه الاذن تاني
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), RESULTCODE
                )
            } else {
                // لا حاجة للشرح هذه اول مرة
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), RESULTCODE
                )
            }

        } else {
            list_Main = getAllAudio()
            link_Viewpager_With_Taplayout()


        }


    }

    private fun link_Viewpager_With_Taplayout() {
        val fragmentlist = arrayListOf<Fragment>(
            Quran(),
            Albums()
        )
        val viewpagerAdapter = ViewpagerAdapter(
            supportFragmentManager,
            lifecycle,
            fragmentlist
        )
        binding.viewpager.adapter = viewpagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab_layout, position ->
            tab_layout.text = viewpagerAdapter.getPageTitle(position)

            tab_layout.icon = AppCompatResources
                .getDrawable(this, viewpagerAdapter.getPageIcon(position))
        }.attach()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RESULTCODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    list_Main = getAllAudio()
                    link_Viewpager_With_Taplayout()

                } else {
                    checkPermissionforexternalstorage()
                }
                return
            }
            else -> {

            }


        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menue, menu)
        val search = menu?.findItem(R.id.search_bar)
        val searchView: SearchView = search?.actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                quranadapter.filter.filter(p0)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.Name ->{
                lifecycleScope.launch {
                    viewModel.saveToDataStore("Name")
                    quranadapter.notifyDataSetChanged()

                }
            }
            R.id.Date ->{
                lifecycleScope.launch {
                    viewModel.saveToDataStore("Date")

                    quranadapter.notifyDataSetChanged()
                }
            }
            R.id.Size ->{
                lifecycleScope.launch {
                    viewModel.saveToDataStore("Size")
                    quranadapter.notifyDataSetChanged()

                }
            }
        }
        return super.onOptionsItemSelected(item)

    }


}





