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