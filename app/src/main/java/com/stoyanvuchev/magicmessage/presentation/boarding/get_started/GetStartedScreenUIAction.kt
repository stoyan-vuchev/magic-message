package com.stoyanvuchev.magicmessage.presentation.boarding.get_started

import androidx.compose.runtime.Immutable

@Immutable
interface GetStartedScreenUIAction {
    data object TogglePrivacyPolicy : GetStartedScreenUIAction
    data object ToggleTermsOfService : GetStartedScreenUIAction
}