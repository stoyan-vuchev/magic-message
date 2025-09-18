package com.stoyanvuchev.magicmessage.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stoyanvuchev.magicmessage.core.ui.DrawingController
import com.stoyanvuchev.magicmessage.core.ui.components.DrawingCanvas
import com.stoyanvuchev.magicmessage.core.ui.theme.MagicMessageTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MagicMessageTheme {

                Column(
                    modifier = Modifier.Companion
                        .fillMaxSize()
                        .systemBarsPadding(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    val viewModel = hiltViewModel<DummyViewModel>()
                    val controller by viewModel.controller.collectAsStateWithLifecycle()

                    DrawingCanvas(
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .fillMaxWidth()
                            .aspectRatio(3f / 4f)
                            .background(Color.Black),
                        controller = controller
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val progress by remember(controller.totalPointCount) {
                        derivedStateOf {
                            controller.totalPointCount.toFloat() / controller.maxPoints
                        }
                    }

                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        progress = { progress }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        TextButton(
                            onClick = remember { { controller.clear() } }
                        ) { Text(text = "Reset") }

                        TextButton(
                            onClick = remember { { controller.undo() } }
                        ) { Text(text = "Undo") }

                        TextButton(
                            onClick = remember { { controller.redo() } }
                        ) { Text(text = "Redo") }

                    }

                }

            }
        }
    }

}

@HiltViewModel
class DummyViewModel @Inject constructor() : ViewModel() {

    private val _controller = MutableStateFlow(DrawingController())
    val controller = _controller.asStateFlow()

}