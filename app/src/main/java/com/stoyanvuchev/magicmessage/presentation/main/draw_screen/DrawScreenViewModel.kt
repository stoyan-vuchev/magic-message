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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stoyanvuchev.magicmessage.core.ui.DrawingController
import com.stoyanvuchev.magicmessage.framework.export.ExporterProgressObserver
import com.stoyanvuchev.magicmessage.framework.service.ExportDataHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawScreenViewModel @Inject constructor(
    private val progressObserver: ExporterProgressObserver
) : ViewModel() {

    private val _drawingController = MutableStateFlow(DrawingController())
    val drawingController = _drawingController.asStateFlow()

    private val _state = MutableStateFlow(DrawScreenState())
    val state = _state.asStateFlow()

    private val _uiActionChannel = Channel<DrawScreenUIAction>()
    val uiActionFlow = _uiActionChannel.receiveAsFlow()

    val exporterState = progressObserver.exporterState
    val exporterProgress = progressObserver.progress
    val exportedUri = progressObserver.exportedUri

    fun onUIAction(action: DrawScreenUIAction) {
        when (action) {

            is DrawScreenUIAction.Undo -> _drawingController.value.undo()
            is DrawScreenUIAction.Redo -> _drawingController.value.redo()
            is DrawScreenUIAction.Export -> exportGif(action)
            is DrawScreenUIAction.DismissExporterDialog -> progressObserver.resetAll()

            is DrawScreenUIAction.SetDialogEditType -> setDialogEditType(action)
            is DrawScreenUIAction.SetBrushEffect -> setBrushEffect(action)
            is DrawScreenUIAction.SetBrushThickness -> setBrushThickness(action)
            is DrawScreenUIAction.SetBrushColor -> setBrushColor(action)
            is DrawScreenUIAction.SetBGLayer -> setBGLayer(action)

            else -> Unit

        }
    }

    private fun setBGLayer(
        action: DrawScreenUIAction.SetBGLayer
    ) {
        _state.update {
            it.copy(
                drawConfiguration = it.drawConfiguration.copy(
                    bgLayer = action.layer
                )
            )
        }
    }

    private fun setBrushColor(
        action: DrawScreenUIAction.SetBrushColor
    ) {
        _state.update {
            it.copy(
                drawConfiguration = it.drawConfiguration.copy(
                    color = action.color
                )
            )
        }
    }

    private fun setBrushThickness(
        action: DrawScreenUIAction.SetBrushThickness
    ) {
        _state.update {
            it.copy(
                drawConfiguration = it.drawConfiguration.copy(
                    thickness = action.thickness
                )
            )
        }
    }

    private fun setBrushEffect(
        action: DrawScreenUIAction.SetBrushEffect
    ) {
        _state.update {
            it.copy(
                drawConfiguration = it.drawConfiguration.copy(
                    effect = action.effect
                )
            )
        }
    }

    private fun setDialogEditType(action: DrawScreenUIAction.SetDialogEditType) {
        _state.update { it.copy(dialogEditType = action.type) }
    }

    private fun exportGif(action: DrawScreenUIAction.Export) {
        ExportDataHolder.strokes = _drawingController.value.strokes
        ExportDataHolder.bgLayer = _state.value.drawConfiguration.bgLayer
        sendUIAction(action)
    }

    private fun sendUIAction(action: DrawScreenUIAction) {
        viewModelScope.launch { _uiActionChannel.send(action) }
    }

}