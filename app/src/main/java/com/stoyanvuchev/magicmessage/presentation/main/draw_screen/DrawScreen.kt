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

import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.DrawingController
import com.stoyanvuchev.magicmessage.core.ui.components.DrawingCanvas
import com.stoyanvuchev.magicmessage.core.ui.components.bottom_bar.BottomBar
import com.stoyanvuchev.magicmessage.core.ui.components.bottom_bar.BottomBarItem
import com.stoyanvuchev.magicmessage.core.ui.components.top_bar.TopBar
import com.stoyanvuchev.magicmessage.core.ui.effect.defaultHazeEffect
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import com.stoyanvuchev.magicmessage.core.ui.theme.isInDarkThemeMode
import com.stoyanvuchev.magicmessage.domain.brush.BrushEffect
import com.stoyanvuchev.magicmessage.domain.brush.BrushThickness
import com.stoyanvuchev.magicmessage.framework.export.ExporterState
import com.stoyanvuchev.magicmessage.presentation.main.draw_screen.dialog.DialogEditType
import com.stoyanvuchev.magicmessage.presentation.main.draw_screen.dialog.DrawScreenDialog
import com.stoyanvuchev.magicmessage.presentation.main.draw_screen.dialog.DrawScreenExportDialog
import com.stoyanvuchev.systemuibarstweaker.LocalSystemUIBarsTweaker
import com.stoyanvuchev.systemuibarstweaker.ScrimStyle
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawScreen(
    state: DrawScreenState,
    exporterProgress: Int,
    exporterState: ExporterState,
    exportedUri: Uri?,
    drawingController: DrawingController,
    onUIAction: (DrawScreenUIAction) -> Unit
) {

    val hazeState = rememberHazeState()
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    val tweaker = LocalSystemUIBarsTweaker.current
    val darkTheme by rememberUpdatedState(isInDarkThemeMode())
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.PartiallyExpanded }
    )

    DisposableEffect(
        tweaker,
        darkTheme,
        state.dialogEditType
    ) {
        tweaker.tweakSystemBarsStyle(
            statusBarStyle = tweaker.statusBarStyle.copy(
                darkIcons = state.dialogEditType == DialogEditType.NONE && !darkTheme
            ),
            navigationBarStyle = tweaker.navigationBarStyle.copy(
                darkIcons = state.dialogEditType == DialogEditType.NONE && !darkTheme,
                scrimStyle = ScrimStyle.None
            )
        )
        onDispose {
            tweaker.tweakSystemBarsStyle(
                statusBarStyle = tweaker.statusBarStyle.copy(
                    darkIcons = !darkTheme
                ),
                navigationBarStyle = tweaker.navigationBarStyle.copy(
                    darkIcons = !darkTheme,
                    scrimStyle = ScrimStyle.None
                )
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Theme.colors.surfaceElevationLow,
        contentColor = Theme.colors.onSurfaceElevationLow,
        topBar = {

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

                    OutlinedIconButton(
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

        },
        bottomBar = {

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
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState)
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {

            DrawingCanvas(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f)
                    .onGloballyPositioned {
                        canvasSize = it.size
                    },
                controller = drawingController,
                drawConfiguration = state.drawConfiguration
            )

            val progress by remember(drawingController.totalPointCount) {
                derivedStateOf {
                    drawingController.totalPointCount.toFloat() / drawingController.maxPoints
                }
            }

            val animatedProgress by animateFloatAsState(
                targetValue = progress,
                animationSpec = spring()
            )

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 3.dp)
                    .align(Alignment.BottomCenter),
                progress = { animatedProgress },
                color = state.drawConfiguration.color,
                trackColor = Color.Unspecified,
                drawStopIndicator = {},
                strokeCap = StrokeCap.Square
            )

        }

    }

    if (state.dialogEditType != DialogEditType.NONE) {

        DrawScreenDialog(
            onDismiss = {
                scope.launch { sheetState.hide() }
                    .invokeOnCompletion {
                        onUIAction(
                            DrawScreenUIAction.SetDialogEditType(
                                DialogEditType.NONE
                            )
                        )
                    }
            },
            type = state.dialogEditType,
            state = state,
            sheetState = sheetState,
            hazeState = hazeState,
            onUIAction = onUIAction
        )

    }

    if (exporterState != ExporterState.IDLE) {

        DrawScreenExportDialog(
            exporterProgress = exporterProgress,
            exporterState = exporterState,
            exportedUri = exportedUri,
            hazeState = hazeState,
            onUIAction = onUIAction
        )

    }

}