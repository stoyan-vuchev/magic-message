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

package com.stoyanvuchev.magicmessage.core.ui.components.grid

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.core.ui.components.interaction.rememberRipple
import com.stoyanvuchev.magicmessage.core.ui.ext.drawStroke
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import com.stoyanvuchev.magicmessage.domain.layer.BackgroundLayer
import com.stoyanvuchev.magicmessage.domain.model.CreationModel

@Composable
fun GridCreationItem(
    modifier: Modifier = Modifier,
    creation: CreationModel,
    onCreationClick: (CreationModel, IntSize) -> Unit,
    onCreationLongClick: (CreationModel, IntSize) -> Unit
) {

    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    val scaleX by remember {
        derivedStateOf {
            findScaleFromSize(
                a = creation.drawConfiguration.canvasWidth.toFloat(),
                b = canvasSize.width.toFloat()
            )
        }
    }

    val scaleY by remember {
        derivedStateOf {
            findScaleFromSize(
                a = creation.drawConfiguration.canvasHeight.toFloat(),
                b = canvasSize.height.toFloat()
            )
        }
    }

    Canvas(
        modifier = modifier
            .onPlaced { canvasSize = it.size }
            .background(
                color = Theme.colors.surfaceElevationHigh,
                shape = Theme.shapes.smallShape
            )
            .border(
                width = 1.dp,
                color = Theme.colors.outline
                    .compositeOver(Theme.colors.surfaceElevationLow),
                shape = Theme.shapes.smallShape
            )
            .clip(shape = Theme.shapes.smallShape)
            .clipToBounds()
            .combinedClickable(
                onClick = remember { { onCreationClick(creation, canvasSize) } },
                onLongClick = remember { { onCreationLongClick(creation, canvasSize) } },
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                role = Role.Image
            )
    ) {

        // Draw the background layer.
        when (creation.drawConfiguration.bgLayer) {

            is BackgroundLayer.ColorLayer -> {

                drawRect(
                    color = creation.drawConfiguration.bgLayer.color,
                    size = size
                )

            }

            is BackgroundLayer.LinearGradientLayer -> {

                val brush = Brush.linearGradient(
                    colors = creation.drawConfiguration.bgLayer.colors,
                    start = creation.drawConfiguration.bgLayer.start,
                    end = creation.drawConfiguration.bgLayer.end ?: Offset.Infinite
                )

                drawRect(
                    brush = brush,
                    size = size
                )

            }

            else -> Unit

        }

        scale(
            scaleX = scaleX,
            scaleY = scaleY,
            pivot = Offset.Zero
        ) {

            // Draw completed strokes.
            creation.drawingSnapshot.strokes.forEach { drawStroke(it) }

        }

    }

}

@Stable
private fun findScaleFromSize(
    a: Float,
    b: Float
): Float {
    val min = minOf(a, b)
    val max = maxOf(a, b)
    return min / max
}