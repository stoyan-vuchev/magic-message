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

package com.stoyanvuchev.magicmessage.framework

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import assertk.assertThat
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.stoyanvuchev.magicmessage.domain.brush.BrushEffect
import com.stoyanvuchev.magicmessage.domain.layer.BackgroundLayer
import com.stoyanvuchev.magicmessage.domain.model.StrokeModel
import com.stoyanvuchev.magicmessage.domain.model.TimedPoint
import com.stoyanvuchev.magicmessage.framework.export.Exporter
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExporterTest {

    private lateinit var context: Context
    private lateinit var exporter: Exporter

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        exporter = Exporter(context)
    }

    @Test
    fun `exportGif should create a GIF and return non-null Uri`() = runTest {

        // Prepare a simple stroke with two points.
        val strokes = listOf(
            StrokeModel(
                points = listOf(
                    TimedPoint(
                        offset = Offset(100f, 100f),
                        timestamp = 0L
                    ),
                    TimedPoint(
                        offset = Offset(200f, 200f),
                        timestamp = 33L
                    )
                ),
                color = Color.Cyan,
                width = 12f,
                effect = BrushEffect.NONE
            )
        )

        var lastProgress = 0
        val uri = exporter.exportGif(
            strokes = strokes,
            width = 1080,
            height = 1920,
            background = BackgroundLayer.ColorLayer(Color.Black),
        ) { progress ->
            lastProgress = progress
        }

        assertThat(uri).isNotNull()
        assertThat(lastProgress in 0..100).isTrue()

        // Check that file actually exists
        val fileExists = context.contentResolver.openInputStream(uri!!)?.use { true } ?: false
        assertThat(fileExists).isTrue()

    }

}
