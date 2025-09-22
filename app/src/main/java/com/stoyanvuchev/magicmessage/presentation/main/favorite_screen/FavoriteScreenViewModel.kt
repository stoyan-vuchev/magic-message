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

package com.stoyanvuchev.magicmessage.presentation.main.favorite_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent
import com.stoyanvuchev.magicmessage.domain.usecase.CreationUseCases
import com.stoyanvuchev.magicmessage.framework.service.ExportDataHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteScreenViewModel @Inject constructor(
    private val useCases: CreationUseCases
) : ViewModel() {

    val state: StateFlow<FavoriteScreenState> = combine(
        flow = useCases.getExportedUseCase(onlyFavorite = true),
        flow2 = useCases.getDraftsUseCase(onlyFavorite = true)
    ) { exported, drafted ->

        FavoriteScreenState(
            draftedCreationsList = drafted,
            exportedCreationsList = exported
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FavoriteScreenState()
    )

    private val _uiActionChannel = Channel<FavoriteScreenUIAction>()
    val uiActionFlow = _uiActionChannel.receiveAsFlow()

    private val _navigationChannel = Channel<NavigationEvent>()
    val navigationFlow = _navigationChannel.receiveAsFlow()

    fun onUIAction(action: FavoriteScreenUIAction) {
        when (action) {
            is FavoriteScreenUIAction.RemoveFromFavorite -> removeFromFavorite(action)
            is FavoriteScreenUIAction.AddToFavorite -> addToFavorite(action)
            is FavoriteScreenUIAction.ExportGif -> exportGif(action)
        }
    }

    private fun removeFromFavorite(action: FavoriteScreenUIAction.RemoveFromFavorite) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                useCases.removeAsFavoriteUseCase(action.creationId)
            }
        }
    }

    private fun addToFavorite(action: FavoriteScreenUIAction.AddToFavorite) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                useCases.markAsFavoriteUseCase(action.creationId)
            }
        }
    }

    private fun exportGif(action: FavoriteScreenUIAction.ExportGif) {
        ExportDataHolder.strokes = action.creation.drawingSnapshot.strokes
        ExportDataHolder.bgLayer = action.creation.drawConfiguration.bgLayer
        sendUIAction(action)
    }

    fun onNavigationEvent(event: NavigationEvent) {
        sendNavigationEvent(event)
    }

    private fun sendUIAction(action: FavoriteScreenUIAction) {
        viewModelScope.launch { _uiActionChannel.send(action) }
    }

    private fun sendNavigationEvent(event: NavigationEvent) {
        viewModelScope.launch { _navigationChannel.send(event) }
    }

}