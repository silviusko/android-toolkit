package com.ktt.toolkit.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by luke.kao on 2019/1/16.
 */
private const val VIEW_PORT_WIDTH = 100f
private const val VIEW_PORT_HEIGHT = 100f

class GradientMaskImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    private var linearGradient: LinearGradient? = null
    private var localMatrix = Matrix()
    private var saveLayerPaint = Paint()
    private var imagePaint = Paint().apply {
        isAntiAlias = true
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
    }

    override fun onDraw(canvas: Canvas?) {
        // this must be saved before super.onDraw()
        canvas?.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), saveLayerPaint)

        super.onDraw(canvas)

        val saveCount = canvas?.save() ?: return

        linearGradient?.apply {
            localMatrix.reset()
            localMatrix.setScale(width / VIEW_PORT_WIDTH, height / VIEW_PORT_HEIGHT)
            this.setLocalMatrix(localMatrix)

            imagePaint.shader = this
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), imagePaint)
        }

        canvas.restoreToCount(saveCount)
    }

    fun setGradient(from: Int, to: Int) {
        linearGradient = LinearGradient(
                VIEW_PORT_WIDTH / 2,
                0f,
                VIEW_PORT_WIDTH / 2,
                VIEW_PORT_HEIGHT,
                from,
                to,
                Shader.TileMode.CLAMP
        )
    }
}