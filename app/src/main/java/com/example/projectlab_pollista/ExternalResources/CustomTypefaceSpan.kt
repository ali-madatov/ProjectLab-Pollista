package com.example.projectlab_pollista.ExternalResources

import android.graphics.Paint
import android.graphics.Typeface

import android.text.TextPaint

import android.text.style.TypefaceSpan


class CustomTypefaceSpan(family: String?, private val newType: Typeface) : TypefaceSpan(family) {
    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeFace(ds, newType)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, newType)
    }

    companion object {
        private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
            val oldStyle: Int
            val old: Typeface = paint.getTypeface()
            oldStyle = old?.style ?: 0
            val fake = oldStyle and tf.style.inv()
            if (fake and Typeface.BOLD != 0) {
                paint.setFakeBoldText(true)
            }
            if (fake and Typeface.ITALIC != 0) {
                paint.setTextSkewX(-0.25f)
            }
            paint.setTypeface(tf)
        }
    }
}