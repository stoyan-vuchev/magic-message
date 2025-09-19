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

package com.stoyanvuchev.magicmessage.core.ui

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.stoyanvuchev.magicmessage.core.ui.utils.ParticleUtils
import com.stoyanvuchev.magicmessage.domain.brush.BrushEffect
import com.stoyanvuchev.magicmessage.domain.model.Particle
import com.stoyanvuchev.magicmessage.domain.model.StrokeModel
import com.stoyanvuchev.magicmessage.domain.model.TimedPoint

@Stable
class DrawingController {

    // Max allowed points per session
    val maxPoints: Int = 500
    var totalPointCount by mutableIntStateOf(0)
        private set

    var drawingEnabled by mutableStateOf(true)
        private set

    val strokes = mutableStateListOf<StrokeModel>()

    private val _particles = mutableListOf<Particle>()
    val particles: SnapshotStateList<Particle> = mutableStateListOf()

    val currentPoints = mutableStateListOf<TimedPoint>()

    private val undoStack = ArrayDeque<StrokeModel>()
    private val redoStack = ArrayDeque<StrokeModel>()

    private var startTime: Long = 0
    private val lock = Any()

    fun startStroke(
        offset: Offset,
        color: Color,
        effect: BrushEffect
    ) {
        if (!drawingEnabled) return
        currentPoints.clear()
        startTime = System.currentTimeMillis()
        currentPoints.add(TimedPoint(offset, 0L))
        if (effect != BrushEffect.NONE) {
            spawnParticles(offset, color)
        }
    }

    fun addPoint(
        offset: Offset,
        color: Color,
        effect: BrushEffect
    ) {

        if (!drawingEnabled) return

        // Check if adding this point exceeds the max
        val prospectiveTotal = totalPointCount + currentPoints.size + 1
        if (prospectiveTotal > maxPoints) {
            drawingEnabled = false
            return
        }

        val t = System.currentTimeMillis() - startTime
        val point = TimedPoint(offset, t)
        currentPoints.add(point)
        if (effect != BrushEffect.NONE) {
            spawnParticles(offset, color)
        }

    }

    fun endStroke(
        color: Color,
        width: Float,
        effect: BrushEffect
    ) {
        if (currentPoints.isNotEmpty()) {

            val stroke = StrokeModel(
                points = currentPoints.toList(),
                color = color,
                width = width,
                effect = effect
            )

            strokes.add(stroke)
            undoStack.addLast(stroke)
            redoStack.clear()

            totalPointCount += stroke.points.size
            if (totalPointCount >= maxPoints) {
                drawingEnabled = false
            }

            currentPoints.clear()

        }
    }

    private fun spawnParticles(
        offset: Offset,
        color: Color
    ) {
        synchronized(lock) {
            _particles.addAll(
                ParticleUtils.spawnParticles(
                    at = offset,
                    brushColor = color
                )
            )
        }
    }

    fun updateParticles(deltaMs: Long) {
        synchronized(lock) {
            val iterator = _particles.iterator()
            while (iterator.hasNext()) {
                val p = iterator.next()
                p.position += p.velocity * (deltaMs / 16f)
                p.age += deltaMs
                if (p.age >= p.lifetime) iterator.remove() // safe removal
            }
        }
    }

    fun syncParticlesToUI() {
        particles.clear()
        particles.addAll(_particles)
    }

    fun clear() {
        strokes.clear()
        _particles.clear()
        currentPoints.clear()
        undoStack.clear()
        redoStack.clear()
        totalPointCount = 0
        drawingEnabled = true
    }

    fun undo() {

        val last = undoStack.removeLastOrNull() ?: return
        strokes.remove(last)
        redoStack.addLast(last)

        totalPointCount -= last.points.size
        if (!drawingEnabled && totalPointCount < maxPoints) {
            drawingEnabled = true
        }

    }

    fun redo() {

        val last = redoStack.removeLastOrNull() ?: return
        strokes.add(last)
        undoStack.addLast(last)

        totalPointCount += last.points.size
        if (totalPointCount >= maxPoints) {
            drawingEnabled = false
        }

    }

}