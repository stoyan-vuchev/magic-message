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