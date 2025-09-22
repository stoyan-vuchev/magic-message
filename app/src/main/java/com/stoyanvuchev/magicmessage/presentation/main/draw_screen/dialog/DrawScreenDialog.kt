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

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.components.RowSelectionItem
import com.stoyanvuchev.magicmessage.core.ui.effect.defaultHazeEffect
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import com.stoyanvuchev.magicmessage.domain.brush.BrushEffect
import com.stoyanvuchev.magicmessage.domain.brush.BrushThickness
import com.stoyanvuchev.magicmessage.domain.defaultDrawColors
import com.stoyanvuchev.magicmessage.presentation.main.draw_screen.DrawScreenState
import com.stoyanvuchev.magicmessage.presentation.main.draw_screen.DrawScreenUIAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawScreenDialog(
    onDismiss: () -> Unit,
    type: DialogEditType,
    state: DrawScreenState,
    sheetState: SheetState,
    onUIAction: (DrawScreenUIAction) -> Unit,
) {

    if (type != DialogEditType.NONE) {

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = Color.Transparent,
            shape = RectangleShape,
            dragHandle = null,
            scrimColor = Color.Black.copy(alpha = .5f),
            contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .systemBarsPadding()
                    .padding(bottom = 100.dp)
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth()
                    .clip(Theme.shapes.mediumShape)
                    .border(
                        width = 1.dp,
                        color = Theme.colors.outline,
                        shape = Theme.shapes.mediumShape
                    )
                    .defaultHazeEffect()
                    .background(color = Theme.colors.surfaceElevationHigh.copy(.25f))
                    .padding(8.dp)
            ) {

                val labelText by rememberUpdatedState(
                    stringResource(
                        when (type) {
                            DialogEditType.NONE -> R.string.draw_screen_brush_effect_label
                            DialogEditType.EFFECT -> R.string.draw_screen_brush_effect_label
                            DialogEditType.THICKNESS -> R.string.draw_screen_brush_width_label
                            DialogEditType.COLOR -> R.string.draw_screen_color_label
                            DialogEditType.BG_LAYER -> R.string.draw_screen_bg_layer_label
                        }
                    )
                )

                Text(
                    modifier = Modifier.padding(8.dp),
                    text = labelText,
                    style = Theme.typefaces.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    when (type) {

                        DialogEditType.NONE -> Unit

                        DialogEditType.EFFECT -> {

                            RowSelectionItem(
                                selected = state.drawConfiguration.effect == BrushEffect.NONE,
                                onClick = remember {
                                    {
                                        onUIAction(
                                            DrawScreenUIAction.SetBrushEffect(
                                                BrushEffect.NONE
                                            )
                                        )
                                    }
                                },
                                label = {

                                    val labelText by rememberUpdatedState(
                                        stringResource(
                                            R.string.draw_screen_brush_effect_none_label
                                        )
                                    )

                                    Text(text = labelText)

                                },
                                icon = {

                                    val icon by rememberUpdatedState(
                                        painterResource(R.drawable.swibble_line)
                                    )

                                    Icon(
                                        painter = icon,
                                        contentDescription = null
                                    )

                                }
                            )

                            RowSelectionItem(
                                selected = state.drawConfiguration.effect == BrushEffect.BUBBLES,
                                onClick = remember {
                                    {
                                        onUIAction(
                                            DrawScreenUIAction.SetBrushEffect(
                                                BrushEffect.BUBBLES
                                            )
                                        )
                                    }
                                },
                                label = {

                                    val labelText by rememberUpdatedState(
                                        stringResource(
                                            R.string.draw_screen_brush_effect_bubbles_label
                                        )
                                    )

                                    Text(text = labelText)

                                },
                                icon = {

                                    val icon by rememberUpdatedState(
                                        painterResource(R.drawable.swibble_bubbles)
                                    )

                                    Icon(
                                        painter = icon,
                                        contentDescription = null
                                    )

                                }
                            )

                        }

                        DialogEditType.THICKNESS -> {

                            BrushThickness.entries.forEach { thickness ->

                                RowSelectionItem(
                                    selected = thickness == state.drawConfiguration.thickness,
                                    onClick = remember {
                                        { onUIAction(DrawScreenUIAction.SetBrushThickness(thickness)) }
                                    },
                                    label = {

                                        val labelText by rememberUpdatedState(
                                            stringResource(
                                                when (thickness) {
                                                    BrushThickness.THIN -> R.string.draw_screen_brush_width_thin_label
                                                    BrushThickness.MEDIUM -> R.string.draw_screen_brush_width_medium_label
                                                    BrushThickness.LARGE -> R.string.draw_screen_brush_width_large_label
                                                    BrushThickness.THICK -> R.string.draw_screen_brush_width_thick_label
                                                }
                                            )
                                        )

                                        Text(text = labelText)

                                    },
                                    icon = {

                                        val icon by rememberUpdatedState(
                                            painterResource(
                                                when (thickness) {
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

                            }

                        }

                        DialogEditType.COLOR -> {

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                defaultDrawColors.forEach { color ->

                                    DrawScreenDialogColorsItem(
                                        selected = color == state.drawConfiguration.color,
                                        onClick = remember {
                                            { onUIAction(DrawScreenUIAction.SetBrushColor(color)) }
                                        },
                                        color = color
                                    )

                                }

                            }

                        }

                        DialogEditType.BG_LAYER -> {

                            DrawScreenDialogBGLayerItem(
                                backgroundLayer = state.drawConfiguration.bgLayer,
                                onUIAction = onUIAction
                            )

                        }

                    }

                }

            }

        }

    }

}