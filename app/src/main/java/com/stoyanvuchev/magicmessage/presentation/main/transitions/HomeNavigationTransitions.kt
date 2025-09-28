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

package com.stoyanvuchev.magicmessage.presentation.main.transitions

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Stable
import androidx.navigation.NavBackStackEntry
import com.stoyanvuchev.magicmessage.core.ui.navigation.NavigationScreen
import com.stoyanvuchev.magicmessage.core.ui.transition.DefaultTransitions
import com.stoyanvuchev.magicmessage.core.ui.transition.NavigationTransitions
import com.stoyanvuchev.magicmessage.presentation.main.MainScreen

@Stable
object HomeNavigationTransitions : NavigationTransitions {

    override fun enter(
        scope: AnimatedContentTransitionScope<NavBackStackEntry>,
        initialRoute: NavigationScreen?,
        targetRoute: NavigationScreen?
    ): EnterTransition {
        return when {

            initialRoute is MainScreen.Favorite
                    && targetRoute is MainScreen.Home
                    || initialRoute is MainScreen.Menu
                    && targetRoute is MainScreen.Home -> {

                DefaultTransitions.Enter.slideIntoContainerAndFadeIn(
                    scope = scope,
                    slideDirection = AnimatedContentTransitionScope.SlideDirection.End
                )

            }

            else -> DefaultTransitions.Enter.fadeInWithScaleIn(
                initialScale = DefaultTransitions.INITIAL_SCALE
            )

        }
    }

    override fun exit(
        scope: AnimatedContentTransitionScope<NavBackStackEntry>,
        initialRoute: NavigationScreen?,
        targetRoute: NavigationScreen?
    ): ExitTransition {
        return when {

            initialRoute is MainScreen.Home
                    && targetRoute is MainScreen.Favorite
                    || initialRoute is MainScreen.Home
                    && targetRoute is MainScreen.Menu -> {

                DefaultTransitions.Exit.slideOutOfContainerAndFadeOut(
                    scope = scope,
                    slideDirection = AnimatedContentTransitionScope.SlideDirection.Start
                )

            }

            else -> DefaultTransitions.Exit.fadeOutWithScaleOut(
                targetScale = DefaultTransitions.TARGET_SCALE
            )

        }
    }

    override fun popEnter(
        scope: AnimatedContentTransitionScope<NavBackStackEntry>,
        initialRoute: NavigationScreen?,
        targetRoute: NavigationScreen?
    ): EnterTransition {
        return when {

            initialRoute is MainScreen.Favorite
                    && targetRoute is MainScreen.Home
                    || initialRoute is MainScreen.Menu
                    && targetRoute is MainScreen.Home -> {

                DefaultTransitions.Enter.slideIntoContainerAndFadeIn(
                    scope = scope,
                    slideDirection = AnimatedContentTransitionScope.SlideDirection.End
                )

            }

            else -> DefaultTransitions.Enter.fadeInWithScaleIn(
                initialScale = DefaultTransitions.INITIAL_POP_SCALE
            )

        }
    }

    override fun popExit(
        scope: AnimatedContentTransitionScope<NavBackStackEntry>,
        initialRoute: NavigationScreen?,
        targetRoute: NavigationScreen?
    ): ExitTransition {
        return when {

            initialRoute is MainScreen.Home
                    && targetRoute is MainScreen.Favorite
                    || initialRoute is MainScreen.Home
                    && targetRoute is MainScreen.Menu -> {

                DefaultTransitions.Exit.slideOutOfContainerAndFadeOut(
                    scope = scope,
                    slideDirection = AnimatedContentTransitionScope.SlideDirection.Start
                )

            }

            else -> DefaultTransitions.Exit.fadeOutWithScaleOut(
                targetScale = DefaultTransitions.TARGET_POP_SCALE
            )

        }
    }

}