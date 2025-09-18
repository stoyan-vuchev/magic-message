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

package com.stoyanvuchev.magicmessage.core.ui.theme.typeface

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.stoyanvuchev.magicmessage.R

@Immutable
internal object DefaultTypefaceTokens : TypefaceTokens {

    override val titleLarge: TextStyle
        get() = DefaultTextStyle.copy(
            fontFamily = Caveat,
            fontWeight = FontWeight.Medium,
            fontSize = 46.sp,
            lineHeight = 50.sp,
            letterSpacing = 0.0.sp
        )

    override val titleMedium: TextStyle
        get() = DefaultTextStyle.copy(
            fontFamily = Fixel,
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.0.sp
        )

    override val titleSmall: TextStyle
        get() = DefaultTextStyle.copy(
            fontFamily = Fixel,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.0.sp
        )

    override val bodyLarge: TextStyle
        get() = DefaultTextStyle.copy(
            fontFamily = Fixel,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        )

    override val bodyMedium: TextStyle
        get() = DefaultTextStyle.copy(
            fontFamily = Fixel,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.2.sp
        )

    override val bodySmall: TextStyle
        get() = DefaultTextStyle.copy(
            fontFamily = Fixel,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.3.sp
        )

}

@Immutable
internal interface TypefaceTokens {
    val titleLarge: TextStyle
    val titleMedium: TextStyle
    val titleSmall: TextStyle
    val bodyLarge: TextStyle
    val bodyMedium: TextStyle
    val bodySmall: TextStyle
}


internal val DefaultLineHeightStyle
    get() = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    )

internal val DefaultTextStyle
    get() = TextStyle.Default.copy(
        platformStyle = null,
        lineHeightStyle = DefaultLineHeightStyle,
    )

internal val DefaultFontFamily
    get() = FontFamily.SansSerif

internal val Fixel = FontFamily(
    Font(R.font.fixel_text_regular, weight = FontWeight.Normal),
    Font(R.font.fixel_text_medium, weight = FontWeight.Medium),
    Font(R.font.fixel_display_regular, weight = FontWeight.Normal),
    Font(R.font.fixel_display_medium, weight = FontWeight.Medium)
)

internal val Caveat = FontFamily(
    Font(R.font.caveat_medium, weight = FontWeight.Medium)
)