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

package com.stoyanvuchev.magicmessage.presentation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.DrawingController
import com.stoyanvuchev.magicmessage.core.ui.components.DrawingCanvas
import com.stoyanvuchev.magicmessage.core.ui.components.bottom_bar.BottomBar
import com.stoyanvuchev.magicmessage.core.ui.components.bottom_bar.BottomBarItem
import com.stoyanvuchev.magicmessage.core.ui.components.top_bar.TopBar
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import com.stoyanvuchev.magicmessage.core.ui.theme.isInDarkThemeMode
import com.stoyanvuchev.magicmessage.framework.service.ExportDataHolder
import com.stoyanvuchev.magicmessage.framework.service.ExportGifService
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavHost(
    isBoardingComplete: Boolean?,
    navController: NavHostController,
    controller: DrawingController
) {

    val hazeState = rememberHazeState()

    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Theme.colors.surfaceElevationLow,
        contentColor = Theme.colors.onSurfaceElevationLow,
        topBar = {

            TopBar(
                modifier = Modifier
                    .hazeEffect(
                        state = hazeState,
                        style = HazeStyle(
                            tint = HazeTint(
                                color = Theme.colors.surfaceElevationLow.copy(.5f)
                            ),
                            blurRadius = 16.dp,
                            noiseFactor = -.5f,
                            backgroundColor = Theme.colors.surfaceElevationLow

                        )
                    ),
                title = { Text(text = stringResource(R.string.app_name)) },
                navigationAction = {

                    IconButton(onClick = remember { { controller.clear() } }) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            painter = painterResource(R.drawable.arrow_left_outlined),
                            contentDescription = null
                        )
                    }

                },
                actions = {

                    IconButton(onClick = remember { { controller.undo() } }) {
                        Icon(
                            painter = painterResource(R.drawable.undo),
                            contentDescription = null
                        )
                    }

                    IconButton(onClick = remember { { controller.redo() } }) {
                        Icon(
                            painter = painterResource(R.drawable.redo),
                            contentDescription = null
                        )
                    }

                    val context = LocalContext.current

                    OutlinedIconButton(
                        onClick = {

                            ExportDataHolder.strokes = controller.strokes

                            val intent = Intent(context, ExportGifService::class.java).apply {
                                putExtra("width", canvasSize.width)
                                putExtra("height", canvasSize.height)
                            }
                            ContextCompat.startForegroundService(context, intent)


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
                    modifier = Modifier
                        .hazeEffect(
                            state = hazeState,
                            style = HazeStyle(
                                tint = HazeTint(
                                    color = Theme.colors.surfaceElevationLow.copy(.5f)
                                ),
                                blurRadius = 16.dp,
                                noiseFactor = -.5f,
                                backgroundColor = Theme.colors.surfaceElevationLow

                            )
                        )
                ) {

                    BottomBarItem(
                        onClick = remember { {} },
                        label = {
                            Text(text = stringResource(R.string.draw_screen_brush_effect_label))
                        },
                        icon = {

                            Icon(
                                painter = painterResource(R.drawable.swibble_bubbles),
                                contentDescription = null
                            )

                        }
                    )

                    BottomBarItem(
                        onClick = remember { {} },
                        label = {
                            Text(text = stringResource(R.string.draw_screen_brush_width_label))
                        },
                        icon = {

                            Icon(
                                painter = painterResource(R.drawable.brush_medium),
                                contentDescription = null
                            )

                        }
                    )

                    BottomBarItem(
                        onClick = remember { {} },
                        label = {
                            Text(text = stringResource(R.string.draw_screen_color_label))
                        },
                        icon = {

                            val color = Theme.colors.primary
                            val strokeColor = Theme.colors.onSurfaceElevationLow

                            Canvas(
                                modifier = Modifier.size(16.dp)
                            ) {

                                drawCircle(
                                    color = color,
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
                        onClick = remember { {} },
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

//            AppBottomNavBar(
//                navController = navController,
//                hazeState = hazeState
//            )

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
                    .background(
                        if (isInDarkThemeMode()) Color.Black
                        else Color.White
                    )
                    .onGloballyPositioned {
                        canvasSize = it.size
                    },
                controller = controller
            )

            val progress by remember(controller.totalPointCount) {
                derivedStateOf {
                    controller.totalPointCount.toFloat() / controller.maxPoints
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
                color = Theme.colors.primary,
                trackColor = Color.Unspecified,
                drawStopIndicator = {},
                strokeCap = StrokeCap.Square
            )

        }

        /*NavHost(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState),
            navController = navController,
            startDestination = InitialScreen
        ) {

            composable<InitialScreen> {
                LaunchedEffect(isBoardingComplete) {
                    if (isBoardingComplete != null) {
                        navController.navigate(
                            if (isBoardingComplete) MainScreen.Home
                            else BoardingScreen.GetStarted
                        ) {
                            popUpTo(navController.graph.id) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            }

            boardingNavGraph(navController = navController)
            mainNavGraph(navController = navController)

        }*/

    }

}