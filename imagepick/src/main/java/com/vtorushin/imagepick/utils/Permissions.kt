package com.vtorushin.imagepick.utils

import android.Manifest
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

fun Fragment.getCameraAndStoragePermissionLauncher(onGranted: () -> Unit, onNotGranted: () -> Unit) = run {
    val resultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.CAMERA] == true && permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true && permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                onGranted()
            } else {
                onNotGranted()
            }
        }
    resultLauncher
}

fun launchCameraAndStoragePermissionLauncher(launcher: ActivityResultLauncher<Array<String>>) {
    launcher.launch(
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )
}