package com.developersmarket.instafitt

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.developersmarket.instafitt.adapter.ImageAdapter
import com.developersmarket.instafitt.databinding.ActivityMainBinding
import com.developersmarket.instafitt.model.ImageModel
import com.developersmarket.instafitt.utils.AdLoader
import java.io.File
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.sanojpunchihewa.updatemanager.UpdateManager
import com.sanojpunchihewa.updatemanager.UpdateManagerConstant


class MainActivity : AppCompatActivity() {
    private val REQUEST_WRITE_STORAGE = 112
    private var mAdapter: ImageAdapter? = null
    lateinit var mUpdateManager: UpdateManager

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result: Uri? ->
            if (result != null) {
                val intent = Intent(this, EditActivity::class.java).apply {
                    putExtra(EditActivity.IMAGE_URI_EXTRA, result)
                }
                startActivity(intent)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AdLoader.initializeAds(this)
        AdLoader.loadInterstitialAd(getString(R.string.Admob_adUnitId_Interstitial))
        AdLoader.loadRewardedInterstitialAd(getString(R.string.Admob_adUnitId_Interstitial_Rewarded))

        requestPermission()

        binding.btSelectImage.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }
        mUpdateManager = UpdateManager.Builder(this).mode(UpdateManagerConstant.IMMEDIATE);
        mUpdateManager.start();
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_WRITE_STORAGE
        )
    }

    companion object {
        private lateinit var binding: ActivityMainBinding

        fun rvScrolltoPosition(pos: Int) {
            binding.rvList.scrollToPosition(pos)
        }
        val app_name = "InstaFitt"

        private const val TAG = "MainActivity"

    }


    override fun onResume() {
        super.onResume()

        // Retrieve the list of image file paths

        val imageFilePaths = getImageFilePaths()
        val imageModels = mutableListOf<ImageModel>()

        if (imageFilePaths != null && imageFilePaths.isNotEmpty()) {
            binding.rvList.visibility = View.VISIBLE
            binding.laEmpty.visibility = View.GONE
        } else {
            binding.rvList.visibility = View.GONE
            binding.laEmpty.visibility = View.VISIBLE
        }
        if (imageFilePaths != null) {
            for (filePath in imageFilePaths) {
                val imageModel = ImageModel()
                imageModel.imgUrl = filePath
                imageModels.add(imageModel)
            }
        }

//        Collections.reverse(imageFilePaths);
        mAdapter =
            ImageAdapter(imageFilePaths as List<String>, object : ImageAdapter.OnClickListener {
                override fun onRowClick(position: Int) {
                    Log.d(Companion.TAG, "onRowClick() called with: position = $position")
                    val intent = Intent(applicationContext, ImageActivity::class.java)
                    val args = Bundle()
                    args.putSerializable("ARRAYLIST", imageModels as Serializable)
                    intent.putExtra("BUNDLE", args)
                    intent.putExtra("imageLoc", position)
                    startActivity(intent)
                }
            })
//        val layoutManager = GridLayoutManager(this,2)
//        layoutManager.reverseLayout = true
//        binding.rvList.layoutManager = layoutManager

        binding.rvList.adapter = mAdapter


    }

    private fun getImageFilePaths(): List<String?>? {
//        val directory = File(Environment.getExternalStorageDirectory(), "Download/InstaFitt")
        val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), app_name)
        val files: Array<out File>? = directory.listFiles()
        val imageFilePaths: MutableList<String?> = ArrayList()
        if (files == null) return imageFilePaths
        for (file in files) {
            if (file.name.endsWith(".jpg") || file.name.endsWith(".png") || file.name.endsWith(".jpeg")) {
                imageFilePaths.add(file.absolutePath)
            }
        }
        return imageFilePaths
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_WRITE_STORAGE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, do your work
                } else {
                    // permission denied, disable the functionality that depends on this permission
                }
                return
            }
        }
    }
}