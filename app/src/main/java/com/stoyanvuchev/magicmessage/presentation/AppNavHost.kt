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
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.stoyanvuchev.magicmessage.core.ui.navigation.InitialScreen
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import com.stoyanvuchev.magicmessage.core.ui.transition.DefaultNavigationTransitions
import com.stoyanvuchev.magicmessage.framework.export.ExporterState
import com.stoyanvuchev.magicmessage.presentation.boarding.BoardingScreen
import com.stoyanvuchev.magicmessage.presentation.boarding.boardingNavGraph
import com.stoyanvuchev.magicmessage.presentation.main.MainScreen
import com.stoyanvuchev.magicmessage.presentation.main.mainNavGraph

@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavHost(
    navController: NavHostController,
    state: MainActivityState,
    onDismissExportDialog: () -> Unit
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Theme.colors.surfaceElevationLow,
        contentColor = Theme.colors.onSurfaceElevationLow,
        bottomBar = { AppBottomNavBar(navController = navController) }
    ) { _ ->

        NavHost(
            modifier = Modifier.fillMaxSize(),
            enterTransition = remember { { DefaultNavigationTransitions.enter(this) } },
            exitTransition = remember { { DefaultNavigationTransitions.exit(this) } },
            popEnterTransition = remember { { DefaultNavigationTransitions.popEnter(this) } },
            popExitTransition = remember { { DefaultNavigationTransitions.popExit(this) } },
            navController = navController,
            startDestination = InitialScreen
        ) {

            composable<InitialScreen> {
                LaunchedEffect(state.isBoardingComplete) {
                    if (state.isBoardingComplete != null) {
                        navController.navigate(
                            if (state.isBoardingComplete) MainScreen.Home
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

    if (state.exporterState != ExporterState.IDLE) {

        ExportDialog(
            exporterProgress = state.exporterProgress,
            exporterState = state.exporterState,
            exportedUri = state.exportedUri,
            onDismissExportDialog = onDismissExportDialog
        )

    }

}