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

package com.stoyanvuchev.magicmessage.framework.export

import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ExporterProgressObserver @Inject constructor() {

    private val _progress = MutableStateFlow(0)
    val progress: StateFlow<Int> = _progress.asStateFlow()

    private val _exporterState = MutableStateFlow(ExporterState.IDLE)
    val exporterState: StateFlow<ExporterState> = _exporterState.asStateFlow()

    private val _exportedUri = MutableStateFlow<Uri?>(null)
    val exportedUri: StateFlow<Uri?> = _exportedUri.asStateFlow()

    internal fun updateProgress(newProgress: Int) {
        _progress.update { newProgress }
    }

    internal fun updateExporterState(newState: ExporterState) {
        _exporterState.update { newState }
    }

    internal fun updateExportedUri(newUri: Uri) {
        _exportedUri.update { newUri }
    }

    fun resetAll() {
        _progress.update { 0 }
        _exporterState.update { ExporterState.IDLE }
        _exportedUri.update { null }
    }

}