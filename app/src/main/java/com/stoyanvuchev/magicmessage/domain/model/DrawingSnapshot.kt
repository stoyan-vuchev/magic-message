package com.stoyanvuchev.magicmessage.domain.model

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class DrawingSnapshot(
    val totalPointCount: Int,
    val drawingEnabled: Boolean,
    @Stable val strokes: List<StrokeModel>,
    @Stable val currentPoints: List<TimedPoint>,
    @Stable val undoStack: List<StrokeModel>,
    @Stable val redoStack: List<StrokeModel>,
    val startTime: Long
)