package com.stoyanvuchev.magicmessage.core.ui.utils

internal fun lerp(start: Float, end: Float, fraction: Float): Float {
    return start + (end - start) * fraction
}