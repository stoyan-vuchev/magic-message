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

package com.stoyanvuchev.magicmessage.domain.layer

import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.stoyanvuchev.magicmessage.domain.serializer.ColorListSerializer
import com.stoyanvuchev.magicmessage.domain.serializer.ColorSerializer
import com.stoyanvuchev.magicmessage.domain.serializer.OffsetSerializer
import com.stoyanvuchev.magicmessage.domain.serializer.UriSerializer
import kotlinx.serialization.Serializable

@Stable
@Serializable
sealed class BackgroundLayer {

    @Serializable
    data class ColorLayer(
        @Serializable(with = ColorSerializer::class) val color: Color
    ) : BackgroundLayer()

    @Serializable
    data class LinearGradientLayer(
        @Serializable(with = ColorListSerializer::class) val colors: List<Color>,
        @Serializable(with = OffsetSerializer::class) val start: Offset = Offset.Zero,
        @Serializable(with = OffsetSerializer::class) val end: Offset? = null
    ) : BackgroundLayer()

    @Serializable
    data class ImageLayer(
        @Serializable(with = UriSerializer::class) val uri: Uri
    ) : BackgroundLayer()

}