package com.developersmarket.instafitt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.developersmarket.instafitt.R


class ImageAdapter(
    private val mImageFilePaths: List<String>,
    private val onClickListener: OnClickListener
) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(view, onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filePath = mImageFilePaths[position]
        Glide.with(holder.itemView.context)
            .load(filePath)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return mImageFilePaths.size
    }

    class ViewHolder(itemView: View, onClickListener: OnClickListener) :
        RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView

        init {
            imageView = itemView.findViewById(R.id.iv_image)

            itemView.setOnClickListener {
                onClickListener.onRowClick(adapterPosition)
            }
        }
    }

    interface OnClickListener {
        fun onRowClick(position: Int)
    }
}
