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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.components.bottom_bar.BottomBar
import com.stoyanvuchev.magicmessage.core.ui.components.bottom_bar.BottomBarItem
import com.stoyanvuchev.magicmessage.core.ui.components.top_bar.TopBar
import com.stoyanvuchev.magicmessage.core.ui.navigation.InitialScreen
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import com.stoyanvuchev.magicmessage.presentation.boarding.BoardingScreen
import com.stoyanvuchev.magicmessage.presentation.boarding.boardingNavGraph
import com.stoyanvuchev.magicmessage.presentation.main.MainScreen
import com.stoyanvuchev.magicmessage.presentation.main.mainNavGraph
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavHost(
    isBoardingComplete: Boolean?,
    navController: NavHostController
) {

    val hazeState = rememberHazeState()

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

                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.menu_outlined),
                            contentDescription = null
                        )
                    }

                },
                actions = {

                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.favorite_outlined),
                            contentDescription = null
                        )
                    }

                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.swibble_line),
                            contentDescription = null
                        )
                    }

                }
            )

        },
        bottomBar = {

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

//            AppBottomNavBar(
//                navController = navController,
//                hazeState = hazeState
//            )

        }
    ) { _ ->

        NavHost(
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

        }

    }

}