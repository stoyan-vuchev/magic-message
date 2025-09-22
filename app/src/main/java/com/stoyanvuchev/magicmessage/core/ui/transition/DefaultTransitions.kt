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

package com.stoyanvuchev.magicmessage.core.ui.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry

@Stable
object DefaultTransitions {

    const val INITIAL_SCALE = .9f
    const val INITIAL_POP_SCALE = 1.1f
    const val TARGET_SCALE = 1.1f
    const val TARGET_POP_SCALE = .9f

    @Stable
    object Enter {

        fun fadeInWithScaleIn(
            initialScale: Float
        ): EnterTransition {
            return fadeIn(
                spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ) + scaleIn(
                initialScale = initialScale,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }

        fun slideIntoContainerAndFadeIn(
            scope: AnimatedContentTransitionScope<NavBackStackEntry>,
            slideDirection: AnimatedContentTransitionScope.SlideDirection,
            initialOffset: (Int) -> Int = { it }
        ): EnterTransition {
            return with(scope) {
                fadeIn(
                    spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + slideIntoContainer(
                    towards = slideDirection,
                    initialOffset = initialOffset,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMediumLow,
                        visibilityThreshold = IntOffset.VisibilityThreshold
                    )
                )
            }
        }

    }

    @Stable
    object Exit {

        fun fadeOutWithScaleOut(
            targetScale: Float
        ): ExitTransition {
            return fadeOut(
                spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ) + scaleOut(
                targetScale = targetScale,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }

        fun slideOutOfContainerAndFadeOut(
            scope: AnimatedContentTransitionScope<NavBackStackEntry>,
            slideDirection: AnimatedContentTransitionScope.SlideDirection,
            targetOffset: (Int) -> Int = { it }
        ): ExitTransition {
            return with(scope) {
                fadeOut(
                    spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + slideOutOfContainer(
                    towards = slideDirection,
                    targetOffset = targetOffset,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMediumLow,
                        visibilityThreshold = IntOffset.VisibilityThreshold
                    )
                )
            }
        }

    }

}