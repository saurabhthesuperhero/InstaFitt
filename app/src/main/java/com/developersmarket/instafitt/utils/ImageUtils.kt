package com.developersmarket.instafitt.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Environment
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.developersmarket.instafitt.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.ExecutionException

object ImageUtils {
    var canvasColor= Color.WHITE
    var backBitmap: Bitmap? =null
    val app_name="InstaFitt"

    //COLOR, IMAGE
    var mode= "COLOR"
    fun resizeImage(
        imageUrl: String,
        imageBackUrl: String,
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
                    backBitmap = if (imageBackUrl != "" && mode.equals("IMAGE")){
                        Glide.with(context)
                            .asBitmap()
                            .load(imageBackUrl)
                            .submit()
                            .get()
                    }else{
                        null
                    }
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
                    canvas.drawColor(canvasColor)

                    val x: Float = ((canvasWidth - originalWidth) / 2).toFloat()
                    val y: Float = ((canvasHeight - originalHeight) / 2).toFloat()

                    if (backBitmap != null) {
//                        canvas.drawBitmap(backBitmap!!, 0F, 0F, null)
                        val src = Rect(0, 0, backBitmap!!.width, backBitmap!!.height)
                        val dst = Rect(0, 0, canvas.width, canvas.height)
                        canvas.drawBitmap(backBitmap!!, src, dst, null)
                    }

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
        imageBackUrl: String,
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

                    backBitmap = if (imageBackUrl != "" && mode == "IMAGE"){
                        Glide.with(context)
                            .asBitmap()
                            .load(imageBackUrl)
                            .submit()
                            .get()
                    }else{
                        null
                    }
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
                    canvas.drawColor(canvasColor)
                    val x = (canvasSize - originalWidth) / 2
                    val y = (canvasSize - originalHeight) / 2
                    if (backBitmap != null) {
//                        canvas.drawBitmap(backBitmap!!, 0F, 0F, null)
                        val src = Rect(0, 0, backBitmap!!.width, backBitmap!!.height)
                        val dst = Rect(0, 0, canvas.width, canvas.height)
                        canvas.drawBitmap(backBitmap!!, src, dst, null)
                    }
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

    fun downloadImageFromImageView(context: Context, imageView: ImageView) {
        // create a bitmap from the ImageView's drawable
        val drawable = imageView.drawable as BitmapDrawable
        val bitmap = drawable.bitmap

        // get the SD card's root directory
        val path_download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = UUID.randomUUID().toString() + ".png"
        // create a directory for the app
        val appDir = File(path_download, app_name)
        if (!appDir.exists()) {
            appDir.mkdir()
        }

        // create a file to save the image
        val file = File(appDir, fileName)

        try {
            // create a file output stream and save the image
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun shareImage(context: Context, imageView: ImageView) {
        // create a bitmap from the ImageView's drawable
        val drawable = imageView.drawable as BitmapDrawable
        val bitmap = drawable.bitmap

        // generate a random file name
        val fileName = UUID.randomUUID().toString() + ".png"

        // create a file to save the image
        val file = File(context.cacheDir, fileName)

        try {
            // create a file output stream and save the image
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // create the share intent and start the activity
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/png"
        val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID  + ".provider", file)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(Intent.createChooser(intent, "Share image"))
    }


}