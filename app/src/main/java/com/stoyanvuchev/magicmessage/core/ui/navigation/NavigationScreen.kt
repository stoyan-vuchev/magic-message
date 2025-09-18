package com.stoyanvuchev.magicmessage.core.ui.navigation

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
interface NavigationScreen

@Serializable
data object InitialScreen : NavigationScreen

