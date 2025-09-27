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

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

object LightColorPaletteTokens : ColorPaletteTokens {

    override val primary: Color get() = Color(0xFF007BD1)
    override val onPrimary: Color get() = Color(0xFFFFFFFF)

    override val surfaceElevationLow: Color get() = Color(0xFFDBE4E8)
    override val onSurfaceElevationLow: Color get() = Color(0xFF0D181D)

    override val surfaceElevationMedium: Color get() = Color(0xFFE7EDF0)
    override val onSurfaceElevationMedium: Color get() = Color(0xFF070E13)

    override val surfaceElevationHigh: Color get() = Color(0xFFF3F6F7)
    override val onSurfaceElevationHigh: Color get() = Color(0xFF030609)

    override val error: Color get() = Color(0xFFCA1F2D)
    override val onError: Color get() = Color(0xFFFFFFFF)

    override val outline: Color get() = Color(0x14000000)

}

object DarkColorPaletteTokens : ColorPaletteTokens {

    override val primary: Color get() = Color(0xFF007BD1)
    override val onPrimary: Color get() = Color(0xFFFFFFFF)

    override val surfaceElevationLow: Color get() = Color(0xFF0E1011)
    override val onSurfaceElevationLow: Color get() = Color(0xFFE7EDF0)

    override val surfaceElevationMedium: Color get() = Color(0xFF101313)
    override val onSurfaceElevationMedium: Color get() = Color(0xFFF3F6F7)

    override val surfaceElevationHigh: Color get() = Color(0xFF1D1F20)
    override val onSurfaceElevationHigh: Color get() = Color(0xFFF3F6F7)

    override val error: Color get() = Color(0xFFCA1F2D)
    override val onError: Color get() = Color(0xFFFFFFFF)

    override val outline: Color get() = Color(0x14FFFFFF)

}

@Immutable
interface ColorPaletteTokens {
    val primary: Color
    val onPrimary: Color
    val surfaceElevationLow: Color
    val onSurfaceElevationLow: Color
    val surfaceElevationMedium: Color
    val onSurfaceElevationMedium: Color
    val surfaceElevationHigh: Color
    val onSurfaceElevationHigh: Color
    val error: Color
    val onError: Color
    val outline: Color
}