package com.stoyanvuchev.magicmessage.core.ui.ext

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path

fun List<Offset>.toSmoothPath(): Path {

    val path = Path()
    if (isEmpty()) return path

    path.moveTo(
        x = first().x,
        y = first().y
    )

    for (i in 1 until size) {

        val prev = this[i - 1]
        val current = this[i]
        val midPoint = Offset(
            x = (prev.x + current.x) / 2f,
            y = (prev.y + current.y) / 2f
        )

        path.quadraticTo(
            x1 = prev.x,
            y1 = prev.y,
            x2 = midPoint.x,
            y2 = midPoint.y
        )

    }

    // Ensure last point is included.
    path.lineTo(
        x = last().x,
        y = last().y
    )

    return path

}