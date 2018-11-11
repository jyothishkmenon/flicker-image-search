package com.uber.test.flickersearch.imagegrid

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.LruCache
import android.widget.ImageView
import com.uber.test.flickersearch.utils.NetworkRequestUtil.getBitmapFromUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.launch

object ImageLoader {
    private const val CACHE_SIZE = 4 * 1024 * 1024
    private val jobMap = mutableMapOf<Int, Job>()
    private val cache: LruCache<String, Bitmap?> = LruCache(CACHE_SIZE)

    fun fetchImage(imageView: ImageView, url: String) {
        if (jobMap.containsKey(imageView.hashCode())) {
            jobMap[imageView.hashCode()]?.cancel()
            jobMap.remove(imageView.hashCode())
        }
        jobMap[imageView.hashCode()] = GlobalScope.launch {
            val bitmap = if (cache[url] == null) {
                getBitmapFromUrl(url)?.let {
                    getResizedBitmap(it, getOrDefault(imageView.height), getOrDefault(imageView.width))
                }
            } else cache[url]
            bitmap?.let { launch(Dispatchers.Main) { imageView.setImageBitmap(it) }}
        }
    }

    private fun getOrDefault(value: Int) = if (value > 0) value else 240

    private fun getResizedBitmap(bm: Bitmap, newHeight: Int, newWidth: Int): Bitmap {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false)
    }
}