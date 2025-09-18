package com.stoyanvuchev.magicmessage.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

@Composable
fun ParticleUpdater(controller: DrawingController) {
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {

            var lastTime = System.currentTimeMillis()
            while (isActive) {

                val now = System.currentTimeMillis()
                val delta = now - lastTime
                lastTime = now

                // Update physics off UI thread.
                controller.updateParticles(delta)

                // Sync to Compose state on Main thread.
                withContext(Dispatchers.Main) {
                    controller.syncParticlesToUI()
                }

                delay(16L) // ~60fps

            }

        }
    }
}
