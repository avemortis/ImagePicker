package com.vtorushin.imagepick.ui.fragments.gallery

import androidx.lifecycle.ViewModel

class GalleryViewModel : ViewModel() {
    var files: ArrayList<String> = ArrayList()
    var photoFilePath: String? = null
}