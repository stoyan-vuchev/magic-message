package com.stoyanvuchev.magicmessage.framework

import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.ui.graphics.toArgb
import com.stoyanvuchev.magicmessage.domain.model.Particle
import com.stoyanvuchev.magicmessage.domain.model.StrokeModel

object Renderer {

    fun drawStroke(
        canvas: Canvas,
        path: Path,
        stroke: StrokeModel
    ) {

        val paint = Paint().apply {
            color = stroke.color.toArgb()
            style = Paint.Style.STROKE
            strokeWidth = stroke.width
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            isAntiAlias = true
        }

        canvas.drawPath(path, paint)

    }

    fun drawParticle(
        canvas: Canvas,
        p: Particle
    ) {

        val paint = Paint().apply {
            color = p.color.toArgb()
            alpha = (255 * (1f - p.age.toFloat() / p.lifetime)).coerceIn(0f, 1f).toInt()
            maskFilter = BlurMaskFilter(p.size / 2, BlurMaskFilter.Blur.NORMAL)
            isAntiAlias = true
        }

        canvas.drawCircle(
            p.position.x,
            p.position.y,
            p.size / 2,
            paint
        )

    }

}
