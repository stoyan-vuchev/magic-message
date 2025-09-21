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

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.core.ui.DrawingController
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import com.stoyanvuchev.magicmessage.core.ui.theme.isInDarkThemeMode
import com.stoyanvuchev.magicmessage.presentation.main.draw_screen.dialog.DialogEditType
import com.stoyanvuchev.magicmessage.presentation.main.draw_screen.dialog.DrawScreenDialog
import com.stoyanvuchev.systemuibarstweaker.LocalSystemUIBarsTweaker
import com.stoyanvuchev.systemuibarstweaker.ScrimStyle
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun DrawScreen(
    state: DrawScreenState,
    drawingController: DrawingController,
    onUIAction: (DrawScreenUIAction) -> Unit,
    onNavigationEvent: (NavigationEvent) -> Unit
) {

    val hazeState = rememberHazeState()
    val tweaker = LocalSystemUIBarsTweaker.current
    val darkTheme by rememberUpdatedState(isInDarkThemeMode())
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { v -> v != SheetValue.PartiallyExpanded }
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

            DrawScreenTopBar(
                state = state,
                hazeState = hazeState,
                drawingController = drawingController,
                onUIAction = onUIAction,
                onNavigationEvent = onNavigationEvent
            )

        },
        bottomBar = {

            DrawScreenBottomBar(
                hazeState = hazeState,
                state = state,
                onUIAction = onUIAction
            )

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
                onSizeChanged = { size ->
                    onUIAction(
                        DrawScreenUIAction.UpdateCanvasSize(
                            width = size.width,
                            height = size.height
                        )
                    )
                },
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f),
                controller = drawingController,
                drawConfiguration = state.drawConfiguration,
                onUIAction = onUIAction
            )

            val progress by remember(drawingController.totalPointCount) {
                derivedStateOf {
                    drawingController.totalPointCount.toFloat() / DrawingController.MAX_POINTS
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

}