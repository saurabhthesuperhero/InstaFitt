package com.developersmarket.instafitt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.developersmarket.instafitt.R
import com.developersmarket.instafitt.model.ItemEditAction


class ItemAdapter(private val itemList: List<ItemEditAction>, private val listener: OnItemClickListener) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    interface OnItemClickListener {
        fun onItemClick(item: ItemEditAction)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_edit_action_aspect, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
//        holder.view.findViewById<ImageView>(R.id.iv_action_image).setImageResource(item.imageResId)
        holder.view.findViewById<TextView>(R.id.tv_action).text = item.name
        holder.view.setOnClickListener {
            listener.onItemClick(item)
        }
    }

    override fun getItemCount(): Int = itemList.size
}
