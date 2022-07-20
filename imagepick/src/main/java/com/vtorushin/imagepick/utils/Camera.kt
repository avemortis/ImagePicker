package com.vtorushin.imagepick.utils

import android.annotation.SuppressLint
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.io.IOException

@SuppressLint("QueryPermissionsNeeded")
fun Fragment.dispatchTakePictureIntentAndReturnFilePath(launcher: ActivityResultLauncher<Intent>) : String? {
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
        var photoFile: File? = null
        try {
            photoFile = createTempImageFile()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        if (photoFile != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(requireContext(), requireContext().packageName, photoFile))
            launcher.launch(takePictureIntent)
            return photoFile.absolutePath
        }
    }
    return null
}

fun Fragment.bindCameraFutureToLifecycle(cameraProvideFuture: ListenableFuture<ProcessCameraProvider>, preview: Preview) {
    cameraProvideFuture.addListener({
        val cameraProvider = cameraProvideFuture.get()
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this, cameraSelector, preview
            )
        } catch (e: Exception) {
            Log.d("Error", "Failed")
        }
    }, ContextCompat.getMainExecutor(requireContext()))
}