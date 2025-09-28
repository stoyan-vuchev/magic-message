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

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.stoyanvuchev.magicmessage.R

enum class ThemeMode { SYSTEM, LIGHT, DARK }

val LocalThemeMode = compositionLocalOf { ThemeMode.SYSTEM }

@Stable
@Composable
fun isInDarkThemeMode(
    themeMode: ThemeMode = LocalThemeMode.current
) = when (themeMode) {
    ThemeMode.SYSTEM -> isSystemInDarkTheme()
    ThemeMode.LIGHT -> false
    ThemeMode.DARK -> true
}

@Composable
fun ThemeMode.label() = stringResource(
    when (this) {
        ThemeMode.SYSTEM -> R.string.theme_mode_system
        ThemeMode.DARK -> R.string.theme_mode_dark
        ThemeMode.LIGHT -> R.string.theme_mode_light
    }
)

@Composable
fun ThemeMode.icon() = painterResource(
    when (this) {
        ThemeMode.SYSTEM -> R.drawable.system_theme_mode_outlined
        ThemeMode.DARK -> R.drawable.dark_theme_mode_outlined
        ThemeMode.LIGHT -> R.drawable.light_theme_mode_outlined
    }
)