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

package com.stoyanvuchev.magicmessage.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import com.stoyanvuchev.magicmessage.core.ui.ext.LocalHazeState
import com.stoyanvuchev.magicmessage.core.ui.theme.color.ColorPalette
import com.stoyanvuchev.magicmessage.core.ui.theme.color.LocalColorPalette
import com.stoyanvuchev.magicmessage.core.ui.theme.color.LocalContentColor
import com.stoyanvuchev.magicmessage.core.ui.theme.color.asAnimatedColorPalette
import com.stoyanvuchev.magicmessage.core.ui.theme.color.darkColorPalette
import com.stoyanvuchev.magicmessage.core.ui.theme.color.lightColorPalette
import com.stoyanvuchev.magicmessage.core.ui.theme.shape.LocalShapes
import com.stoyanvuchev.magicmessage.core.ui.theme.shape.Shapes
import com.stoyanvuchev.magicmessage.core.ui.theme.typeface.LocalTextStyle
import com.stoyanvuchev.magicmessage.core.ui.theme.typeface.LocalTypefaces
import com.stoyanvuchev.magicmessage.core.ui.theme.typeface.Typefaces
import com.stoyanvuchev.systemuibarstweaker.LocalSystemUIBarsTweaker
import com.stoyanvuchev.systemuibarstweaker.ProvideSystemUIBarsTweaker
import com.stoyanvuchev.systemuibarstweaker.ScrimStyle
import dev.chrisbanes.haze.rememberHazeState

@Composable
fun UIWrapper(
    themeMode: ThemeMode = ThemeMode.System,
    colorPalette: ColorPalette = getDefaultColorPalette(isInDarkThemeMode(themeMode)),
    animateColorPalette: Boolean = true,
    typefaces: Typefaces = Typefaces.Default,
    shapes: Shapes = Shapes.Default,
    content: @Composable () -> Unit
) = ProvideSystemUIBarsTweaker {

    val darkTheme = isInDarkThemeMode(themeMode)
    val tweaker = LocalSystemUIBarsTweaker.current
    val hazeState = rememberHazeState()
    val finalColorPalette by rememberUpdatedState(
        if (animateColorPalette) colorPalette.asAnimatedColorPalette()
        else colorPalette
    )

    DisposableEffect(darkTheme, tweaker) {
        tweaker.tweakSystemBarsStyle(
            statusBarStyle = tweaker.statusBarStyle.copy(darkIcons = !darkTheme),
            navigationBarStyle = tweaker.navigationBarStyle.copy(
                darkIcons = !darkTheme,
                scrimStyle = ScrimStyle.None
            )
        )
        onDispose {}
    }

    CompositionLocalProvider(
        LocalThemeMode provides themeMode,
        LocalColorPalette provides finalColorPalette,
        LocalContentColor provides finalColorPalette.onSurfaceElevationLow,
        LocalTypefaces provides typefaces,
        LocalTextStyle provides typefaces.bodyMedium,
        LocalShapes provides shapes,
        LocalHazeState provides hazeState,
        content = content
    )

}

@Stable
fun getDefaultColorPalette(darkTheme: Boolean): ColorPalette {
    return (if (darkTheme) darkColorPalette()
    else lightColorPalette())
}