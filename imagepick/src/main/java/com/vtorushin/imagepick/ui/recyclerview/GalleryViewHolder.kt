package com.vtorushin.imagepick.ui.recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.vtorushin.imagepick.databinding.ViewHolderCameraBinding
import com.vtorushin.imagepick.databinding.ViewHolderGalleryBinding

sealed class GalleryItems(view: ViewBinding) : RecyclerView.ViewHolder(view.root) {
    companion object {
        const val VIEW_TYPE = 1
    }
}

sealed class ConstGalleryItems(view: ViewBinding) : GalleryItems(view) {
    companion object {
        const val VIEW_TYPE = 2
    }
}

class GalleryViewHolder(bind: ViewHolderGalleryBinding) : GalleryItems(bind) {
    val image = bind.viewHolderGalleryImageView
}

class ThumbnailViewHolder(bind: ViewHolderCameraBinding) : ConstGalleryItems(bind) {
    val thumbnail = bind.viewHolderCameraThumbnail
}