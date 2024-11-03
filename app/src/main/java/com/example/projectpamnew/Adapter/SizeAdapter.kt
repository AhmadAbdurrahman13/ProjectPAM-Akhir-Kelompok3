package com.example.projectpamnew.Adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectpamnew.Model.BrandModel
import com.example.projectpamnew.R
import com.example.projectpamnew.databinding.ViewholderBrandBinding
import com.example.projectpamnew.databinding.ViewholderColorBinding
import com.example.projectpamnew.databinding.ViewholderSizeBinding

class SizeAdapter(private val items: MutableList<String>) :
    RecyclerView.Adapter<SizeAdapter.ViewHolder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1
    private lateinit var context: Context

    class ViewHolder(val binding: ViewholderSizeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderSizeBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.sizeText.text=items[position]

        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
        }

        if (selectedPosition == position) {
            holder.binding.sizeLayout.setBackgroundResource(R.drawable.sec_bg_selected)
            holder.binding.sizeText.setTextColor(context.resources.getColor(R.color.maroon))
        } else {
            holder.binding.sizeLayout.setBackgroundResource(R.drawable.sec_bg)
            holder.binding.sizeText.setTextColor(context.resources.getColor(R.color.black))
        }
    }

    override fun getItemCount(): Int = items.size
}
