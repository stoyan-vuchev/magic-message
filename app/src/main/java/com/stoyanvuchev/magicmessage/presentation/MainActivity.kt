package com.stoyanvuchev.magicmessage.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.theme.MagicMessageTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MagicMessageTheme {

                Box(
                    modifier = Modifier.Companion
                        .fillMaxSize()
                        .systemBarsPadding(),
                    contentAlignment = Alignment.Companion.Center
                ) {

                    Text(
                        text = "Hello, ${stringResource(R.string.app_name)}!",
                        color = MaterialTheme.colorScheme.onBackground
                    )

                }

            }
        }
    }

}