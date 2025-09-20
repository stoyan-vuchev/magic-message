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

package com.stoyanvuchev.magicmessage.presentation.main.draw_screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import com.stoyanvuchev.magicmessage.core.ui.DrawingController
import com.stoyanvuchev.magicmessage.core.ui.ParticleUpdater
import com.stoyanvuchev.magicmessage.core.ui.ext.drawStroke
import com.stoyanvuchev.magicmessage.domain.layer.BackgroundLayer
import com.stoyanvuchev.magicmessage.domain.model.DrawConfiguration
import com.stoyanvuchev.magicmessage.domain.model.StrokeModel

@Composable
fun DrawingCanvas(
    modifier: Modifier = Modifier,
    controller: DrawingController,
    drawConfiguration: DrawConfiguration,
    onUIAction: (DrawScreenUIAction) -> Unit
) {

    ParticleUpdater(
        controller = controller,
        brushEffect = drawConfiguration.effect
    )

    Canvas(
        modifier = modifier
            .clipToBounds()
            .pointerInput(drawConfiguration) {
                if (!controller.drawingEnabled) return@pointerInput
                awaitEachGesture {

                    val down = awaitFirstDown()
                    controller.startStroke(
                        offset = down.position,
                        color = drawConfiguration.color,
                        effect = drawConfiguration.effect
                    )

                    drag(down.id) { change ->
                        controller.addPoint(
                            change.position,
                            color = drawConfiguration.color,
                            effect = drawConfiguration.effect
                        )
                        change.consume()
                    }

                    onUIAction(
                        DrawScreenUIAction.OnStrokeEnded(
                            color = drawConfiguration.color,
                            width = drawConfiguration.thickness.thickness.toPx(),
                            effect = drawConfiguration.effect
                        )
                    )

                }
            }
    ) {

        // Draw the background layer.
        when (drawConfiguration.bgLayer) {

            is BackgroundLayer.ColorLayer -> {

                drawRect(
                    color = drawConfiguration.bgLayer.color,
                    size = size
                )

            }

            is BackgroundLayer.LinearGradientLayer -> {

                val brush = Brush.linearGradient(
                    colors = drawConfiguration.bgLayer.colors,
                    start = drawConfiguration.bgLayer.start,
                    end = drawConfiguration.bgLayer.end ?: Offset.Infinite
                )

                drawRect(
                    brush = brush,
                    size = size
                )

            }

            else -> Unit

        }

        // Draw completed strokes.
        controller.strokes.forEach { drawStroke(it) }

        // Draw the current stroke in real-time.
        if (controller.currentPoints.isNotEmpty()) {
            drawStroke(
                StrokeModel(
                    points = controller.currentPoints.toList(),
                    color = drawConfiguration.color,
                    width = drawConfiguration.thickness.thickness.toPx(),
                    effect = drawConfiguration.effect
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