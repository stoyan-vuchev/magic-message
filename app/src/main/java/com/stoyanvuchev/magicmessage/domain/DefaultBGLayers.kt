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

package com.stoyanvuchev.magicmessage.domain

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.stoyanvuchev.magicmessage.domain.layer.BackgroundLayer

@Immutable
object DefaultBGLayers {

    val colorLayers: List<BackgroundLayer.ColorLayer> = defaultDrawColors
        .map { color -> BackgroundLayer.ColorLayer(color) }

    val linearGradientLayers: List<BackgroundLayer.LinearGradientLayer> = listOf(
        BackgroundLayer.LinearGradientLayer(
            colors = listOf(
                Color(0xFF1A80E5),
                Color(0xFF3C1AE5)
            )
        ),
        BackgroundLayer.LinearGradientLayer(
            colors = listOf(
                Color(0xFFEC407A),
                Color(0xFF1A80E5)
            )
        ),
        BackgroundLayer.LinearGradientLayer(
            colors = listOf(
                Color(0xFFFFCA28),
                Color(0xFF66BB6A)
            )
        ),
        BackgroundLayer.LinearGradientLayer(
            colors = listOf(
                Color(0xFFede342),
                Color(0xFFff51eb)
            )
        ),
        BackgroundLayer.LinearGradientLayer(
            colors = listOf(
                Color(0xFFf4e900),
                Color(0xFF60b6f1)
            )
        ),
        BackgroundLayer.LinearGradientLayer(
            colors = listOf(
                Color(0xFF2178dd),
                Color(0xFFf8cf6a)
            )
        ),
        BackgroundLayer.LinearGradientLayer(
            colors = listOf(
                Color(0xFFff0084),
                Color(0xFF33001b)
            )
        ),
        BackgroundLayer.LinearGradientLayer(
            colors = listOf(
                Color(0xFF03001e),
                Color(0xFF7303c0),
                Color(0xFFec38bc),
                Color(0xFFfdeff9)
            )
        )
    )

}