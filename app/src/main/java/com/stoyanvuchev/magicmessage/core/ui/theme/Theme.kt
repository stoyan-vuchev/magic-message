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
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import com.stoyanvuchev.magicmessage.core.ui.theme.color.ColorPalette
import com.stoyanvuchev.magicmessage.core.ui.theme.color.ColorScheme
import com.stoyanvuchev.magicmessage.core.ui.theme.color.LocalColorPalette
import com.stoyanvuchev.magicmessage.core.ui.theme.color.toColorPalette
import com.stoyanvuchev.magicmessage.core.ui.theme.shape.LocalShapes
import com.stoyanvuchev.magicmessage.core.ui.theme.shape.Shapes
import com.stoyanvuchev.magicmessage.core.ui.theme.typeface.LocalTypefaces
import com.stoyanvuchev.magicmessage.core.ui.theme.typeface.Typefaces

@Composable
fun MagicMessageTheme(
    themeMode: ThemeMode = LocalThemeMode.current,
    colorScheme: ColorScheme = ColorScheme.BLUE,
    content: @Composable () -> Unit
) = UIWrapper(
    themeMode = themeMode,
    colorPalette = colorScheme.toColorPalette(isInDarkThemeMode(themeMode)),
    content = content
)

@Stable
object Theme {

    val colors: ColorPalette
        @ReadOnlyComposable
        @Composable
        get() = LocalColorPalette.current

    val shapes: Shapes
        @ReadOnlyComposable
        @Composable
        get() = LocalShapes.current

    val typefaces: Typefaces
        @ReadOnlyComposable
        @Composable
        get() = LocalTypefaces.current

}