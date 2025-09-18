package com.stoyanvuchev.magicmessage.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.stoyanvuchev.magicmessage.core.ui.navigation.InitialScreen
import com.stoyanvuchev.magicmessage.presentation.boarding.BoardingScreen
import com.stoyanvuchev.magicmessage.presentation.boarding.boardingNavGraph

@Composable
fun AppNavHost(navController: NavHostController) {

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = InitialScreen
    ) {

        composable<InitialScreen> {
            LaunchedEffect(Unit) {
                navController.navigate(BoardingScreen.GetStarted) {
                    popUpTo(InitialScreen) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }

        boardingNavGraph(navController = navController)

    }

}