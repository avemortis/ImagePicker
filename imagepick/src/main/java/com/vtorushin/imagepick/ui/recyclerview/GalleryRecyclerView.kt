package com.vtorushin.imagepick.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vtorushin.imagepick.databinding.ViewHolderCameraBinding
import com.vtorushin.imagepick.databinding.ViewHolderGalleryBinding

class GalleryRecyclerViewAdapter(
    var size: Int,
    val onClick: (holder: GalleryItems, position: Int) -> Unit,
    val onBing: (holder: GalleryItems, position: Int) -> Unit,
    val onConstHolderCreate: (holder: ConstGalleryItems) -> Unit
) : RecyclerView.Adapter<GalleryItems>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryItems {
        return when (viewType) {
            GalleryItems.VIEW_TYPE -> {
                val bind = ViewHolderGalleryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                GalleryViewHolder(bind)
            }

            else -> {
                val bind = ViewHolderCameraBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                val holder = ThumbnailViewHolder(bind)
                onConstHolderCreate(holder)
                return holder
            }
        }
    }

    override fun getItemViewType(position: Int) =
        when (position >= ConstGalleryItems::class.sealedSubclasses.size) {
            true -> GalleryItems.VIEW_TYPE
            false -> ConstGalleryItems.VIEW_TYPE
        }

    override fun onBindViewHolder(holder: GalleryItems, position: Int) {
        holder.itemView.setOnClickListener { onClick(holder, position) }
        onBing(holder, position)
    }

    override fun getItemCount() = size
}