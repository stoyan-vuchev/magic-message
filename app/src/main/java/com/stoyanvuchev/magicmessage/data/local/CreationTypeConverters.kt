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

package com.stoyanvuchev.magicmessage.data.local

import android.net.Uri
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.stoyanvuchev.magicmessage.domain.brush.BrushEffect
import com.stoyanvuchev.magicmessage.domain.layer.BackgroundLayer
import com.stoyanvuchev.magicmessage.domain.model.DrawConfiguration
import com.stoyanvuchev.magicmessage.domain.model.StrokeModel
import com.stoyanvuchev.magicmessage.domain.model.TimedPoint
import com.stoyanvuchev.magicmessage.domain.serializer.BackgroundLayerSerializer
import com.stoyanvuchev.magicmessage.domain.serializer.ColorSerializer
import com.stoyanvuchev.magicmessage.domain.serializer.OffsetSerializer
import com.stoyanvuchev.magicmessage.domain.serializer.UriSerializer
import kotlinx.serialization.json.Json

@ProvidedTypeConverter
class CreationTypeConverters {

    private val json = Json { encodeDefaults = true }

    @TypeConverter
    fun fromStrokeModel(strokeModel: StrokeModel): String {
        return json.encodeToString(strokeModel)
    }

    @TypeConverter
    fun toStrokeModel(data: String): StrokeModel {
        return json.decodeFromString(data)
    }

    @TypeConverter
    fun fromStrokeList(strokes: List<StrokeModel>): String {
        return json.encodeToString(strokes)
    }

    @TypeConverter
    fun toStrokeList(data: String): List<StrokeModel> {
        return json.decodeFromString(data)
    }

    @TypeConverter
    fun fromEffect(effect: BrushEffect): String {
        return json.encodeToString(effect)
    }

    @TypeConverter
    fun toEffect(data: String): BrushEffect {
        return json.decodeFromString(data)
    }

    @TypeConverter
    fun fromColor(color: Color): String {
        return json.encodeToString(ColorSerializer, color)
    }

    @TypeConverter
    fun toColor(data: String): Color {
        return json.decodeFromString(ColorSerializer, data)
    }

    @TypeConverter
    fun fromTimedPoint(timedPoints: TimedPoint): String {
        return json.encodeToString(timedPoints)
    }

    @TypeConverter
    fun toTimedPoint(data: String): TimedPoint {
        return json.decodeFromString(data)
    }

    @TypeConverter
    fun fromTimedPointList(timedPoints: List<TimedPoint>): String {
        return json.encodeToString(timedPoints)
    }

    @TypeConverter
    fun toTimedPoints(data: String): List<TimedPoint> {
        return json.decodeFromString(data)
    }

    @TypeConverter
    fun fromOffset(offset: Offset): String {
        return json.encodeToString(OffsetSerializer, offset)
    }

    @TypeConverter
    fun toOffset(data: String): Offset {
        return json.decodeFromString(OffsetSerializer, data)
    }

    @TypeConverter
    fun fromUri(uri: Uri): String {
        return json.encodeToString(UriSerializer, uri)
    }

    @TypeConverter
    fun toUri(data: String): Uri {
        return json.decodeFromString(UriSerializer, data)
    }

    @TypeConverter
    fun fromUBackgroundLayer(bgLayer: BackgroundLayer): String {
        return json.encodeToString(BackgroundLayerSerializer, bgLayer)
    }

    @TypeConverter
    fun toBackgroundLayer(data: String): BackgroundLayer {
        return json.decodeFromString(BackgroundLayerSerializer, data)
    }

    @TypeConverter
    fun fromDrawConfiguration(drawConfiguration: DrawConfiguration): String {
        return json.encodeToString(drawConfiguration)
    }

    @TypeConverter
    fun toDrawConfiguration(data: String): DrawConfiguration {
        return json.decodeFromString(data)
    }

}