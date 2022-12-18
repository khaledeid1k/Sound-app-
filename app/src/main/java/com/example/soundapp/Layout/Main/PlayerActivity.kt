package com.example.soundapp.Layout.Main

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.example.soundapp.DataBase.QuaranFiles
import com.example.soundapp.R
import com.example.soundapp.databinding.ActivityPlayerBinding
import kotlinx.coroutines.*
import wseemann.media.FFmpegMediaMetadataRetriever


class PlayerActivity : AppCompatActivity() {
    lateinit var binding: ActivityPlayerBinding
    var position: Int = -1
    private var list = arrayListOf<QuaranFiles>()

    lateinit var uri: Uri
    private val mHandler: Handler = Handler()

    companion object {
        var mediaplayer: MediaPlayer? = null
        var RepeatBoolean = false
        var shuffleBoolean = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        position = intent.getIntExtra("position", -1)
        list = intent.getParcelableArrayListExtra("list")!!

        CoverImage(list[position].Path)
        startMedia()
        playNextSound()
        playPreviousSound()
        playPauseSound()
        playShuffle()
        playRepeat()

    }

    private fun repeat() {
        if (RepeatBoolean) {
            RepeatBoolean = false
            binding.repeat.setImageResource(R.drawable.ic_baseline_repeat_24)
        } else {
            RepeatBoolean = true
            binding.repeat.setImageResource(R.drawable.ic_baseline_repeaton_24)

        }
    }

    private fun playRepeat() {
        binding.repeat.setOnClickListener {
            repeat()
        }
    }

    private fun shuffle() {
        if (shuffleBoolean) {
            shuffleBoolean = false
            binding.shuffle.setImageResource(R.drawable.ic_baseline_shuffle_24)
        } else {
            shuffleBoolean = true
            binding.shuffle.setImageResource(R.drawable.ic_baseline_shuffleon_24_)

        }
    }

    private fun playShuffle() {
        binding.shuffle.setOnClickListener {
            shuffle()
        }
    }

    fun changeseekbar() {
        if (mediaplayer != null) {

            binding.seekbar.progress = mediaplayer!!.currentPosition
            binding.durationplay.text = createTimeLabel(mediaplayer!!.currentPosition)
        }

    }

