package com.stoyanvuchev.magicmessage.core.ui.event

import androidx.compose.runtime.Immutable
import com.stoyanvuchev.magicmessage.core.ui.navigation.NavigationScreen

@Immutable
interface NavigationEvent {
    data object NavigateUp : NavigationEvent
    data class NavigateTo(val screen: NavigationScreen) : NavigationEvent
}