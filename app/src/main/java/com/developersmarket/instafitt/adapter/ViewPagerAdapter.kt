package com.developersmarket.instafitt.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.developersmarket.instafitt.EditActivity
import com.developersmarket.instafitt.R
import com.developersmarket.instafitt.model.ImageModel
import java.util.*
import kotlin.collections.ArrayList


class ViewPagerAdapter(context: Context, `object`: ArrayList<ImageModel>, loc: Int) :
    PagerAdapter() {
    // Context object
    var context: Context
    private val TAG = "ViewPagerAdapter"
    var objectList: ArrayList<ImageModel>

    // Layout Inflater
    var mLayoutInflater: LayoutInflater
    var loc: Int

    // Viewpager Constructor
    init {
        this.context = context
        objectList = `object`
        this.loc = loc
        mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        // return the number of images
        return objectList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // inflating the item.xml
        val itemView: View = mLayoutInflater.inflate(R.layout.item_only_image, container, false)
        // referencing the image view from the item.xml file
        val btn_edit: Button = itemView.findViewById(R.id.btn_edit) as Button
        val btn_share: Button = itemView.findViewById(R.id.btn_share) as Button
        val imageView: ImageView = itemView.findViewById(R.id.imageViewMain) as ImageView
        val imageUrl: String? = objectList[position].imgUrl
        Glide.with(context)
            .load(imageUrl)
            .into(imageView)
        btn_edit.setOnClickListener {
            val intent = Intent(context, EditActivity::class.java)
            val args = Bundle()
            intent.putExtra(EditActivity.IMAGE_URI_EXTRA, imageUrl)
            Log.d(TAG, "instantiateItem() called"+imageUrl)
            context.startActivity(intent)
        }

        btn_share.setOnClickListener {
            //imageUrl is type of String of URI
            val imageUri = Uri.parse(imageUrl)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            shareIntent.type = "image/*"
            context.startActivity(Intent.createChooser(shareIntent, "Share image using"))
        }
        // Adding the View
        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}
