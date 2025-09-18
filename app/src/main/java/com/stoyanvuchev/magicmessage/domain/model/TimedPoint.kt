package com.stoyanvuchev.magicmessage.domain.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset

@Stable
data class TimedPoint(
    val offset: Offset,
    val timestamp: Long
)