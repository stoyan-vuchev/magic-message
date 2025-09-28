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

package com.stoyanvuchev.magicmessage.presentation.boarding

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent
import com.stoyanvuchev.magicmessage.presentation.boarding.get_started.GetStartedScreen
import com.stoyanvuchev.magicmessage.presentation.boarding.get_started.GetStartedScreenUIAction
import com.stoyanvuchev.magicmessage.presentation.boarding.get_started.GetStartedScreenViewModel
import com.stoyanvuchev.magicmessage.presentation.boarding.permission_screen.PermissionScreen
import com.stoyanvuchev.magicmessage.presentation.boarding.permission_screen.PermissionScreenUIAction
import com.stoyanvuchev.magicmessage.presentation.boarding.permission_screen.PermissionScreenViewModel
import com.stoyanvuchev.magicmessage.presentation.main.MainScreen
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalPermissionsApi::class)
fun NavGraphBuilder.boardingNavGraph(navController: NavHostController) {

    navigation<BoardingScreen.Navigation>(
        startDestination = BoardingScreen.GetStarted
    ) {

        composable<BoardingScreen.GetStarted> {

            val viewModel = hiltViewModel<GetStartedScreenViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            val uriHandler = LocalUriHandler.current

            LaunchedEffect(viewModel.uiActionFlow) {
                viewModel.uiActionFlow.collectLatest { navigationEvent ->
                    when (navigationEvent) {

                        is GetStartedScreenUIAction.ViewPrivacyPolicy -> {
                            uriHandler.openUri(navigationEvent.url)
                        }

                        is GetStartedScreenUIAction.ViewTermsOfService -> {
                            uriHandler.openUri(navigationEvent.url)
                        }

                        else -> Unit

                    }
                }
            }

            LaunchedEffect(viewModel.navigationFlow) {
                viewModel.navigationFlow.collectLatest { navigationEvent ->
                    when (navigationEvent) {

                        is NavigationEvent.NavigateTo -> {
                            navController.navigate(navigationEvent.screen) {
                                launchSingleTop = true
                            }
                        }

                        else -> Unit

                    }
                }
            }

            GetStartedScreen(
                state = state,
                onUIAction = viewModel::onUIAction,
                onNavigationEvent = viewModel::onNavigationEvent
            )

        }

        composable<BoardingScreen.Permission> {

            val viewModel = hiltViewModel<PermissionScreenViewModel>()
            val context = LocalContext.current

            val notificationPermissionState = rememberPermissionState(
                Manifest.permission.POST_NOTIFICATIONS
            )

            val isPermissionRevoked by rememberUpdatedState(
                notificationPermissionState.status.shouldShowRationale
            )

            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->

                if (isGranted) {
                    navController.navigate(MainScreen.Home) {
                        popUpTo(BoardingScreen.GetStarted) { inclusive = true }
                        launchSingleTop = true
                    }
                }

            }

            LaunchedEffect(viewModel.uiActionFlow) {
                viewModel.uiActionFlow.collectLatest { action ->
                    when (action) {

                        is PermissionScreenUIAction.RequestPermissions -> {
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }

                        is PermissionScreenUIAction.OpenSettings -> {

                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            ).apply {
                                data = Uri.fromParts(
                                    "package",
                                    context.packageName,
                                    null
                                )
                            }

                            context.startActivity(intent)

                        }

                    }
                }
            }

            PermissionScreen(
                isPermissionRevoked = isPermissionRevoked,
                onUIAction = viewModel::onUIAction
            )

        }

    }

}