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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.components.bottom_bar.BottomBar
import com.stoyanvuchev.magicmessage.core.ui.components.bottom_bar.BottomBarItem
import com.stoyanvuchev.magicmessage.core.ui.effect.defaultHazeEffect
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import com.stoyanvuchev.magicmessage.domain.brush.BrushEffect
import com.stoyanvuchev.magicmessage.domain.brush.BrushThickness
import com.stoyanvuchev.magicmessage.presentation.main.draw_screen.dialog.DialogEditType
import dev.chrisbanes.haze.HazeState

@Composable
fun DrawScreenBottomBar(
    hazeState: HazeState,
    state: DrawScreenState,
    onUIAction: (DrawScreenUIAction) -> Unit
) {

    Column(modifier = Modifier.fillMaxWidth()) {

        BottomBar(
            modifier = Modifier.defaultHazeEffect(hazeState = hazeState)
        ) {

            BottomBarItem(
                onClick = remember {
                    {
                        onUIAction(
                            DrawScreenUIAction.SetDialogEditType(
                                DialogEditType.EFFECT
                            )
                        )
                    }
                },
                label = {
                    Text(text = stringResource(R.string.draw_screen_brush_effect_label))
                },
                icon = {

                    val icon by rememberUpdatedState(
                        painterResource(
                            when (state.drawConfiguration.effect) {
                                BrushEffect.NONE -> R.drawable.swibble_line
                                BrushEffect.BUBBLES -> R.drawable.swibble_bubbles
                                BrushEffect.STARS -> R.drawable.swibble_stars
                                BrushEffect.HEARTS -> R.drawable.swibble_hearts
                            }
                        )
                    )

                    Icon(
                        painter = icon,
                        contentDescription = null
                    )

                }
            )

            BottomBarItem(
                onClick = remember {
                    {
                        onUIAction(
                            DrawScreenUIAction.SetDialogEditType(
                                DialogEditType.THICKNESS
                            )
                        )
                    }
                },
                label = {
                    Text(text = stringResource(R.string.draw_screen_brush_width_label))
                },
                icon = {

                    val icon by rememberUpdatedState(
                        painterResource(
                            when (state.drawConfiguration.thickness) {
                                BrushThickness.THIN -> R.drawable.brush_thin
                                BrushThickness.MEDIUM -> R.drawable.brush_medium
                                BrushThickness.LARGE -> R.drawable.brush_medium
                                BrushThickness.THICK -> R.drawable.brush_thick
                            }
                        )
                    )

                    Icon(
                        painter = icon,
                        contentDescription = null
                    )

                }
            )

            BottomBarItem(
                onClick = remember {
                    {
                        onUIAction(
                            DrawScreenUIAction.SetDialogEditType(
                                DialogEditType.COLOR
                            )
                        )
                    }
                },
                label = {
                    Text(text = stringResource(R.string.draw_screen_color_label))
                },
                icon = {

                    val strokeColor = Theme.colors.onSurfaceElevationLow

                    Canvas(
                        modifier = Modifier
                            .size(22.dp)
                    ) {

                        drawCircle(
                            color = state.drawConfiguration.color,
                            radius = 4.dp.toPx(),
                            center = center
                        )

                        drawCircle(
                            color = strokeColor,
                            radius = 8.dp.toPx(),
                            style = Stroke(
                                width = 2.dp.toPx(),
                                cap = StrokeCap.Round
                            ),
                            center = center
                        )

                    }

                }
            )

            BottomBarItem(
                onClick = remember {
                    {
                        onUIAction(
                            DrawScreenUIAction.SetDialogEditType(
                                DialogEditType.BG_LAYER
                            )
                        )
                    }
                },
                label = {
                    Text(text = stringResource(R.string.draw_screen_bg_layer_label))
                },
                icon = {

                    Icon(
                        painter = painterResource(R.drawable.bg_layer),
                        contentDescription = null
                    )

                }
            )

        }

    }

}