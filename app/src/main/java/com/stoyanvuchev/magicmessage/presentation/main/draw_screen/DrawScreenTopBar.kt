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

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.DrawingController
import com.stoyanvuchev.magicmessage.core.ui.components.top_bar.TopBar
import com.stoyanvuchev.magicmessage.core.ui.effect.defaultAuraGlow
import com.stoyanvuchev.magicmessage.core.ui.effect.defaultHazeEffect
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import dev.chrisbanes.haze.HazeState

@Composable
fun DrawScreenTopBar(
    hazeState: HazeState,
    state: DrawScreenState,
    drawingController: DrawingController,
    canvasSize: IntSize,
    onUIAction: (DrawScreenUIAction) -> Unit
) {

    TopBar(
        modifier = Modifier.defaultHazeEffect(hazeState = hazeState),
        title = { Text(text = stringResource(R.string.app_name)) },
        navigationAction = {

            IconButton(onClick = remember { { drawingController.clear() } }) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(R.drawable.arrow_left_outlined),
                    contentDescription = null
                )
            }

        },
        actions = {

            IconButton(
                onClick = remember { { onUIAction(DrawScreenUIAction.Undo) } },
            ) {
                Icon(
                    painter = painterResource(R.drawable.undo),
                    contentDescription = null
                )
            }

            IconButton(
                onClick = remember { { onUIAction(DrawScreenUIAction.Redo) } }
            ) {
                Icon(
                    painter = painterResource(R.drawable.redo),
                    contentDescription = null
                )
            }

            var exportOffset by remember { mutableStateOf(IntOffset.Zero) }
            val animated by rememberUpdatedState(
                drawingController.totalPointCount == drawingController.maxPoints
            )

            val transition = rememberInfiniteTransition(
                label = "export-button-transition"
            )

            val glowOffsetX1 by transition.animateValue(
                initialValue = 0.dp,
                targetValue = 0.dp,
                typeConverter = Dp.VectorConverter,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        (-2).dp at 0
                        2.dp at 1000
                        durationMillis = 2000
                    },
                    repeatMode = RepeatMode.Reverse
                )

            )

            val glowOffsetX2 by transition.animateValue(
                initialValue = 0.dp,
                targetValue = 0.dp,
                typeConverter = Dp.VectorConverter,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        2.dp at 0
                        (-2).dp at 1000
                        durationMillis = 2000
                    },
                    repeatMode = RepeatMode.Reverse
                )

            )

            val alpha by animateFloatAsState(
                targetValue = if (animated) 1f else 0f,
                animationSpec = tween(durationMillis = 360)
            )

            OutlinedIconButton(
                modifier = Modifier
                    .onSizeChanged { exportOffset = it.center }
                    .defaultAuraGlow(
                        color = state.drawConfiguration.color,
                        centerOffset = exportOffset,
                        glowOffset = glowOffsetX1,
                        alpha = alpha
                    )
                    .defaultAuraGlow(
                        color = state.drawConfiguration.color,
                        centerOffset = exportOffset,
                        glowOffset = glowOffsetX2,
                        alpha = alpha,
                        spread = (-1).dp
                    ),
                onClick = {
                    onUIAction(
                        DrawScreenUIAction.Export(
                            width = canvasSize.width,
                            height = canvasSize.height
                        )
                    )
                },
                colors = IconButtonDefaults.outlinedIconButtonColors(
                    containerColor = Theme.colors.surfaceElevationHigh,
                    contentColor = Theme.colors.onSurfaceElevationHigh
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = Theme.colors.outline
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.export),
                    contentDescription = null
                )
            }

        }
    )

}