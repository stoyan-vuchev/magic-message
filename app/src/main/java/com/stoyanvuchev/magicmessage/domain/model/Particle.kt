package com.stoyanvuchev.magicmessage.domain.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Stable
data class Particle(
    var position: Offset,
    var velocity: Offset,
    var color: Color,
    var size: Float,
    var lifetime: Long,
    var age: Long = 0L
)