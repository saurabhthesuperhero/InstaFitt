package com.developersmarket.instafitt

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.developersmarket.instafitt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
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
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btSelectImage.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }
    }
}