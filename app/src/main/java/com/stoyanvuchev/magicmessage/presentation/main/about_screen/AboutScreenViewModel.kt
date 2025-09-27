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

package com.stoyanvuchev.magicmessage.presentation.main.about_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutScreenViewModel @Inject constructor() : ViewModel() {

    private val _uiActionChannel = Channel<AboutScreenUIAction>()
    val uiActionFlow = _uiActionChannel.receiveAsFlow()

    private val _navigationEventChannel = Channel<NavigationEvent>()
    val navigationEventFlow = _navigationEventChannel.receiveAsFlow()

    fun onUIAction(action: AboutScreenUIAction) {
        sendUIAction(action)
    }

    fun onNavigationEvent(event: NavigationEvent) {
        sendNavigationEvent(event)
    }

    private fun sendUIAction(action: AboutScreenUIAction) {
        viewModelScope.launch { _uiActionChannel.send(action) }
    }

    private fun sendNavigationEvent(event: NavigationEvent) {
        viewModelScope.launch { _navigationEventChannel.send(event) }
    }

}