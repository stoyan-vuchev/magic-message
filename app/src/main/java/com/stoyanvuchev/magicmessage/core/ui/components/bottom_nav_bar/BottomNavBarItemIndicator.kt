package com.stoyanvuchev.magicmessage.core.ui.components.bottom_nav_bar

import androidx.compose.ui.graphics.Path

internal fun bumpPath(
    centerX: Float,
    width: Float,
    height: Float,
    totalWidth: Float
): Path = Path().apply {

    moveTo(0f, 0f)
    lineTo(centerX - width / 2f, 0f)

    // Left curve
    cubicTo(
        centerX - width / 4f, 0f,
        centerX - width / 4f, height,
        centerX, height
    )

    // Right curve
    cubicTo(
        centerX + width / 4f, height,
        centerX + width / 4f, 0f,
        centerX + width / 2f, 0f
    )

    lineTo(totalWidth, 0f)
    lineTo(0f, 0f)
    close()

}