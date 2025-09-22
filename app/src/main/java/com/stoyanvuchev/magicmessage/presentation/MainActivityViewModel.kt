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

package com.stoyanvuchev.magicmessage.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stoyanvuchev.magicmessage.core.ui.theme.ThemeMode
import com.stoyanvuchev.magicmessage.domain.preferences.AppPreferences
import com.stoyanvuchev.magicmessage.framework.export.ExporterProgressObserver
import com.stoyanvuchev.magicmessage.framework.export.ExporterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val progressObserver: ExporterProgressObserver,
    appPreferences: AppPreferences
) : ViewModel() {

    val state = combine(
        appPreferences.getIsBoardingComplete(),
        appPreferences.getThemeMode(),
        progressObserver.exporterState,
        progressObserver.progress,
        progressObserver.exportedUri
    ) { flows ->

        MainActivityState(
            isBoardingComplete = flows[0] as Boolean?,
            themeMode = flows[1] as ThemeMode,
            exporterState = flows[2] as ExporterState,
            exporterProgress = flows[3] as Int,
            exportedUri = flows[4] as Uri?
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainActivityState()
    )

    fun onDismissExportDialog() {
        viewModelScope.launch {
            progressObserver.resetAll()
        }
    }

}