package com.xayah.imghelper.Component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.core.widget.NestedScrollView


class MyScrollView : NestedScrollView {
    private var inner: View? = null
    private var mY = 0f
    private val normal = Rect()
    private var onSlideListener: OnSlideListenerInterface? = null

    fun setOnSlideListener(onSlideListener: OnSlideListenerInterface?) {
        this.onSlideListener = onSlideListener
    }

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    override fun onFinishInflate() {
        if (childCount > 0) {
            inner = getChildAt(0)
        }
        super.onFinishInflate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (inner == null) {
            return super.onTouchEvent(event)
        } else {
            commOnTouchEvent(event)
        }

        return super.onTouchEvent(event)
    }

    private fun commOnTouchEvent(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> mY = event.y
            MotionEvent.ACTION_UP -> if (isNeedAnimation) {
                animation()
                if (onSlideListener != null) {
                    onSlideListener!!.onSlideListener(mY)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val preY = mY
                val nowY = event.y
                val deltaY = (preY - nowY).toInt() / size
                mY = nowY
                if (isNeedMove) {
                    if (normal.isEmpty) {
                        normal[inner!!.left, inner!!.top, inner!!.right] = inner!!.bottom
                        return
                    }
                    val yy = inner!!.top - deltaY
                    inner!!.layout(
                        inner!!.left, yy, inner!!.right,
                        inner!!.bottom - deltaY
                    )
                }
            }
            else -> {
            }
        }
    }

    private fun animation() {
        val translateAnimation = TranslateAnimation(
            0F, 0F, inner!!.top.toFloat(),
            normal.top.toFloat()
        )
        translateAnimation.duration = 200
        inner!!.startAnimation(translateAnimation)
        inner!!.layout(normal.left, normal.top, normal.right, normal.bottom)
        normal.setEmpty()
    }

    private val isNeedAnimation: Boolean
        get() = !normal.isEmpty
    private val isNeedMove: Boolean
        get() {
            val offset = inner!!.measuredHeight - height
            val scrollY = scrollY
            return scrollY == 0 || scrollY == offset
        }

    interface OnSlideListenerInterface {
        fun onSlideListener(coordinate: Float)
    }

    companion object {
        private const val size = 4
    }
}