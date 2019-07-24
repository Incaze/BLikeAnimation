package com.incaze.blikeanimation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Nullable
import java.util.*
import kotlin.math.max
import kotlin.math.min

class ShitDrawing(context: Context, @Nullable attrs: AttributeSet) : View(context, attrs){

    private val firePalette = intArrayOf(
        0xff070707.toInt(),
        0xff0f071f.toInt(),
        0xff1d0731.toInt(),
        0xff240746.toInt(),
        0xff310755.toInt(),
        0xff400769.toInt(),
        0xff450778.toInt(),
        0xff53078d.toInt(),
        0xff6407a2.toInt(),
        0xff7807b0.toInt(),
        0xff8507c0.toInt(),
        0xff8607c5.toInt(),
        0xff9707df.toInt(),
        0xff9e07df.toInt(),
        0xff9e07df.toInt(),
        0xffa507da.toInt(),
        0xffa507da.toInt(),
        0xffa80fd7.toInt(),
        0xffb110d1.toInt(),
        0xffba10d1.toInt(),
        0xffc110d1.toInt(),
        0xffc617cf.toInt(),
        0xffc318c9.toInt(),
        0xffc918c6.toInt(),
        0xffc71fbe.toInt(),
        0xffc11fab.toInt(),
        0xffc11fac.toInt(),
        0xffbe27a2.toInt(),
        0xffbe27b3.toInt(),
        0xffc12fa1.toInt(),
        0xffb62f92.toInt(),
        0xffb62f89.toInt(),
        0xffb9378d.toInt(),
        0xffcf6eae.toInt(),
        0xffdf9fca.toInt(),
        0xfffce6f5.toInt(),
        0xffffffff.toInt()
    )

    private var firePixels: IntArray? = null
    private var fireWidth: Int = 0
    private var fireHeight: Int = 0
    private lateinit var bitmap: Bitmap
    private var paint = Paint()
    private var random = Random()
    private var bitmapPixels: IntArray? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val aspectRatio = w.toFloat() / h.toFloat()
        fireWidth = 200
        fireHeight = (fireWidth / aspectRatio).toInt()
        //fireWidth = w;
        //fireHeight = h;
        firePixels = IntArray(fireWidth * fireHeight)
        bitmap = Bitmap.createBitmap(fireWidth, fireHeight, Bitmap.Config.RGB_565)
        for (x in 0 until fireWidth) {
            firePixels!![x + (fireHeight - 1) * fireWidth] = firePalette.size - 1
        }

    }

    override fun onDraw(canvas: Canvas) {
        spreadFire()
        drawFire(canvas)
        invalidate()
    }

    private fun spreadFire(){
        for (y in 0 until fireHeight - 1) {
            for (x in 0 until fireWidth) {
                val randX = random.nextInt(3)
                val randY = random.nextInt(6)
                val dstX = min(fireWidth - 1, max(0, x + randX - 1))
                val dstY = min(fireHeight - 1, y + randY)
                val deltaFire = -(randX and 1)
                firePixels!![x + y * fireWidth] = max(0, firePixels!![dstX + dstY * fireWidth] + deltaFire)
            }
        }
    }
    private fun drawFire(canvas: Canvas) {
        val pixelCount = fireWidth * fireHeight
        if (bitmapPixels == null || bitmapPixels!!.size < pixelCount) {
            bitmapPixels = IntArray(pixelCount)
        }
        for (x in 0 until fireWidth) {
            for (y in 0 until fireHeight) {
                var temperature = firePixels!![x + y * fireWidth]
                if (temperature < 0) {
                    temperature = 0
                }
                if (temperature >= firePalette.size) {
                    temperature = firePalette.size - 1
                }
                @ColorInt val color = firePalette[temperature]
                bitmapPixels!![fireWidth * y + x] = color
            }
        }
        val scale = width.toFloat() / fireWidth
        canvas.scale(scale, scale)
        bitmap.setPixels(bitmapPixels, 0, fireWidth, 0, 0, fireWidth, fireHeight)
        canvas.drawBitmap(bitmap, 0F , 0F, paint)
    }
}

