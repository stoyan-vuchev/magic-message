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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.stoyanvuchev.magicmessage.core.ui.components.bottom_nav_bar.BottomNavBar
import com.stoyanvuchev.magicmessage.core.ui.components.bottom_nav_bar.BottomNavBarItem
import com.stoyanvuchev.magicmessage.core.ui.effect.defaultHazeEffect
import com.stoyanvuchev.magicmessage.presentation.main.mainScreenNavDestinations
import dev.chrisbanes.haze.HazeState

@Composable
fun AppBottomNavBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    hazeState: HazeState
) {

    val destinations = rememberSaveable { mainScreenNavDestinations }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination by rememberUpdatedState(
        navBackStackEntry?.destination
    )

    val isNavBarVisible by remember(currentDestination) {
        derivedStateOf {
            destinations.any { destination ->
                currentDestination?.hasRoute(destination::class) ?: false
            }
        }
    }

    AnimatedVisibility(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        visible = isNavBarVisible,
        enter = remember { fadeIn() + slideInVertically { it } },
        exit = remember { slideOutVertically { it } + fadeOut() }
    ) {

        BottomNavBar(
            modifier = Modifier.defaultHazeEffect(hazeState = hazeState)
        ) {

            destinations.forEachIndexed { index, destination ->

                val selected by remember(currentDestination, index) {
                    derivedStateOf {
                        currentDestination
                            ?.hasRoute(destination::class)
                            ?: false
                    }
                }

                BottomNavBarItem(
                    selected = selected,
                    onClick = { navController.navigate(destination) },
                    icon = { offsetY ->

                        Icon(
                            modifier = Modifier.offset(y = offsetY),
                            painter = painterResource(destination.icon),
                            contentDescription = null
                        )

                    },
                    label = {

                        Text(text = stringResource(destination.label))

                    }
                )

            }

        }

    }

}