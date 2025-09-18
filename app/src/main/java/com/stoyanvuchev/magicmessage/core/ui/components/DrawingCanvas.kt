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

package com.stoyanvuchev.magicmessage.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.core.ui.DrawingController
import com.stoyanvuchev.magicmessage.core.ui.ParticleUpdater
import com.stoyanvuchev.magicmessage.core.ui.ext.drawStroke
import com.stoyanvuchev.magicmessage.domain.BrushType
import com.stoyanvuchev.magicmessage.domain.model.StrokeModel

@Composable
fun DrawingCanvas(
    modifier: Modifier = Modifier,
    controller: DrawingController,
    color: Color = MaterialTheme.colorScheme.primary,
    width: Dp = 5.dp
) {

    ParticleUpdater(controller = controller)

    Canvas(
        modifier = modifier
            .clipToBounds()
            .pointerInput(Unit) {
                if (!controller.drawingEnabled) return@pointerInput
                awaitEachGesture {

                    val down = awaitFirstDown()
                    controller.startStroke(
                        offset = down.position,
                        color = color
                    )

                    drag(down.id) { change ->
                        controller.addPoint(
                            change.position,
                            color = color
                        )
                        change.consume()
                    }

                    controller.endStroke(
                        color = color,
                        width = width.toPx(),
                        brush = BrushType.NORMAL
                    )

                }
            }
    ) {

        // Draw completed strokes.
        controller.strokes.forEach { drawStroke(it) }

        // Draw the current stroke in real-time.
        if (controller.currentPoints.isNotEmpty()) {
            drawStroke(
                StrokeModel(
                    points = controller.currentPoints.toList(),
                    color = color,
                    width = width.toPx(),
                    brush = BrushType.GLOW
                )
            )
        }

        // Draw particles.
        controller.particles.forEach { particle ->
            drawCircle(
                color = particle.color,
                radius = particle.size,
                center = particle.position
            )
        }

    }

}