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

package com.stoyanvuchev.magicmessage.presentation.main.draw_screen

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.stoyanvuchev.magicmessage.domain.brush.BrushEffect
import com.stoyanvuchev.magicmessage.domain.brush.BrushThickness
import com.stoyanvuchev.magicmessage.domain.layer.BackgroundLayer
import com.stoyanvuchev.magicmessage.presentation.main.draw_screen.dialog.DialogEditType

@Immutable
interface DrawScreenUIAction {

    data object Undo : DrawScreenUIAction
    data object Redo : DrawScreenUIAction

    data class UpdateCanvasSize(
        val width: Int,
        val height: Int
    ) : DrawScreenUIAction

    data class OnStrokeEnded(
        val color: Color,
        val width: Float,
        val effect: BrushEffect
    ) : DrawScreenUIAction

    data class Export(
        val messageId: Long?
    ) : DrawScreenUIAction

    data class SetDialogEditType(
        val type: DialogEditType
    ) : DrawScreenUIAction

    data class SetBrushEffect(
        val effect: BrushEffect
    ) : DrawScreenUIAction

    data class SetBrushThickness(
        val thickness: BrushThickness
    ) : DrawScreenUIAction

    data class SetBrushColor(
        val color: Color
    ) : DrawScreenUIAction

    data class SetBGLayer(
        val layer: BackgroundLayer
    ) : DrawScreenUIAction

}