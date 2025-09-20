package com.stoyanvuchev.magicmessage.domain.model

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class DrawingSnapshot(
    val totalPointCount: Int,
    val drawingEnabled: Boolean,
    val strokes: List<StrokeModel>,
    val currentPoints: List<TimedPoint>,
    val undoStack: List<StrokeModel>,
    val redoStack: List<StrokeModel>,
    val startTime: Long
)