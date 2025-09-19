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

import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.createBitmap
import com.stoyanvuchev.magicmessage.core.ui.ext.toSmoothPath
import com.stoyanvuchev.magicmessage.core.ui.utils.ParticleUtils
import com.stoyanvuchev.magicmessage.domain.model.Particle
import com.stoyanvuchev.magicmessage.domain.model.StrokeModel
import com.waynejo.androidndkgif.GifEncoder
import java.io.File
import javax.inject.Inject
import kotlin.math.roundToInt

class Exporter @Inject constructor(
    private val context: Context
) {

    fun exportGif(
        strokes: List<StrokeModel>,
        width: Int,
        height: Int,
        updateProgress: (Int) -> Unit
    ): Uri? {

        val totalDuration = strokes.maxOfOrNull { it.points.last().timestamp } ?: 0L
        val frameInterval = 33L // ~30fps

        val values = ContentValues().apply {
            put(
                MediaStore.MediaColumns.DISPLAY_NAME,
                "magic_message_${System.currentTimeMillis()}.gif"
            )
            put(MediaStore.MediaColumns.MIME_TYPE, "image/gif")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MagicMessage")
        }

        val uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )!!

        val output = context.contentResolver.openOutputStream(uri)!!
        val bitmap = createBitmap(width, height)
        val canvas = Canvas(bitmap)

        // Create a temporary file for NDK encoder.
        val tempFile = File(
            context.cacheDir,
            "temp_export_magic_message_${System.currentTimeMillis()}.gif"
        )

        val encoder = GifEncoder()
        encoder.init(width, height, tempFile.absolutePath)

        try {

            var elapsedTime = 0L
            val activeParticles = mutableListOf<Particle>()

            for (stroke in strokes) {
                val strokeStart = stroke.points.firstOrNull()?.timestamp ?: 0L
                val strokeEnd = stroke.points.lastOrNull()?.timestamp ?: 0L

                for (frameTime in strokeStart..strokeEnd step frameInterval) {
                    // Clear canvas for this frame
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

                    // Draw all fully completed previous strokes
                    strokes.takeWhile { it != stroke }.forEach { prev ->
                        val path = prev.points.map { it.offset }.toSmoothPath()
                        Renderer.drawStroke(canvas, path.asAndroidPath(), prev)
                    }

                    // Draw current stroke progressively
                    val visiblePoints = stroke.points.filter { it.timestamp <= frameTime }
                    if (visiblePoints.isNotEmpty()) {
                        val path = visiblePoints.map { it.offset }.toSmoothPath()
                        Renderer.drawStroke(canvas, path.asAndroidPath(), stroke)
                    }

                    // Spawn new particles for points appearing in this frame
                    stroke.points.filter { it.timestamp in (frameTime - frameInterval)..frameTime }
                        .forEach { pt ->
                            activeParticles.addAll(
                                ParticleUtils.spawnParticles(pt.offset, stroke.color)
                            )
                        }

                    // Update and draw all active particles directly on canvas
                    val iterator = activeParticles.iterator()
                    while (iterator.hasNext()) {

                        val p = iterator.next()
                        p.position += p.velocity * (frameInterval / 16f)
                        p.age += frameInterval

                        if (p.age >= p.lifetime) iterator.remove()
                        else {
                            // Draw particle
                            canvas.drawCircle(
                                p.position.x,
                                p.position.y,
                                p.size,
                                Paint().apply {
                                    this.color = p.color.toArgb()
                                    this.style = Paint.Style.FILL
                                }
                            )
                        }

                    }

                    // Encode frame into GIF
                    encoder.encodeFrame(bitmap, frameInterval.toInt())

                    // Update progress
                    val progress =
                        ((elapsedTime + frameTime - strokeStart).toDouble() / totalDuration) * 100
                    updateProgress(progress.roundToInt().coerceIn(0, 100))
                }

                elapsedTime += strokeEnd - strokeStart
            }

        } finally {
            encoder.close()
        }

        // Copy temporary GIF to MediaStore output
        tempFile.inputStream().use { input ->
            output.use { out -> input.copyTo(out) }
        }

        tempFile.delete()
        return uri

    }

}