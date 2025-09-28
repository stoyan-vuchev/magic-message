/*
 * MIT License
 *
 * Copyright (c) 2025 Stoyan Vuchev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.stoyanvuchev.magicmessage.presentation.main.about_screen

import androidx.compose.runtime.Stable

@Stable
sealed class AboutScreenUIAction(val url: String) {

    data object PrivacyPolicy : AboutScreenUIAction(
        "https://github.com/stoyan-vuchev/magic-message/blob/main/legal/privacy_policy.md"
    )

    data object TermsOfService : AboutScreenUIAction(
        "https://github.com/stoyan-vuchev/magic-message/blob/main/legal/terms_of_service.md"
    )

    data object GitHub : AboutScreenUIAction(
        "https://github.com/stoyan-vuchev/magic-message"
    )

    data object DevGitHub : AboutScreenUIAction("https://github.com/stoyan-vuchev")
    data object DevX : AboutScreenUIAction("https://x.com/stoyan_vuchev")
    data object DevLinkedIn : AboutScreenUIAction("https://linkedin.com/in/stoyan-vuchev")

}