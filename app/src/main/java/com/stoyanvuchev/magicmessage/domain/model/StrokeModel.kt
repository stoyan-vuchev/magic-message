package com.stoyanvuchev.magicmessage.domain.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.stoyanvuchev.magicmessage.domain.BrushType

@Stable
data class StrokeModel(
    val points: List<TimedPoint>,
    val color: Color,
    val width: Float,
    val brush: BrushType
)