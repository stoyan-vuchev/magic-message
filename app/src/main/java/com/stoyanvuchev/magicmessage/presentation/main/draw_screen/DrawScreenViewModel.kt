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

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stoyanvuchev.magicmessage.core.ui.DrawingController
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent
import com.stoyanvuchev.magicmessage.domain.brush.BrushEffect
import com.stoyanvuchev.magicmessage.domain.usecase.creation.CreationUseCases
import com.stoyanvuchev.magicmessage.framework.service.ExportDataHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawScreenViewModel @Inject constructor(
    private val useCases: CreationUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var id = savedStateHandle.get<Long?>("messageId")

    private val _drawingController = MutableStateFlow(DrawingController())
    val drawingController = _drawingController.asStateFlow()

    private val _state = MutableStateFlow(DrawScreenState(messageId = id))
    val state = _state.asStateFlow()

    private val _uiActionChannel = Channel<DrawScreenUIAction>()
    val uiActionFlow = _uiActionChannel.receiveAsFlow()

    private val _navigationEventChannel = Channel<NavigationEvent>()
    val navigationEventFlow = _navigationEventChannel.receiveAsFlow()

    fun onUIAction(action: DrawScreenUIAction) {
        when (action) {

            is DrawScreenUIAction.Undo -> _drawingController.value.undo()
            is DrawScreenUIAction.Redo -> _drawingController.value.redo()

            is DrawScreenUIAction.UpdateCanvasSize -> updateCanvasSize(action)

            is DrawScreenUIAction.OnStrokeEnded -> onStrokeEnded(
                action.color,
                action.width,
                action.effect
            )

            is DrawScreenUIAction.Export -> exportGif(action)

            is DrawScreenUIAction.SetDialogEditType -> setDialogEditType(action)
            is DrawScreenUIAction.SetBrushEffect -> setBrushEffect(action)
            is DrawScreenUIAction.SetBrushThickness -> setBrushThickness(action)
            is DrawScreenUIAction.SetBrushColor -> setBrushColor(action)
            is DrawScreenUIAction.SetBGLayer -> setBGLayer(action)

            else -> Unit

        }
    }

    fun onNavigationEvent(event: NavigationEvent) {
        saveDraft()
        sendNavigationEvent(event)
    }

    private fun updateCanvasSize(action: DrawScreenUIAction.UpdateCanvasSize) {
        _state.update {
            it.copy(
                drawConfiguration = it.drawConfiguration.copy(
                    canvasWidth = action.width,
                    canvasHeight = action.height
                )
            )
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
        saveDraft()
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
        saveDraft()
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
        saveDraft()
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
        saveDraft()
    }

    private fun setDialogEditType(action: DrawScreenUIAction.SetDialogEditType) {
        _state.update { it.copy(dialogEditType = action.type) }
    }

    private fun exportGif(action: DrawScreenUIAction.Export) {
        if (_drawingController.value.strokes.isNotEmpty()) {
            ExportDataHolder.strokes = _drawingController.value.strokes
            ExportDataHolder.bgLayer = _state.value.drawConfiguration.bgLayer
            sendUIAction(action)
        }
    }

    private fun sendUIAction(action: DrawScreenUIAction) {
        viewModelScope.launch { _uiActionChannel.send(action) }
    }

    private fun sendNavigationEvent(event: NavigationEvent) {
        viewModelScope.launch { _navigationEventChannel.send(event) }
    }

    private fun onStrokeEnded(
        color: Color,
        width: Float,
        effect: BrushEffect
    ) {
        _drawingController.value.endStroke(color, width, effect)
        saveDraft()
    }

    private fun saveDraft() {
        viewModelScope.launch(Dispatchers.IO) {
            val snapshot = _drawingController.value.snapshot()
            if (snapshot.strokes.isNotEmpty()) {
                id = useCases.saveOrUpdateUseCase(
                    draftId = id,
                    drawConfiguration = _state.value.drawConfiguration,
                    drawingSnapshot = snapshot
                )
            }
        }
    }

    private fun loadDraft(existingDraftId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.getByIdUseCase(existingDraftId)?.let { model ->

                _drawingController.update {
                    DrawingController.fromSnapshot(model.drawingSnapshot)
                }

                _state.update {
                    it.copy(
                        messageId = model.id,
                        drawConfiguration = model.drawConfiguration
                    )
                }

            }
        }
    }

    init {
        id?.let {
            loadDraft(it)
        }
    }

}