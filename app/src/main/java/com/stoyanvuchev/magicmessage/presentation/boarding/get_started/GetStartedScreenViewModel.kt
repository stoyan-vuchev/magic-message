package com.stoyanvuchev.magicmessage.presentation.boarding.get_started

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetStartedScreenViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(GetStartedScreenState())
    val state = _state.asStateFlow()

    private val _navigationChannel = Channel<NavigationEvent>()
    val navigationFlow = _navigationChannel.receiveAsFlow()

    fun onUIAction(action: GetStartedScreenUIAction) {
        when (action) {

            is GetStartedScreenUIAction.TogglePrivacyPolicy -> {
                _state.update { it.copy(isPrivacyPolicyChecked = !it.isPrivacyPolicyChecked) }
            }

            is GetStartedScreenUIAction.ToggleTermsOfService -> {
                _state.update { it.copy(isTermsOfServiceChecked = !it.isTermsOfServiceChecked) }
            }

        }
    }

    fun onNavigationEvent(event: NavigationEvent) {
        sendNavigationAction(event)
    }

    private fun sendNavigationAction(event: NavigationEvent) {
        viewModelScope.launch { _navigationChannel.send(event) }
    }

}