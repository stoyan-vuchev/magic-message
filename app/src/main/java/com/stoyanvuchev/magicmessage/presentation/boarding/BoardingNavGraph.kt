package com.stoyanvuchev.magicmessage.presentation.boarding

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent
import com.stoyanvuchev.magicmessage.presentation.boarding.get_started.GetStartedScreen
import com.stoyanvuchev.magicmessage.presentation.boarding.get_started.GetStartedScreenViewModel
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.boardingNavGraph(navController: NavHostController) {

    navigation<BoardingScreen.Navigation>(
        startDestination = BoardingScreen.GetStarted
    ) {

        composable<BoardingScreen.GetStarted> {

            val viewModel = hiltViewModel<GetStartedScreenViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

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

        composable<BoardingScreen.PrivacyPolicy> { }

        composable<BoardingScreen.TermsOfService> { }

        composable<BoardingScreen.PermissionNotice> { }

    }

}