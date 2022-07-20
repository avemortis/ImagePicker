package com.vtorushin.imagepick.utils

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import java.io.File
import java.io.IOException

@SuppressLint("Recycle")
fun Fragment.getMediaFilesPaths() = run {
    val imageList: ArrayList<String> = ArrayList()
    val columns = arrayOf(
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media._ID
    )
    val imageCursor: Cursor = requireActivity().contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
        null, ""
    )!!
    for (i in 0 until imageCursor.count) {
        imageCursor.moveToPosition(i)
        val dataColumnIndex =
            imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)
        imageList.add(imageCursor.getString(dataColumnIndex))
    }
    imageList.reverse()
    imageList
}

@Throws(IOException::class)
fun createTempImageFile(): File? {
    val storageDir = Environment.getExternalStorageDirectory()
    return File.createTempFile(
        "example",
        ".jpg",
        storageDir
    )
}