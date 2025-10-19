/*
 * MIT License
 *
 * Copyright (c) 2025 Stoyan Vuchev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.stoyanvuchev.magicmessage.framework

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
            isFilterBitmap = false
            isAntiAlias = true
        }

        canvas.drawPath(path, paint)

    }

    fun drawParticle(
        canvas: Canvas,
        p: Particle
    ) {

        val paint = Paint().apply {
            this.color = p.color.toArgb()
            this.style = Paint.Style.FILL
            this.isFilterBitmap = false
            this.isAntiAlias = true
        }

        // Draw particle
        canvas.drawCircle(
            p.position.x,
            p.position.y,
            p.size,
            paint
        )

    }

}
