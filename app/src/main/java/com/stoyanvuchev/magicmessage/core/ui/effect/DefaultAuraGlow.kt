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

package com.stoyanvuchev.magicmessage.core.ui.effect

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

fun Modifier.outerAuraGlow(
    radius: Dp = 8.dp,
    color: Color,
    centerOffset: IntOffset,
    glowOffset1: Dp,
    glowOffset2: Dp,
    alpha: Float,
    spread: Dp = 0.dp,
    shape: Shape = CircleShape
): Modifier {
    return dropShadow(
        shape = shape,
        shadow = Shadow(
            radius = radius,
            brush = Brush.radialGradient(
                colors = listOf(
                    color,
                    color.copy(.75f),
                    color.copy(0f)
                ),
                center = Offset(
                    x = centerOffset.x.toFloat(),
                    y = centerOffset.y.toFloat()
                )
            ),
            offset = DpOffset(
                x = glowOffset1,
                y = glowOffset1
            ),
            alpha = alpha,
            spread = spread
        )
    ).then(
        Modifier.dropShadow(
            shape = shape,
            shadow = Shadow(
                radius = radius,
                brush = Brush.radialGradient(
                    colors = listOf(
                        color,
                        color.copy(.75f),
                        color.copy(0f)
                    ),
                    center = Offset(
                        x = centerOffset.x.toFloat(),
                        y = centerOffset.y.toFloat()
                    )
                ),
                offset = DpOffset(
                    x = glowOffset2,
                    y = glowOffset2
                ),
                alpha = alpha,
                spread = spread
            )
        )
    )
}

fun Modifier.innerAuraGlow(
    radius: Dp = 8.dp,
    color: Color,
    centerOffset: IntOffset,
    alpha: Float,
    spread: Dp = (-0).dp,
    shape: Shape = CircleShape
): Modifier {
    return innerShadow(
        shape = shape,
        shadow = Shadow(
            radius = radius,
            brush = Brush.radialGradient(
                colors = listOf(
                    color.copy(0f),
                    color
                ),
                center = Offset(
                    x = centerOffset.x.toFloat(),
                    y = centerOffset.y.toFloat()
                )
            ),
            alpha = alpha,
            spread = spread
        )
    )
}