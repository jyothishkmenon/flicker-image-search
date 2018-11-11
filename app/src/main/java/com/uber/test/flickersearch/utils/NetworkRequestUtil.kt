package com.uber.test.flickersearch.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.BufferedInputStream
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import kotlin.text.StringBuilder

object NetworkRequestUtil {
    private const val BYTES_TO_READ = 1024

    fun getDataFromNetwork(url: String, map: Map<String, String>): String? {
        try {
            val urlBuilder = StringBuilder(url)
            map.forEach {
                urlBuilder.append("&${it.key}=${it.value}")
            }
            val urlObj = URL(urlBuilder.toString())
            val httpURLConnection = urlObj.openConnection() as HttpURLConnection
            httpURLConnection.setRequestProperty("Content-Type", "application/json")
            val finalString = StringBuilder()
            try {
                val inputStream = BufferedInputStream(httpURLConnection.inputStream)
                val content = ByteArray(BYTES_TO_READ)
                var bytesRead = inputStream.read(content)
                while (bytesRead != -1) {
                    finalString.append(String(content, 0, bytesRead))
                    bytesRead = inputStream.read(content)
                }
            } catch (e: IOException) { e.printStackTrace()
            } catch (e: Exception) { e.printStackTrace()
            } finally {
                httpURLConnection.disconnect()
                return finalString.toString()
            }
        } catch (e: IOException) { e.printStackTrace(); return null
        } catch (e: Exception) { e.printStackTrace(); return null }
    }

    fun getBitmapFromUrl(src: String): Bitmap? {
        return try {
            val url = java.net.URL(src)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}