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

package com.stoyanvuchev.magicmessage.core.ui.theme.color.tokens

import androidx.compose.ui.graphics.Color
import com.stoyanvuchev.magicmessage.core.ui.theme.color.ColorScheme

object MonoLightColorPaletteTokens : ColorPaletteTokens {

    override val scheme: ColorScheme get() = ColorScheme.MONO

    override val primary: Color get() = Color(0xFF000000)
    override val onPrimary: Color get() = Color(0xFFFFFFFF)

    override val surfaceElevationLow: Color get() = Color(0xFFEBEBEB)
    override val onSurfaceElevationLow: Color get() = Color(0xFF202020)

    override val surfaceElevationMedium: Color get() = Color(0xFFF0F0F0)
    override val onSurfaceElevationMedium: Color get() = Color(0xFF151515)

    override val surfaceElevationHigh: Color get() = Color(0xFFFEFEFE)
    override val onSurfaceElevationHigh: Color get() = Color(0xFF101010)

    override val error: Color get() = Color(0xFFCA1F2D)
    override val onError: Color get() = Color(0xFFFFFFFF)

    override val outline: Color get() = Color(0x14000000)

}

object MonoDarkColorPaletteTokens : ColorPaletteTokens {

    override val scheme: ColorScheme get() = ColorScheme.MONO

    override val primary: Color get() = Color(0xFFFFFFFF)
    override val onPrimary: Color get() = Color(0xFF000000)

    override val surfaceElevationLow: Color get() = Color(0xFF101010)
    override val onSurfaceElevationLow: Color get() = Color(0xFFEBEBEB)

    override val surfaceElevationMedium: Color get() = Color(0xFF151515)
    override val onSurfaceElevationMedium: Color get() = Color(0xFFF0F0F0)

    override val surfaceElevationHigh: Color get() = Color(0xFF202020)
    override val onSurfaceElevationHigh: Color get() = Color(0xFFFEFEFE)

    override val error: Color get() = Color(0xFFCA1F2D)
    override val onError: Color get() = Color(0xFFFFFFFF)

    override val outline: Color get() = Color(0x14FFFFFF)

}