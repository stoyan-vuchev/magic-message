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

package com.stoyanvuchev.magicmessage.domain.serializer

import com.stoyanvuchev.magicmessage.domain.layer.BackgroundLayer
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

object BackgroundLayerSerializer : KSerializer<BackgroundLayer> {

    @OptIn(InternalSerializationApi::class)
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("BackgroundLayer")

    private val json = Json { encodeDefaults = true }

    override fun serialize(encoder: Encoder, value: BackgroundLayer) {
        val jsonString = when (value) {
            is BackgroundLayer.ColorLayer -> json.encodeToString(
                BackgroundLayer.ColorLayer.serializer(),
                value
            )

            is BackgroundLayer.LinearGradientLayer -> json.encodeToString(
                BackgroundLayer.LinearGradientLayer.serializer(),
                value
            )

            is BackgroundLayer.ImageLayer -> json.encodeToString(
                BackgroundLayer.ImageLayer.serializer(),
                value
            )
        }
        encoder.encodeString(jsonString)
    }

    override fun deserialize(decoder: Decoder): BackgroundLayer {
        val string = decoder.decodeString()
        val jsonElement = json.parseToJsonElement(string).jsonObject
        return when {
            "color" in jsonElement -> json.decodeFromString(
                BackgroundLayer.ColorLayer.serializer(),
                string
            )

            "colors" in jsonElement -> json.decodeFromString(
                BackgroundLayer.LinearGradientLayer.serializer(),
                string
            )

            "uri" in jsonElement -> json.decodeFromString(
                BackgroundLayer.ImageLayer.serializer(),
                string
            )

            else -> throw IllegalArgumentException("Unknown BackgroundLayer type")
        }
    }
}