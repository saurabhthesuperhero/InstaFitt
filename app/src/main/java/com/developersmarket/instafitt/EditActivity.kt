package com.developersmarket.instafitt

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.developersmarket.instafitt.adapter.ItemAdapter
import com.developersmarket.instafitt.databinding.ActivityEditBinding
import com.developersmarket.instafitt.model.ItemEditAction

class EditActivity : AppCompatActivity() {
    private var itemList = arrayListOf<ItemEditAction>()
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

        val adapter = ItemAdapter(itemList, object : ItemAdapter.OnItemClickListener {
            override fun onItemClick(item: ItemEditAction) {

            }

        })
        binding.rvList.adapter = adapter
    }

    private fun initData() {
        itemList.add(ItemEditAction("Square", R.drawable.square))
        itemList.add(ItemEditAction("3:4", R.drawable.square))
        itemList.add(ItemEditAction("3:2", R.drawable.square))
        itemList.add(ItemEditAction("16:9", R.drawable.square))
    }

    companion object {
        const val IMAGE_URI_EXTRA = "image_uri_extra"
    }
}