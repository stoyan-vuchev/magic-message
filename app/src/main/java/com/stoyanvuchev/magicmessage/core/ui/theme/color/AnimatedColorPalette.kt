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

package com.stoyanvuchev.magicmessage.core.ui.theme.color

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

@Stable
@Composable
fun ColorPalette.asAnimatedColorPalette(): ColorPalette {

    val animatedPrimary by animateColor(this.primary)
    val animatedOnPrimary by animateColor(this.onPrimary)

    val animatedSurfaceElevationLow by animateColor(this.surfaceElevationLow)
    val animatedOnSurfaceElevationLow by animateColor(this.onSurfaceElevationLow)

    val animatedSurfaceElevationMedium by animateColor(this.surfaceElevationMedium)
    val animatedOnSurfaceElevationMedium by animateColor(this.onSurfaceElevationMedium)

    val animatedSurfaceElevationHigh by animateColor(this.surfaceElevationHigh)
    val animatedOnSurfaceElevationHigh by animateColor(this.onSurfaceElevationHigh)

    val animatedError by animateColor(this.error)
    val animatedOnError by animateColor(this.onError)

    val animatedOutline by animateColor(this.outline)

    return ColorPalette(
        primary = animatedPrimary,
        onPrimary = animatedOnPrimary,
        surfaceElevationLow = animatedSurfaceElevationLow,
        onSurfaceElevationLow = animatedOnSurfaceElevationLow,
        surfaceElevationMedium = animatedSurfaceElevationMedium,
        onSurfaceElevationMedium = animatedOnSurfaceElevationMedium,
        surfaceElevationHigh = animatedSurfaceElevationHigh,
        onSurfaceElevationHigh = animatedOnSurfaceElevationHigh,
        error = animatedError,
        onError = animatedOnError,
        outline = animatedOutline
    )

}

@Stable
@Composable
private fun animateColor(color: Color): State<Color> {
    return animateColorAsState(
        targetValue = color,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
}