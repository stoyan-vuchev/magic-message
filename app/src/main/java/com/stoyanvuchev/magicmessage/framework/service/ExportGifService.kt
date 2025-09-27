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

package com.stoyanvuchev.magicmessage.framework.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.domain.layer.BackgroundLayer
import com.stoyanvuchev.magicmessage.domain.model.StrokeModel
import com.stoyanvuchev.magicmessage.domain.usecase.creation.CreationUseCases
import com.stoyanvuchev.magicmessage.framework.export.Exporter
import com.stoyanvuchev.magicmessage.framework.export.ExporterProgressObserver
import com.stoyanvuchev.magicmessage.framework.export.ExporterState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

object ExportDataHolder {
    lateinit var strokes: List<StrokeModel>
    lateinit var bgLayer: BackgroundLayer
}

@AndroidEntryPoint
class ExportGifService : Service() {

    @Inject
    lateinit var exporter: Exporter

    @Inject
    lateinit var progressObserver: ExporterProgressObserver

    @Inject
    lateinit var creationUseCases: CreationUseCases

    companion object Companion {
        private const val CHANNEL_ID = "gif_export_channel"
        private const val PROGRESS_NOTIFICATION_ID = 1
        private const val COMPLETED_NOTIFICATION_ID = 2
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        progressObserver.resetAll()
        progressObserver.updateExporterState(ExporterState.PREPARING)

        createNotificationChannel()
        startForeground(
            PROGRESS_NOTIFICATION_ID,
            buildProgressNotification(0)
        )

        val width = intent?.getIntExtra("width", 0) ?: 1080
        val height = intent?.getIntExtra("height", 0) ?: 1920
        val messageId = intent?.getLongExtra("messageId", 0L) ?: 0L

        CoroutineScope(Dispatchers.Default + SupervisorJob()).launch {

            progressObserver.updateExporterState(ExporterState.EXPORTING)

            exporter.exportGif(
                strokes = ExportDataHolder.strokes,
                width = width,
                height = height,
                background = ExportDataHolder.bgLayer
            ) { progress ->
                progressObserver.updateProgress(progress)
                updateProgress(progress)
            }?.let {

                withContext(Dispatchers.IO) {
                    creationUseCases.markAsExportedUseCase(messageId = messageId)
                }

                progressObserver.updateExportedUri(it)
                progressObserver.updateExporterState(ExporterState.COMPLETED)

                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()

                showCompletedNotification(it)

            }

        }.invokeOnCompletion {

            stopForeground(STOP_FOREGROUND_DETACH)
            stopSelf()

        }

        return START_NOT_STICKY

    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "GIF Export",
            NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "Notifications for Magic Message GIF export." }
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    private fun buildProgressNotification(progress: Int): Notification {
        Log.d("ExportGifService", "Progress: $progress%")
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Exporting GIF")
            .setContentText("Progress: $progress%")
            .setSmallIcon(R.drawable.export)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setAutoCancel(false)
            .setProgress(100, progress, false)
            .build()
    }

    private fun updateProgress(progress: Int) {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(PROGRESS_NOTIFICATION_ID, buildProgressNotification(progress))
    }

    private fun showCompletedNotification(uri: Uri) {
        val viewIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "image/gif")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            viewIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Export Complete")
            .setContentText("Tap to view your GIF 🎉")
            .setSmallIcon(R.drawable.logo_icon)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .build()

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(COMPLETED_NOTIFICATION_ID, notification)

    }

}