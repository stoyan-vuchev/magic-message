package com.stoyanvuchev.magicmessage.framework.service

import android.content.Context
import android.content.Intent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import assertk.assertThat
import assertk.assertions.isGreaterThan
import assertk.assertions.isNotNull
import com.stoyanvuchev.magicmessage.domain.BrushType
import com.stoyanvuchev.magicmessage.domain.model.StrokeModel
import com.stoyanvuchev.magicmessage.domain.model.TimedPoint
import com.stoyanvuchev.magicmessage.framework.Exporter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExportGifServiceInstrumentedTest {

    private lateinit var context: Context
    private lateinit var exporter: Exporter

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        exporter = Exporter(context)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun exportService_exportsGifSuccessfully() = runTest {

        // Prepare strokes
        val strokes = listOf(
            StrokeModel(
                points = listOf(
                    TimedPoint(
                        offset = Offset(100f, 100f),
                        timestamp = 0L
                    ),
                    TimedPoint(
                        offset = Offset(200f, 200f),
                        timestamp = 33L
                    )
                ),
                color = Color.Cyan,
                width = 12f,
                brush = BrushType.NORMAL
            )
        )

        ExportDataHolder.strokes = strokes

        // Start service
        val intent = Intent(
            context,
            ExportGifService::class.java
        ).apply {
            putExtra("width", 1080)
            putExtra("height", 1920)
        }

        context.startService(intent)

        // Give some time for coroutine to finish
        // (can use advanceUntilIdle for proper control)
        advanceUntilIdle()

        val uri = exporter.exportGif(
            strokes = strokes,
            width = 100,
            height = 100
        ) { _ -> }

        assertThat(uri).isNotNull()

        // Check that file exists and is non-empty
        val inputStream = context.contentResolver.openInputStream(uri!!)
        assertThat(inputStream).isNotNull()
        assertThat(inputStream!!.available()).isGreaterThan(0)

    }

}
