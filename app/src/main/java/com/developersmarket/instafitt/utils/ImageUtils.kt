package com.developersmarket.instafitt.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.AsyncTask
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import java.util.concurrent.ExecutionException

object ImageUtils {
    fun resizeImage(
        imageUrl: String,
        imageView: ImageView,
        context: Context,
        targetAspectRatio: Float
    ) {
        class ResizeImageTask : AsyncTask<String, Void, Bitmap>() {
            override fun doInBackground(vararg imageUrls: String): Bitmap? {
                val imageUrl = imageUrls[0]

                try {
                    val originalBitmap = Glide.with(context)
                        .asBitmap()
                        .load(imageUrl)
                        .submit()
                        .get()

                    val originalWidth = originalBitmap.width
                    val originalHeight = originalBitmap.height

                    val originalAspectRatio = originalWidth.toFloat() / originalHeight

                    val canvasWidth: Int
                    val canvasHeight: Int

                    if (originalAspectRatio > targetAspectRatio) {
                        // The width will change, calculate the new width and height
                        canvasWidth = originalWidth
                        canvasHeight = (canvasWidth / targetAspectRatio).toInt()
                    } else {
                        // The height will change, calculate the new width and height
                        canvasHeight = originalHeight
                        canvasWidth = (canvasHeight * targetAspectRatio).toInt()
                    }

                    val resizedBitmap =
                        Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(resizedBitmap)
                    canvas.drawColor(Color.WHITE)

                    val x: Float = ((canvasWidth - originalWidth) / 2).toFloat()
                    val y: Float = ((canvasHeight - originalHeight) / 2).toFloat()
                    canvas.drawBitmap(originalBitmap, x, y, null)
                    return resizedBitmap
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                return null
            }

            override fun onPostExecute(resizedBitmap: Bitmap?) {
                if (resizedBitmap != null) {
                    // Load the resized Bitmap into the ImageView using Glide
                    Glide.with(context)
                        .load(resizedBitmap)
                        .into(imageView)
                }
            }
        }

        val resizeTask = ResizeImageTask()
        resizeTask.execute(imageUrl)
    }

    fun resizeSquareImage(
        imageUrl: String,
        imageView: ImageView,
        context: Context,
        targetAspectRatio: Float
    ) {
        class ResizeImageTask : AsyncTask<String, Void, Bitmap>() {
            override fun doInBackground(vararg imageUrls: String): Bitmap? {
                val imageUrl = imageUrls[0]

                try {
                    val originalBitmap = Glide.with(context)
                        .asBitmap()
                        .load(imageUrl)
                        .submit()
                        .get()

                    val originalWidth = originalBitmap.width
                    val originalHeight = originalBitmap.height

                    val originalAspectRatio = originalWidth.toFloat() / originalHeight

                    if (originalAspectRatio > targetAspectRatio) {
                        // The width will change, calculate the new width
                        val newWidth = (originalHeight * targetAspectRatio).toInt()
                        val changeInPixels = newWidth - originalWidth
                    } else {
                        // The height will change, calculate the new height
                        val newHeight = (originalWidth / targetAspectRatio).toInt()
                        val changeInPixels = newHeight - originalHeight
                    }

                    val canvasSize = Math.max(originalWidth, originalHeight)
                    val resizedBitmap =
                        Bitmap.createBitmap(canvasSize, canvasSize, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(resizedBitmap)
                    canvas.drawColor(Color.WHITE)
                    val x = (canvasSize - originalWidth) / 2
                    val y = (canvasSize - originalHeight) / 2
                    canvas.drawBitmap(originalBitmap, x.toFloat(), y.toFloat(), null)
                    return resizedBitmap
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                return null
            }

            override fun onPostExecute(resizedBitmap: Bitmap?) {
                if (resizedBitmap != null) {
                    // Load the resized Bitmap into the ImageView using Glide
                    Glide.with(context)
                        .load(resizedBitmap)
                        .into(imageView)
                }
            }
        }

        val resizeTask = ResizeImageTask()
        resizeTask.execute(imageUrl)
    }
}



