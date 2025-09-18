package com.stoyanvuchev.magicmessage.core.ui.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.stoyanvuchev.magicmessage.domain.model.Particle

object ParticleUtils {

    fun spawnParticles(
        at: Offset,
        brushColor: Color
    ): List<Particle> {
        val count = (1..6).random() // random number of particles per point
        return List(count) {
            Particle(
                position = at,
                velocity = Offset(
                    x = (-1..1).random().toFloat() * 3f,
                    y = (-1..1).random().toFloat() * 3f
                ),
                color = brushColor.copy(alpha = (50..100).random().toFloat() / 100),
                size = (5..10).random().toFloat(),
                lifetime = (300..1000).random().toLong()
            )
        }
    }

}