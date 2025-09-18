package com.stoyanvuchev.magicmessage.presentation.boarding.get_started

import androidx.compose.runtime.Stable

@Stable
data class GetStartedScreenState(
    val isPrivacyPolicyChecked: Boolean = false,
    val isTermsOfServiceChecked: Boolean = false
)