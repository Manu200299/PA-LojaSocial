package com.example.lojasocial.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class LogoDownloadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val logoUrl = inputData.getString(KEY_LOGO_URL) ?: return@withContext Result.failure()

        try {
            val connection = URL(logoUrl).openConnection()
            connection.connect()
            val inputStream = connection.getInputStream()

            val file = File(applicationContext.filesDir, "loja_social_logo.png")
            FileOutputStream(file).use { output ->
                inputStream.copyTo(output)
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("LogoDownloadWorker", "Error downloading logo", e)
            Result.failure()
        }
    }

    companion object {
        const val KEY_LOGO_URL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFtL-B4gbbMxU-xnCG-BU5FF0SS0-4RmjgzA&s"
    }
}