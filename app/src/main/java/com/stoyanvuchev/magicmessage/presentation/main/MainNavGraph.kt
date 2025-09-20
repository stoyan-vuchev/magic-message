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

package com.stoyanvuchev.magicmessage.presentation.main

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent
import com.stoyanvuchev.magicmessage.framework.service.ExportGifService
import com.stoyanvuchev.magicmessage.presentation.main.draw_screen.DrawScreen
import com.stoyanvuchev.magicmessage.presentation.main.draw_screen.DrawScreenUIAction
import com.stoyanvuchev.magicmessage.presentation.main.draw_screen.DrawScreenViewModel
import com.stoyanvuchev.magicmessage.presentation.main.home_screen.HomeScreen
import com.stoyanvuchev.magicmessage.presentation.main.home_screen.HomeScreenUIAction
import com.stoyanvuchev.magicmessage.presentation.main.home_screen.HomeScreenViewModel
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {

    navigation<MainScreen.Navigation>(
        startDestination = MainScreen.Home
    ) {

        composable<MainScreen.Home> {

            val viewModel = hiltViewModel<HomeScreenViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(viewModel.uiActionFlow) {
                viewModel.uiActionFlow.collectLatest { action ->
                    when (action) {
                        is HomeScreenUIAction.NewMessage -> Unit
                    }
                }
            }

            LaunchedEffect(viewModel.navigationFlow) {
                viewModel.navigationFlow.collectLatest { event ->
                    when (event) {

                        is NavigationEvent.NavigateTo -> {
                            navController.navigate(event.screen) {
                                launchSingleTop = true
                            }
                        }

                        else -> Unit

                    }
                }
            }

            HomeScreen(
                state = state,
                onUIAction = viewModel::onUIAction,
                onNavigationEvent = viewModel::onNavigationEvent
            )

        }

        composable<MainScreen.Favorite> {}

        composable<MainScreen.Menu> {}

        composable<MainScreen.Draw> {

            val viewModel = hiltViewModel<DrawScreenViewModel>()
            val context = LocalContext.current

            val state by viewModel.state.collectAsStateWithLifecycle()
            val drawingController by viewModel.drawingController.collectAsStateWithLifecycle()

            val exporterProgress by viewModel.exporterProgress.collectAsStateWithLifecycle()
            val exporterState by viewModel.exporterState.collectAsStateWithLifecycle()
            val exportedUri by viewModel.exportedUri.collectAsStateWithLifecycle()

            BackHandler(enabled = true) {
                viewModel.onNavigationEvent(NavigationEvent.NavigateUp)
            }

            LaunchedEffect(Unit) {
                viewModel.uiActionFlow.collectLatest { action ->
                    when (action) {

                        is DrawScreenUIAction.Export -> {
                            Intent(
                                context,
                                ExportGifService::class.java
                            ).apply {
                                putExtra("width", action.width)
                                putExtra("height", action.height)
                                putExtra("messageId", action.messageId)
                            }.also { context.startForegroundService(it) }
                        }

                        else -> Unit

                    }
                }
            }

            LaunchedEffect(viewModel.navigationEventFlow) {
                viewModel.navigationEventFlow.collectLatest { event ->
                    when (event) {

                        is NavigationEvent.NavigateUp -> {
                            navController.navigate(MainScreen.Home) {
                                popUpTo(navController.graph.id) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }

                        else -> Unit

                    }
                }
            }

            DrawScreen(
                state = state,
                exporterProgress = exporterProgress,
                exporterState = exporterState,
                exportedUri = exportedUri,
                drawingController = drawingController,
                onUIAction = viewModel::onUIAction,
                onNavigationEvent = viewModel::onNavigationEvent
            )

        }

    }

}