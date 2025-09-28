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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.colorResource
import com.stoyanvuchev.magicmessage.core.ui.theme.color.tokens.BlueDarkColorPaletteTokens
import com.stoyanvuchev.magicmessage.core.ui.theme.color.tokens.BlueLightColorPaletteTokens
import com.stoyanvuchev.magicmessage.core.ui.theme.color.tokens.ColorPaletteTokens
import com.stoyanvuchev.magicmessage.core.ui.theme.color.tokens.MonoDarkColorPaletteTokens
import com.stoyanvuchev.magicmessage.core.ui.theme.color.tokens.MonoLightColorPaletteTokens

@Stable
data class ColorPalette(
    val scheme: ColorScheme,
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
fun colorPalette(
    tokens: ColorPaletteTokens
) = ColorPalette(
    scheme = tokens.scheme,
    primary = tokens.primary,
    onPrimary = tokens.onPrimary,
    surfaceElevationLow = tokens.surfaceElevationLow,
    onSurfaceElevationLow = tokens.onSurfaceElevationLow,
    surfaceElevationMedium = tokens.surfaceElevationMedium,
    onSurfaceElevationMedium = tokens.onSurfaceElevationMedium,
    surfaceElevationHigh = tokens.surfaceElevationHigh,
    onSurfaceElevationHigh = tokens.onSurfaceElevationHigh,
    error = tokens.error,
    onError = tokens.onError,
    outline = tokens.outline
)

@Stable
fun getThemedColorPalette(
    darkTheme: Boolean,
    lightThemeTokens: ColorPaletteTokens = BlueLightColorPaletteTokens,
    darkThemeTokens: ColorPaletteTokens = BlueDarkColorPaletteTokens
): ColorPalette {
    return if (darkTheme) colorPalette(darkThemeTokens)
    else colorPalette(lightThemeTokens)
}

@Stable
@Composable
fun getDynamicColorPalette(
    darkTheme: Boolean
): ColorPalette {

    val primary = colorResource(android.R.color.system_accent1_500)
    val primaryComposite = remember(primary) { primary.copy(alpha = .04f) }
    val onPrimary = remember { Color.White }
    val tokens = remember(darkTheme) {
        if (darkTheme) MonoDarkColorPaletteTokens
        else MonoLightColorPaletteTokens
    }

    val surfaceLow = primaryComposite.compositeOver(tokens.surfaceElevationLow)
    val surfaceMedium = primaryComposite.compositeOver(tokens.surfaceElevationMedium)
    val surfaceHigh = primaryComposite.compositeOver(tokens.surfaceElevationHigh)

    return ColorPalette(
        scheme = ColorScheme.DYNAMIC,
        primary = primary,
        onPrimary = onPrimary,
        surfaceElevationLow = surfaceLow,
        onSurfaceElevationLow = tokens.onSurfaceElevationLow,
        surfaceElevationMedium = surfaceMedium,
        onSurfaceElevationMedium = tokens.onSurfaceElevationMedium,
        surfaceElevationHigh = surfaceHigh,
        onSurfaceElevationHigh = tokens.onSurfaceElevationHigh,
        error = tokens.error,
        onError = tokens.onError,
        outline = tokens.outline
    )

}

val LocalColorPalette = compositionLocalOf { colorPalette(BlueLightColorPaletteTokens) }
val LocalContentColor = compositionLocalOf { Color.Unspecified }