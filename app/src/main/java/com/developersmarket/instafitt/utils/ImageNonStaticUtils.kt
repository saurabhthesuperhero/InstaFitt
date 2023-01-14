package com.developersmarket.instafitt.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.util.*

class ImageNonStaticUtils {
    private val app_name = "InstaFitt"
    fun saveImage(context: Context, imageView: ImageView, frameLayout: FrameLayout) {
        val bitmap = Bitmap.createBitmap(imageView.width, imageView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        // Draw the ImageView and its child views onto the canvas
        imageView.draw(canvas)
        frameLayout.draw(canvas)
        // Save the bitmap to a file
        val path_download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = UUID.randomUUID().toString() + ".png"
        val appDir = File(path_download, app_name)
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val file = File(appDir, fileName)
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()
        Toast.makeText(context, "Download Successfull", Toast.LENGTH_LONG).show();
    }

}