    private fun playMedia(uri: Uri) {

        mediaplayer = MediaPlayer.create(applicationContext, uri)
        if (mediaplayer?.duration == null) {
            Toast.makeText(this, "Could’t play The Sound You Track ", Toast.LENGTH_SHORT).show()
        } else {
            mediaplayer?.setOnCompletionListener {
                if (RepeatBoolean) {
                    playMedia(uri)
                } else {
                    if (position == list.size) {
                        position = 0
                        nextSound()
                    } else {
                        nextSound()
                    }
                }

            }
            binding.seekbar.max = mediaplayer!!.duration
            binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekbar: SeekBar?,
                    progress: Int,
                    fromuser: Boolean
                ) {
                    if (fromuser) {
                        mediaplayer?.seekTo(progress)
                        seekbar?.progress = progress

                    }
                    //        mediaplayerEnd()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }

            })
            runOnUiThread(object : Runnable {
                override fun run() {
                    changeseekbar()
                    mHandler.postDelayed(this, 1000)
                }
            })
            mediaplayer?.start()
            binding.durationend.text = createTimeLabel(mediaplayer!!.duration)
            putnamesura()


        }
    }


    private fun stopMedia() {
        mediaplayer?.stop()
        mediaplayer?.reset()
        mediaplayer?.release()
        mediaplayer = null
    }


    fun createTimeLabel(duration: Int): String? {
        var timeLabel: String? = ""
        val min = duration / 1000 / 60
        val sec = duration / 1000 % 60
        timeLabel += "$min:"
        //هنا هو كاتب اقل من عشره عشان لو طلع الرقم من 1 لحد 9 يحط قبله صفر
        if (sec < 10) timeLabel += "0"
        timeLabel += sec
        return timeLabel
    }

    private fun CoverImage(uri: String?) {
        val getQuaranPhoths = getQuaranPhoths(uri)
        if (getQuaranPhoths != null) {
            if (getQuaranPhoths.isEmpty()) {

                Glide.with(this).asBitmap().load(R.drawable.quran1)
                    .into(binding.coverImage)

                binding.gradient.setBackgroundResource(R.drawable.gradient_bg)
                binding.container.setBackgroundResource(R.drawable.gradient_bg2)
                binding.nameQuran.setTextColor(Color.WHITE)
                binding.sheikh.setTextColor(Color.GRAY)

            } else {

                val bitmap = BitmapFactory.decodeByteArray(
                    getQuaranPhoths, 0,
                    getQuaranPhoths.size
                )
                imageAnimation(this, binding.coverImage, bitmap)
                // cover_image.setImageBitmap(bitmap)

                // here generate palette in background thread
                Palette.from(bitmap).generate { palette ->
                    val dominantSwatch = palette!!.dominantSwatch

                    if (dominantSwatch != null) {
                        binding.gradient.setBackgroundResource(R.drawable.gradient_bg)
                        binding.container.setBackgroundResource(R.drawable.gradient_bg2)

                        val gradiatdrawable = GradientDrawable(
                            GradientDrawable.Orientation.BOTTOM_TOP,
                            intArrayOf(dominantSwatch.rgb, 0x00000000)
                        )
                        binding.gradient.background = gradiatdrawable

                        val gradiatdrawablebg = GradientDrawable(
                            GradientDrawable.Orientation.BOTTOM_TOP,
                            intArrayOf(dominantSwatch.rgb, dominantSwatch.rgb)
                        )
                        binding.container.background = gradiatdrawablebg

                        binding.nameQuran.setTextColor(dominantSwatch.titleTextColor)
                        binding.sheikh.setTextColor(dominantSwatch.titleTextColor)
                        binding.durationplay.setTextColor(dominantSwatch.titleTextColor)
                        binding.durationend.setTextColor(dominantSwatch.titleTextColor)


                    } else {
                        binding.gradient.setBackgroundResource(R.drawable.gradient_bg)
                        binding.container.setBackgroundResource(R.drawable.gradient_bg2)
                        val gradiatdrawable = GradientDrawable(
                            GradientDrawable.Orientation.BOTTOM_TOP,
                            intArrayOf(0xff000000.toInt(), 0x00000000)
                        )
                        binding.gradient.background = gradiatdrawable
                        val gradiatdrawablebg = GradientDrawable(
                            GradientDrawable.Orientation.BOTTOM_TOP,
                            intArrayOf(0xff000000.toInt(), 0xff000000.toInt())
                        )
                        binding.container.background = gradiatdrawablebg
                        binding.nameQuran.setTextColor(Color.WHITE)
                        binding.sheikh.setTextColor(Color.GRAY)
                    }


                }

            }
        }
    }

    private fun putnamesura() {
        binding.sheikh.text = list[position].artist
        binding.nameQuran.text = list[position].title

    }

    private fun nextSound() {
        val randomValues = (0 until list.size).random()
        //    if (mediaplayer != null) {
        stopMedia()
        if (shuffleBoolean) {
            position = randomValues
        } else {
            position += 1
            if (position == list.size) {
                position = 0
            }
        }
        val uris = list[position].Path
        CoverImage(uris)

        uri = Uri.parse(uris)
        playMedia(uri)

        if (mediaplayer?.isPlaying == true) {
            binding.playPause.setImageResource(R.drawable.ic_baseline_pause_24)
        } else {
            binding.playPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }

    }

    private fun playNextSound() {
        binding.nextSound.setOnClickListener {
            nextSound()
        }
    }

    private fun previousSound() {
        val randomValues = (0 until list.size).random()
        if (mediaplayer != null) {
            stopMedia()
            if (shuffleBoolean) {
                position = randomValues
            } else {
                position -= 1
                if (position < 0) {
                    binding.playPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    Toast.makeText(this, "No More Sound", Toast.LENGTH_SHORT).show()
                } else {

                    val uris = list[position].Path
                    CoverImage(uris)
                    uri = Uri.parse(uris)
                    playMedia(uri)
                }
                if (mediaplayer?.isPlaying == true) {
                    binding.playPause.setImageResource(R.drawable.ic_baseline_pause_24)
                } else {
                    binding.playPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                }

            }
        }
    }

    private fun playPreviousSound() {
        binding.previousSound.setOnClickListener {
            previousSound()
        }
    }

    private fun playPauseSound() {
        binding.playPause.setOnClickListener {

            if (mediaplayer?.isPlaying == true) {
                binding.playPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                mediaplayer?.pause()
                if (mediaplayer?.duration == null) {
                    Toast.makeText(this, "Could’t play The Sound You Track ", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    binding.seekbar.max = mediaplayer?.duration!!
                }
                changeseekbar()

            } else {

                binding.playPause.setImageResource(R.drawable.ic_baseline_pause_24)
                mediaplayer?.start()
                if (mediaplayer?.duration == null) {
                    Toast.makeText(this, "Could’t play The Sound You Track ", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    binding.seekbar.max = mediaplayer?.duration!!
                }
                changeseekbar()
            }

        }

    }

    private fun startMedia() {
        if (list.isNotEmpty()) {
            binding.playPause.setImageResource(R.drawable.ic_baseline_pause_24)
            uri = Uri.parse(list[position].Path)
        }
        if (mediaplayer != null) {
            stopMedia()
            playMedia(uri)
        } else {
            playMedia(uri)

        }


    }

    private fun getQuaranPhoths(uri: String?): ByteArray? {
        val mediaMetadataRetriever = FFmpegMediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(uri)
        val embeddedPicture = mediaMetadataRetriever.embeddedPicture
        mediaMetadataRetriever.release()
        return embeddedPicture
    }

    private fun imageAnimation(context: Context, imageView: ImageView, bitmap: Bitmap) {
        val animationOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
        val animationIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        animationOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                Glide.with(context).load(bitmap).into(imageView)
                animationIn.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(p0: Animation?) {

                    }

                    override fun onAnimationEnd(p0: Animation?) {

                    }

                    override fun onAnimationRepeat(p0: Animation?) {

                    }

                })
                imageView.startAnimation(animationIn)
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })

        imageView.startAnimation(animationOut)


    }


}