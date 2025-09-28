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
import androidx.compose.ui.res.stringResource
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.theme.color.tokens.BlueDarkColorPaletteTokens
import com.stoyanvuchev.magicmessage.core.ui.theme.color.tokens.BlueLightColorPaletteTokens
import com.stoyanvuchev.magicmessage.core.ui.theme.color.tokens.MonoDarkColorPaletteTokens
import com.stoyanvuchev.magicmessage.core.ui.theme.color.tokens.MonoLightColorPaletteTokens

enum class ColorScheme { MONO, BLUE, DYNAMIC }

@Stable
@Composable
fun ColorScheme.toColorPalette(
    darkTheme: Boolean
): ColorPalette = when (this) {

    ColorScheme.BLUE -> getThemedColorPalette(
        darkTheme = darkTheme,
        lightThemeTokens = BlueLightColorPaletteTokens,
        darkThemeTokens = BlueDarkColorPaletteTokens
    )

    ColorScheme.MONO -> getThemedColorPalette(
        darkTheme = darkTheme,
        lightThemeTokens = MonoLightColorPaletteTokens,
        darkThemeTokens = MonoDarkColorPaletteTokens
    )

    ColorScheme.DYNAMIC -> getDynamicColorPalette(darkTheme)

}

@Composable
fun ColorScheme.label() = stringResource(
    when (this) {
        ColorScheme.BLUE -> R.string.color_scheme_blue
        ColorScheme.MONO -> R.string.color_scheme_mono
        ColorScheme.DYNAMIC -> R.string.color_scheme_dynamic
    }
)