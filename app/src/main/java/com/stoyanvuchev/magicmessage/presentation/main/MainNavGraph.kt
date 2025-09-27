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

import androidx.activity.compose.BackHandler
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent
import com.stoyanvuchev.magicmessage.framework.service.ExportGifServiceIntent
import com.stoyanvuchev.magicmessage.presentation.main.about_screen.AboutScreen
import com.stoyanvuchev.magicmessage.presentation.main.about_screen.AboutScreenViewModel
import com.stoyanvuchev.magicmessage.presentation.main.deleted_screen.DeletedScreen
import com.stoyanvuchev.magicmessage.presentation.main.deleted_screen.DeletedScreenViewModel
import com.stoyanvuchev.magicmessage.presentation.main.draw_screen.DrawScreen
import com.stoyanvuchev.magicmessage.presentation.main.draw_screen.DrawScreenUIAction
import com.stoyanvuchev.magicmessage.presentation.main.draw_screen.DrawScreenViewModel
import com.stoyanvuchev.magicmessage.presentation.main.favorite_screen.FavoriteScreen
import com.stoyanvuchev.magicmessage.presentation.main.favorite_screen.FavoriteScreenUIAction
import com.stoyanvuchev.magicmessage.presentation.main.favorite_screen.FavoriteScreenViewModel
import com.stoyanvuchev.magicmessage.presentation.main.home_screen.HomeScreen
import com.stoyanvuchev.magicmessage.presentation.main.home_screen.HomeScreenUIAction
import com.stoyanvuchev.magicmessage.presentation.main.home_screen.HomeScreenViewModel
import com.stoyanvuchev.magicmessage.presentation.main.menu_screen.MenuScreen
import com.stoyanvuchev.magicmessage.presentation.main.menu_screen.MenuScreenViewModel
import com.stoyanvuchev.magicmessage.presentation.main.transitions.FavoriteNavigationTransitions
import com.stoyanvuchev.magicmessage.presentation.main.transitions.HomeNavigationTransitions
import com.stoyanvuchev.magicmessage.presentation.main.transitions.MenuNavigationTransitions
import com.stoyanvuchev.magicmessage.presentation.main.transitions.safeRoute
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {

    navigation<MainScreen.Navigation>(
        startDestination = MainScreen.Home
    ) {

        composable<MainScreen.Home>(
            enterTransition = {
                HomeNavigationTransitions.enter(
                    scope = this,
                    initialRoute = initialState.safeRoute(),
                    targetRoute = targetState.safeRoute()
                )
            },
            exitTransition = {
                HomeNavigationTransitions.exit(
                    scope = this,
                    initialRoute = initialState.safeRoute(),
                    targetRoute = targetState.safeRoute()
                )
            },
            popEnterTransition = {
                HomeNavigationTransitions.popEnter(
                    scope = this,
                    initialRoute = initialState.safeRoute(),
                    targetRoute = targetState.safeRoute()
                )
            },
            popExitTransition = {
                HomeNavigationTransitions.popExit(
                    scope = this,
                    initialRoute = initialState.safeRoute(),
                    targetRoute = targetState.safeRoute()
                )
            }
        ) {

            val viewModel = hiltViewModel<HomeScreenViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            val context = LocalContext.current

            LaunchedEffect(viewModel.uiActionFlow) {
                viewModel.uiActionFlow.collectLatest { action ->
                    when (action) {

                        is HomeScreenUIAction.ExportGif -> {
                            ExportGifServiceIntent.start(
                                context = context,
                                messageId = action.creation.id,
                                width = action.creation.drawConfiguration.canvasWidth,
                                height = action.creation.drawConfiguration.canvasHeight
                            )
                        }

                        else -> Unit

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

    }

    composable<MainScreen.Favorite>(
        enterTransition = {
            FavoriteNavigationTransitions.enter(
                scope = this,
                initialRoute = initialState.safeRoute(),
                targetRoute = targetState.safeRoute()
            )
        },
        exitTransition = {
            FavoriteNavigationTransitions.exit(
                scope = this,
                initialRoute = initialState.safeRoute(),
                targetRoute = targetState.safeRoute()
            )
        },
        popEnterTransition = {
            FavoriteNavigationTransitions.popEnter(
                scope = this,
                initialRoute = initialState.safeRoute(),
                targetRoute = targetState.safeRoute()
            )
        },
        popExitTransition = {
            FavoriteNavigationTransitions.popExit(
                scope = this,
                initialRoute = initialState.safeRoute(),
                targetRoute = targetState.safeRoute()
            )
        }
    ) {

        val viewModel = hiltViewModel<FavoriteScreenViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val context = LocalContext.current

        LaunchedEffect(viewModel.uiActionFlow) {
            viewModel.uiActionFlow.collectLatest { action ->
                when (action) {

                    is FavoriteScreenUIAction.ExportGif -> {
                        ExportGifServiceIntent.start(
                            context = context,
                            messageId = action.creation.id,
                            width = action.creation.drawConfiguration.canvasWidth,
                            height = action.creation.drawConfiguration.canvasHeight
                        )
                    }

                    else -> Unit

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

        FavoriteScreen(
            state = state,
            onUIAction = viewModel::onUIAction,
            onNavigationEvent = viewModel::onNavigationEvent
        )

    }

    composable<MainScreen.Menu>(
        enterTransition = {
            MenuNavigationTransitions.enter(
                scope = this,
                initialRoute = initialState.safeRoute(),
                targetRoute = targetState.safeRoute()
            )
        },
        exitTransition = {
            MenuNavigationTransitions.exit(
                scope = this,
                initialRoute = initialState.safeRoute(),
                targetRoute = targetState.safeRoute()
            )
        },
        popEnterTransition = {
            MenuNavigationTransitions.popEnter(
                scope = this,
                initialRoute = initialState.safeRoute(),
                targetRoute = targetState.safeRoute()
            )
        },
        popExitTransition = {
            MenuNavigationTransitions.popExit(
                scope = this,
                initialRoute = initialState.safeRoute(),
                targetRoute = targetState.safeRoute()
            )
        }
    ) {

        val viewModel = hiltViewModel<MenuScreenViewModel>()

        LaunchedEffect(viewModel.navigationEventFlow) {
            viewModel.navigationEventFlow.collectLatest { event ->
                when (event) {
                    is NavigationEvent.NavigateUp -> Unit
                    is NavigationEvent.NavigateTo -> {
                        navController.navigate(event.screen) {
                            launchSingleTop = true
                        }
                    }
                }
            }
        }

        MenuScreen(
            onUIAction = viewModel::onUIAction,
            onNavigationEvent = viewModel::onNavigationEvent
        )

    }

    composable<MainScreen.Draw>(
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() }
    ) {

        val viewModel = hiltViewModel<DrawScreenViewModel>()
        val context = LocalContext.current

        val state by viewModel.state.collectAsStateWithLifecycle()
        val drawingController by viewModel.drawingController.collectAsStateWithLifecycle()

        BackHandler(enabled = true) {
            viewModel.onNavigationEvent(NavigationEvent.NavigateUp)
        }

        LaunchedEffect(Unit) {
            viewModel.uiActionFlow.collectLatest { action ->
                when (action) {

                    is DrawScreenUIAction.Export -> {
                        ExportGifServiceIntent.start(
                            context = context,
                            messageId = state.messageId,
                            width = state.drawConfiguration.canvasWidth,
                            height = state.drawConfiguration.canvasHeight
                        )
                    }

                    else -> Unit

                }
            }
        }

        LaunchedEffect(viewModel.navigationEventFlow) {
            viewModel.navigationEventFlow.collectLatest { event ->
                when (event) {

                    is NavigationEvent.NavigateUp -> {
                        navController.navigateUp()
                    }

                    else -> Unit

                }
            }
        }

        DrawScreen(
            state = state,
            drawingController = drawingController,
            onUIAction = viewModel::onUIAction,
            onNavigationEvent = viewModel::onNavigationEvent
        )

    }

    composable<MainScreen.Deleted> {

        val viewModel = hiltViewModel<DeletedScreenViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(viewModel.navigationEventFlow) {
            viewModel.navigationEventFlow.collectLatest { event ->
                when (event) {
                    is NavigationEvent.NavigateUp -> navController.navigateUp()
                    else -> Unit
                }
            }
        }

        DeletedScreen(
            state = state,
            onUIAction = viewModel::onUIAction,
            onNavigationEvent = viewModel::onNavigationEvent
        )

    }

    composable<MainScreen.About> {

        val viewModel = hiltViewModel<AboutScreenViewModel>()
        val uriHandler = LocalUriHandler.current

        LaunchedEffect(viewModel.uiActionFlow) {
            viewModel.uiActionFlow.collectLatest { action ->
                uriHandler.openUri(action.url)
            }
        }

        LaunchedEffect(viewModel.navigationEventFlow) {
            viewModel.navigationEventFlow.collectLatest { event ->
                when (event) {
                    is NavigationEvent.NavigateUp -> navController.navigateUp()
                    is NavigationEvent.NavigateTo -> {
                        navController.navigate(event.screen) {
                            launchSingleTop = true
                        }
                    }
                }
            }
        }

        AboutScreen(
            onUIAction = viewModel::onUIAction,
            onNavigationEvent = viewModel::onNavigationEvent
        )

    }

}