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
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.stoyanvuchev.magicmessage.core.ui.animation.LocalAnimatedContentScope
import com.stoyanvuchev.magicmessage.core.ui.navigation.InitialScreen
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import com.stoyanvuchev.magicmessage.core.ui.transition.DefaultNavigationTransitions
import com.stoyanvuchev.magicmessage.core.ui.transition.LocalSharedTransitionScope
import com.stoyanvuchev.magicmessage.framework.export.ExporterState
import com.stoyanvuchev.magicmessage.presentation.boarding.BoardingScreen
import com.stoyanvuchev.magicmessage.presentation.boarding.boardingNavGraph
import com.stoyanvuchev.magicmessage.presentation.main.MainScreen
import com.stoyanvuchev.magicmessage.presentation.main.mainNavGraph
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter",
    "UnusedContentLambdaTargetStateParameter"
)
@Composable
fun AppNavHost(
    isBoardingComplete: Boolean?,
    navController: NavHostController,
    exporterState: ExporterState,
    exporterProgress: Int,
    exportedUri: Uri?,
    onDismissExportDialog: () -> Unit
) = SharedTransitionLayout {

    val hazeState = rememberHazeState()

    AnimatedContent(
        targetState = Unit,
        label = "Top level AnimatedContent"
    ) { targetState ->

        CompositionLocalProvider(
            LocalSharedTransitionScope provides this@SharedTransitionLayout,
            LocalAnimatedContentScope provides this@AnimatedContent
        ) {

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = Theme.colors.surfaceElevationLow,
                contentColor = Theme.colors.onSurfaceElevationLow,
                bottomBar = {

                    AppBottomNavBar(
                        navController = navController,
                        hazeState = hazeState
                    )

                }
            ) { _ ->

                NavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .hazeSource(state = hazeState),
                    enterTransition = remember { { DefaultNavigationTransitions.enter(this) } },
                    exitTransition = remember { { DefaultNavigationTransitions.exit(this) } },
                    popEnterTransition = remember { { DefaultNavigationTransitions.popEnter(this) } },
                    popExitTransition = remember { { DefaultNavigationTransitions.popExit(this) } },
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

    }

    if (exporterState != ExporterState.IDLE) {

        ExportDialog(
            exporterProgress = exporterProgress,
            exporterState = exporterState,
            exportedUri = exportedUri,
            hazeState = hazeState,
            onDismissExportDialog = onDismissExportDialog
        )

    }

}