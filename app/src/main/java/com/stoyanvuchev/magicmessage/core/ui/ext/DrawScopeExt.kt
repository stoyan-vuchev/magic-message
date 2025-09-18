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

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.stoyanvuchev.magicmessage.domain.BrushType
import com.stoyanvuchev.magicmessage.domain.model.StrokeModel

fun DrawScope.drawStroke(stroke: StrokeModel) {

    // Single tap: draw a circle instead of a path.
    if (stroke.points.size == 1) {

        if (stroke.brush == BrushType.GLOW) {

            drawCircle(
                color = stroke.color.copy(alpha = .25f),
                radius = stroke.width * 1.5f,
                center = stroke.points.first().offset
            )

            drawCircle(
                color = stroke.color.copy(alpha = .25f),
                radius = stroke.width,
                center = stroke.points.first().offset
            )

        }

        drawCircle(
            color = stroke.color,
            radius = stroke.width / 2f,
            center = stroke.points.first().offset
        )

        return

    }

    val path = stroke.points.map { it.offset }.toSmoothPath()
    when (stroke.brush) {

        BrushType.NORMAL -> {
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

        BrushType.GLOW -> {
            drawGlowPath(
                path = path,
                color = stroke.color,
                width = stroke.width
            )
        }

    }

}

fun DrawScope.drawGlowPath(
    path: Path,
    color: Color,
    width: Float
) {

    drawPath(
        path = path,
        color = color.copy(alpha = .25f),
        style = Stroke(
            width = width * 3f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )

    drawPath(
        path = path,
        color = color.copy(alpha = .25f),
        style = Stroke(
            width = width * 2f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )

    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = width,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )

}