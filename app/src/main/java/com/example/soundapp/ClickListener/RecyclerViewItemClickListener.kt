package com.example.soundapp.ClickListener

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener


open class RecyclerViewItemClickListener(
    context: Context?,
    recyclerView: RecyclerView,
    clickListener: ClickListener?
) : OnItemTouchListener {

    //GestureDetector to detect touch event.
    private val gestureDetector: GestureDetector
    private val clickListener: ClickListener? = clickListener

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        //On Touch event
        val child: View? = rv.findChildViewUnder(e.x, e.y)
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, rv.getChildLayoutPosition(child))
        }
        return false
    }
    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    init {
        gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                //Find child on x and y position relative to screen
                val child: View? = recyclerView.findChildViewUnder(e.x, e.y)
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child))
                }
            }
        })
    }
}

