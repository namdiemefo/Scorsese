package com.naemo.scorsese.helper

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NoSwipePager(context: Context, attributeSet: AttributeSet) :
    ViewPager(context, attributeSet) {

    private var enabled: Boolean? = false

    init {
        this.enabled = true
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (this.enabled == true) {
            return super.onTouchEvent(ev)
        }
        return false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (this.enabled == true) {
            super.onInterceptTouchEvent(event)
        } else false
    }

    fun setPagingEnabled(enabled: Boolean) {
        this.enabled = enabled
    }
}