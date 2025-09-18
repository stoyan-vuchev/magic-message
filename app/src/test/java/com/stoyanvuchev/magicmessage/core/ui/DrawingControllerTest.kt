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

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.stoyanvuchev.magicmessage.domain.BrushType
import org.junit.Before
import org.junit.Test

class DrawingControllerTest {

    private lateinit var controller: DrawingController

    @Before
    fun setup() {
        controller = DrawingController()
    }

    @Test
    fun `startStroke initializes currentPoints with first point`() {
        controller.startStroke(Offset(1f, 2f), Color.Red)
        assertThat(controller.currentPoints).hasSize(1)
        assertThat(controller.currentPoints.first().offset).isEqualTo(Offset(1f, 2f))
        assertThat(controller.totalPointCount).isEqualTo(0)
    }

    @Test
    fun `addPoint appends to currentPoints`() {
        controller.startStroke(Offset(0f, 0f), Color.Red)
        controller.addPoint(Offset(5f, 5f), Color.Red)
        assertThat(controller.currentPoints).hasSize(2)
    }

    @Test
    fun `endStroke creates stroke and clears currentPoints`() {
        controller.startStroke(Offset(0f, 0f), Color.Red)
        controller.addPoint(Offset(10f, 10f), Color.Red)
        controller.endStroke(Color.Red, 4f, BrushType.NORMAL)
        assertThat(controller.strokes).hasSize(1)
        assertThat(controller.strokes.first().points).hasSize(2) // start + added point
        assertThat(controller.currentPoints).isEmpty()
        assertThat(controller.totalPointCount).isEqualTo(2)
    }

    @Test
    fun `exceeding maxPoints disables drawing`() {
        // Fill up to the limit
        repeat(controller.maxPoints) {
            controller.startStroke(Offset(it.toFloat(), 0f), Color.Black)
            controller.addPoint(Offset(it.toFloat(), 1f), Color.Black)
            controller.endStroke(Color.Black, 2f, BrushType.NORMAL)
        }

        assertThat(controller.drawingEnabled).isFalse()
    }

    @Test
    fun `undo removes last stroke`() {
        controller.startStroke(Offset(0f, 0f), Color.Blue)
        controller.addPoint(Offset(1f, 1f), Color.Blue)
        controller.endStroke(Color.Blue, 2f, BrushType.NORMAL)

        assertThat(controller.strokes).hasSize(1)

        controller.undo()
        assertThat(controller.strokes).isEmpty()
        assertThat(controller.totalPointCount).isEqualTo(0)
    }

    @Test
    fun `clear resets everything`() {
        controller.startStroke(Offset(0f, 0f), Color.Magenta)
        controller.addPoint(Offset(3f, 3f), Color.Magenta)
        controller.endStroke(Color.Magenta, 5f, BrushType.NORMAL)

        controller.clear()

        assertThat(controller.strokes).isEmpty()
        assertThat(controller.currentPoints).isEmpty()
        assertThat(controller.totalPointCount).isEqualTo(0)
        assertThat(controller.drawingEnabled).isTrue()
    }

    @Test
    fun `redo restores last undone stroke`() {
        controller.startStroke(Offset(0f, 0f), Color.Green)
        controller.addPoint(Offset(2f, 2f), Color.Green)
        controller.endStroke(Color.Green, 3f, BrushType.NORMAL)

        controller.undo()
        assertThat(controller.strokes).isEmpty()

        controller.redo()
        assertThat(controller.strokes).hasSize(1)
    }

}
