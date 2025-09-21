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

package com.stoyanvuchev.magicmessage.domain.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.stoyanvuchev.magicmessage.domain.DefaultBGLayers
import com.stoyanvuchev.magicmessage.domain.brush.BrushEffect
import com.stoyanvuchev.magicmessage.domain.brush.BrushThickness
import com.stoyanvuchev.magicmessage.domain.layer.BackgroundLayer
import com.stoyanvuchev.magicmessage.domain.serializer.BackgroundLayerSerializer
import com.stoyanvuchev.magicmessage.domain.serializer.ColorSerializer
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class DrawConfiguration(
    val canvasWidth: Int = 720,
    val canvasHeight: Int = 960,
    val effect: BrushEffect = BrushEffect.BUBBLES,
    val thickness: BrushThickness = BrushThickness.MEDIUM,
    @Serializable(with = ColorSerializer::class) val color: Color = Color.White,
    @Serializable(with = BackgroundLayerSerializer::class) val bgLayer: BackgroundLayer = DefaultBGLayers.linearGradientLayers.first()
)