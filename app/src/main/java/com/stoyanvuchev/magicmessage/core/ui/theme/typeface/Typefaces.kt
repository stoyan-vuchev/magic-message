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
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

@Immutable
class Typefaces(
    val titleLarge: TextStyle = DefaultTypefaceTokens.titleLarge,
    val titleMedium: TextStyle = DefaultTypefaceTokens.titleMedium,
    val titleSmall: TextStyle = DefaultTypefaceTokens.titleSmall,
    val bodyLarge: TextStyle = DefaultTypefaceTokens.bodyLarge,
    val bodyMedium: TextStyle = DefaultTypefaceTokens.bodyMedium,
    val bodySmall: TextStyle = DefaultTypefaceTokens.bodySmall,
) {

    companion object {

        val Default get() = Typefaces()

    }

    fun copy(
        titleLarge: TextStyle = this.titleLarge,
        titleMedium: TextStyle = this.titleMedium,
        titleSmall: TextStyle = this.titleSmall,
        bodyLarge: TextStyle = this.bodyLarge,
        bodyMedium: TextStyle = this.bodyMedium,
        bodySmall: TextStyle = this.bodySmall
    ) = Typefaces(
        titleLarge = titleLarge,
        titleMedium = titleMedium,
        titleSmall = titleSmall,
        bodyLarge = bodyLarge,
        bodyMedium = bodyMedium,
        bodySmall = bodySmall
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is Typefaces) return false
        if (titleLarge != other.titleLarge) return false
        if (titleMedium != other.titleMedium) return false
        if (titleSmall != other.titleSmall) return false
        if (bodyLarge != other.bodyLarge) return false
        if (bodyMedium != other.bodyMedium) return false
        if (bodySmall != other.bodySmall) return false
        return true
    }

    override fun hashCode(): Int {
        var result = titleLarge.hashCode()
        result += titleMedium.hashCode()
        result += titleSmall.hashCode()
        result += bodyLarge.hashCode()
        result += bodyMedium.hashCode()
        result += bodySmall.hashCode()
        return result
    }

    override fun toString(): String {
        return "Typefaces(" +
                "titleLarge=$titleLarge," +
                "titleMedium=$titleMedium, " +
                "titleSmall=$titleSmall, " +
                "bodyLarge=$bodyLarge, " +
                "bodyMedium=$bodyMedium, " +
                "bodySmall=$bodySmall" +
                ")"
    }

}

val LocalTypefaces = staticCompositionLocalOf { Typefaces.Default }
val LocalTextStyle = compositionLocalOf { DefaultTypefaceTokens.bodyMedium }