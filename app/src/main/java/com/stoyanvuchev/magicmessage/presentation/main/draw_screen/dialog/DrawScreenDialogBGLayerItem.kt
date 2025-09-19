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

package com.stoyanvuchev.magicmessage.presentation.main.draw_screen.dialog

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.components.interaction.rememberRipple
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import com.stoyanvuchev.magicmessage.core.ui.theme.isInDarkThemeMode
import com.stoyanvuchev.magicmessage.domain.DefaultBGLayers
import com.stoyanvuchev.magicmessage.domain.layer.BackgroundLayer
import com.stoyanvuchev.magicmessage.presentation.main.draw_screen.DrawScreenUIAction

@Composable
fun DrawScreenDialogBGLayerItem(
    backgroundLayer: BackgroundLayer,
    onUIAction: (DrawScreenUIAction) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = stringResource(R.string.draw_screen_bg_layer_colors_label),
            style = Theme.typefaces.bodySmall,
            color = Theme.colors.onSurfaceElevationLow.copy(.5f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            DefaultBGLayers.colorLayers.forEach { layer ->

                val bgColor by animateColorAsState(
                    targetValue = Theme.colors.surfaceElevationHigh.copy(
                        alpha = if (layer == backgroundLayer) .75f else 0f
                    )
                )

                val borderColor by animateColorAsState(
                    targetValue = Theme.colors.outline.copy(
                        alpha = if (layer == backgroundLayer) (if (isInDarkThemeMode()) .04f else 1f)
                        else 0f
                    )
                )

                Box(
                    modifier = Modifier
                        .defaultMinSize(48.dp)
                        .weight(1f)
                        .aspectRatio(1f)
                        .clickable(
                            onClick = remember {
                                {
                                    onUIAction(
                                        DrawScreenUIAction.SetBGLayer(layer)
                                    )
                                }
                            },
                            indication = rememberRipple(),
                            interactionSource = remember { MutableInteractionSource() },
                            role = Role.Tab
                        )
                        .border(
                            width = 1.dp,
                            color = borderColor,
                            shape = Theme.shapes.smallShape
                        )
                        .background(
                            color = bgColor,
                            shape = Theme.shapes.smallShape
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize()
                            .border(
                                width = 1.dp,
                                color = Theme.colors.onSurfaceElevationLow.copy(.5f),
                                shape = CircleShape
                            )
                            .padding(3.dp)
                            .background(
                                color = layer.color,
                                shape = CircleShape
                            )
                    )

                }

            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = stringResource(R.string.draw_screen_bg_layer_gradients_label),
            style = Theme.typefaces.bodySmall,
            color = Theme.colors.onSurfaceElevationLow.copy(.5f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            DefaultBGLayers.linearGradientLayers.forEach { layer ->

                val bgColor by animateColorAsState(
                    targetValue = Theme.colors.surfaceElevationHigh.copy(
                        alpha = if (layer == backgroundLayer) .75f else 0f
                    )
                )

                val borderColor by animateColorAsState(
                    targetValue = Theme.colors.outline.copy(
                        alpha = if (layer == backgroundLayer) (if (isInDarkThemeMode()) .04f else 1f)
                        else 0f
                    )
                )

                Box(
                    modifier = Modifier
                        .defaultMinSize(64.dp)
                        .weight(1f)
                        .aspectRatio(1f)
                        .clickable(
                            onClick = remember {
                                {
                                    onUIAction(
                                        DrawScreenUIAction.SetBGLayer(layer)
                                    )
                                }
                            },
                            indication = rememberRipple(),
                            interactionSource = remember { MutableInteractionSource() },
                            role = Role.Tab
                        )
                        .border(
                            width = 1.dp,
                            color = borderColor,
                            shape = Theme.shapes.smallShape
                        )
                        .background(
                            color = bgColor,
                            shape = Theme.shapes.smallShape
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize()
                            .border(
                                width = 1.dp,
                                color = Theme.colors.onSurfaceElevationLow.copy(.5f),
                                shape = Theme.shapes.smallShape
                            )
                            .padding(3.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = layer.colors,
                                    start = layer.start,
                                    end = layer.end ?: Offset.Infinite
                                ),
                                shape = Theme.shapes.verySmallShape
                            )
                    )

                }

            }

        }

    }

}