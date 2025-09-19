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
import androidx.core.app.NotificationCompat
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.domain.model.StrokeModel
import com.stoyanvuchev.magicmessage.framework.Exporter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

object ExportDataHolder {
    lateinit var strokes: List<StrokeModel>
}

@AndroidEntryPoint
class ExportGifService : Service() {

    @Inject
    lateinit var exporter: Exporter

    companion object Companion {
        private const val CHANNEL_ID = "gif_export_channel"
        private const val PROGRESS_NOTIFICATION_ID = 1
        private const val COMPLETED_NOTIFICATION_ID = 2
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val width = intent?.getIntExtra("width", 0) ?: 1080
        val height = intent?.getIntExtra("height", 0) ?: 1920

        startForeground(
            PROGRESS_NOTIFICATION_ID,
            buildProgressNotification(0)
        )

        CoroutineScope(Dispatchers.Default).launch {

            val uri = exporter.exportGif(
                strokes = ExportDataHolder.strokes,
                width = width,
                height = height
            ) { progress ->
                updateProgress(progress)
            }

            uri?.let {
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
            NotificationManager.IMPORTANCE_LOW
        ).apply { description = "Notifications for Magic Message GIF export" }
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    private fun buildProgressNotification(progress: Int): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Exporting GIF")
            .setContentText("Progress: $progress%")
            .setSmallIcon(R.drawable.export)
            //.setOnlyAlertOnce(true)
            .setOngoing(true)
            .setAutoCancel(false)
            .setProgress(100, progress, false)
            .build()

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
            .setAutoCancel(true)
            .build()

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(COMPLETED_NOTIFICATION_ID, notification)

    }

}