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

package com.stoyanvuchev.magicmessage.presentation.main.deleted_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent
import com.stoyanvuchev.magicmessage.domain.usecase.creation.CreationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeletedScreenViewModel @Inject constructor(
    private val useCases: CreationUseCases
) : ViewModel() {

    val state = combine(
        useCases.getDraftsUseCase(deleted = true),
        useCases.getExportedUseCase(deleted = true)
    ) { flows ->

        DeletedScreenState(
            draftedCreationsList = flows[0],
            exportedCreationsList = flows[1]
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DeletedScreenState()
    )

    private val _navigationEventChannel = Channel<NavigationEvent>()
    val navigationEventFlow = _navigationEventChannel.receiveAsFlow()

    fun onUIAction(
        action: DeletedScreenUIAction
    ) = when (action) {
        is DeletedScreenUIAction.RestoreCreation -> restoreCreation(action)
        is DeletedScreenUIAction.PermanentlyDeleteCreation -> permanentlyDeleteCreation(action)
    }

    fun onNavigationEvent(event: NavigationEvent) {
        sendNavigationEvent(event)
    }

    private fun restoreCreation(
        action: DeletedScreenUIAction.RestoreCreation
    ) {
        viewModelScope.launch {
            useCases.restoreDeletedCreation(action.creationId)
        }
    }

    private fun permanentlyDeleteCreation(
        action: DeletedScreenUIAction.PermanentlyDeleteCreation
    ) {
        viewModelScope.launch {
            useCases.permanentlyDeleteCreation(action.creationId)
        }
    }

    private fun sendNavigationEvent(
        navigationEvent: NavigationEvent
    ) {
        viewModelScope.launch { _navigationEventChannel.send(navigationEvent) }
    }

}