package com.developersmarket.instafitt

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.developersmarket.instafitt.adapter.ItemAdapter
import com.developersmarket.instafitt.databinding.ActivityEditBinding
import com.developersmarket.instafitt.model.ItemEditAction
import com.developersmarket.instafitt.utils.ImageUtils

class EditActivity : AppCompatActivity() {
    private var itemEditActionArrayList = arrayListOf<ItemEditAction>()
    private lateinit var binding: ActivityEditBinding
    private lateinit var imageUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageUri = intent.getParcelableExtra(IMAGE_URI_EXTRA) ?: Uri.EMPTY
        binding.ivMainImage.setImageURI(imageUri)

        initData()
        initRecycler()
    }


    private fun initRecycler() {

        val adapter = ItemAdapter(itemEditActionArrayList, object : ItemAdapter.OnItemClickListener {
            override fun onItemClick(item: ItemEditAction) {
                val targetAspectRatio = when (item.name) {
                    "Square" -> 1f
                    "3:4" -> 3f / 4f
                    "3:2" -> 3f / 2f
                    "16:9" -> 16f / 9f
                    else -> 1f
                }
                if (targetAspectRatio.equals(1f)) {
                    ImageUtils.resizeSquareImage(
                        imageUri.toString(),
                        binding.ivMainImage,
                        this@EditActivity,
                        targetAspectRatio
                    )
                } else {
                    ImageUtils.resizeImage(
                        imageUri.toString(),
                        binding.ivMainImage,
                        this@EditActivity,
                        targetAspectRatio
                    )
                }
            }

        })
        binding.rvList.adapter = adapter
    }

    private fun initData() {
        itemEditActionArrayList.add(ItemEditAction("1:1", R.drawable.square))
        itemEditActionArrayList.add(ItemEditAction("3:4", R.drawable.square))
        itemEditActionArrayList.add(ItemEditAction("3:2", R.drawable.square))
        itemEditActionArrayList.add(ItemEditAction("16:9", R.drawable.square))
    }

    companion object {
        const val IMAGE_URI_EXTRA = "image_uri_extra"
    }
}