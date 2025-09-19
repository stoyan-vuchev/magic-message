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

package com.stoyanvuchev.magicmessage.core.ui.ext

import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.stoyanvuchev.magicmessage.domain.model.StrokeModel

fun DrawScope.drawStroke(stroke: StrokeModel) {

    // Single tap: draw a circle instead of a path.
    if (stroke.points.size == 1) {

        drawCircle(
            color = stroke.color,
            radius = stroke.width / 2f,
            center = stroke.points.first().offset
        )

        return

    }

    val path = stroke.points.map { it.offset }.toSmoothPath()
    drawPath(
        path = path,
        color = stroke.color,
        style = Stroke(
            width = stroke.width,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )

}