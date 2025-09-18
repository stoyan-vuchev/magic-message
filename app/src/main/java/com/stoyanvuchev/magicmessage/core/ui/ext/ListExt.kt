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

package com.stoyanvuchev.magicmessage.core.ui.ext

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path

fun List<Offset>.toSmoothPath(): Path {

    val path = Path()
    if (isEmpty()) return path

    path.moveTo(
        x = first().x,
        y = first().y
    )

    for (i in 1 until size) {

        val prev = this[i - 1]
        val current = this[i]
        val midPoint = Offset(
            x = (prev.x + current.x) / 2f,
            y = (prev.y + current.y) / 2f
        )

        path.quadraticTo(
            x1 = prev.x,
            y1 = prev.y,
            x2 = midPoint.x,
            y2 = midPoint.y
        )

    }

    // Ensure last point is included.
    path.lineTo(
        x = last().x,
        y = last().y
    )

    return path

}