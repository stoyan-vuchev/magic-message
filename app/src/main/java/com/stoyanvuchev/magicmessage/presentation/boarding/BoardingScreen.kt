package com.stoyanvuchev.magicmessage.presentation.boarding

import com.stoyanvuchev.magicmessage.core.ui.navigation.NavigationScreen
import kotlinx.serialization.Serializable

@Serializable
sealed interface BoardingScreen : NavigationScreen {

    @Serializable
    data object Navigation : BoardingScreen

    @Serializable
    data object GetStarted : BoardingScreen

    @Serializable
    data object PrivacyPolicy : BoardingScreen

    @Serializable
    data object TermsOfService : BoardingScreen

    @Serializable
    data object PermissionNotice : BoardingScreen

}