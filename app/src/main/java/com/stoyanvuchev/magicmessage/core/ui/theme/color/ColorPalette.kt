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

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
data class ColorPalette(
    val primary: Color,
    val onPrimary: Color,
    val surfaceElevationLow: Color,
    val onSurfaceElevationLow: Color,
    val surfaceElevationMedium: Color,
    val onSurfaceElevationMedium: Color,
    val surfaceElevationHigh: Color,
    val onSurfaceElevationHigh: Color,
    val error: Color,
    val onError: Color,
    val outline: Color
)

@Stable
fun lightColorPalette(
    primary: Color = LightColorPaletteTokens.primary,
    onPrimary: Color = LightColorPaletteTokens.onPrimary,
    surfaceElevationLow: Color = LightColorPaletteTokens.surfaceElevationLow,
    onSurfaceElevationLow: Color = LightColorPaletteTokens.onSurfaceElevationLow,
    surfaceElevationMedium: Color = LightColorPaletteTokens.surfaceElevationMedium,
    onSurfaceElevationMedium: Color = LightColorPaletteTokens.onSurfaceElevationMedium,
    surfaceElevationHigh: Color = LightColorPaletteTokens.surfaceElevationHigh,
    onSurfaceElevationHigh: Color = LightColorPaletteTokens.onSurfaceElevationHigh,
    error: Color = LightColorPaletteTokens.error,
    onError: Color = LightColorPaletteTokens.onError,
    outline: Color = LightColorPaletteTokens.outline
) = ColorPalette(
    primary = primary,
    onPrimary = onPrimary,
    surfaceElevationLow = surfaceElevationLow,
    onSurfaceElevationLow = onSurfaceElevationLow,
    surfaceElevationMedium = surfaceElevationMedium,
    onSurfaceElevationMedium = onSurfaceElevationMedium,
    surfaceElevationHigh = surfaceElevationHigh,
    onSurfaceElevationHigh = onSurfaceElevationHigh,
    error = error,
    onError = onError,
    outline = outline
)

@Stable
fun darkColorPalette(
    primary: Color = DarkColorPaletteTokens.primary,
    onPrimary: Color = DarkColorPaletteTokens.onPrimary,
    surfaceElevationLow: Color = DarkColorPaletteTokens.surfaceElevationLow,
    onSurfaceElevationLow: Color = DarkColorPaletteTokens.onSurfaceElevationLow,
    surfaceElevationMedium: Color = DarkColorPaletteTokens.surfaceElevationMedium,
    onSurfaceElevationMedium: Color = DarkColorPaletteTokens.onSurfaceElevationMedium,
    surfaceElevationHigh: Color = DarkColorPaletteTokens.surfaceElevationHigh,
    onSurfaceElevationHigh: Color = DarkColorPaletteTokens.onSurfaceElevationHigh,
    error: Color = DarkColorPaletteTokens.error,
    onError: Color = DarkColorPaletteTokens.onError,
    outline: Color = DarkColorPaletteTokens.outline
) = ColorPalette(
    primary = primary,
    onPrimary = onPrimary,
    surfaceElevationLow = surfaceElevationLow,
    onSurfaceElevationLow = onSurfaceElevationLow,
    surfaceElevationMedium = surfaceElevationMedium,
    onSurfaceElevationMedium = onSurfaceElevationMedium,
    surfaceElevationHigh = surfaceElevationHigh,
    onSurfaceElevationHigh = onSurfaceElevationHigh,
    error = error,
    onError = onError,
    outline = outline
)

val LocalColorPalette = compositionLocalOf { lightColorPalette() }
val LocalContentColor = compositionLocalOf { Color.Unspecified }