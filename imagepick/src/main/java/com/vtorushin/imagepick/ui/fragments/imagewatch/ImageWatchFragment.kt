package com.vtorushin.imagepick.ui.fragments.imagewatch

import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.vtorushin.imagepick.databinding.FragmentImageWatchBinding
import com.vtorushin.imagepick.utils.getCameraAndStoragePermissionLauncher
import com.vtorushin.imagepick.utils.launchCameraAndStoragePermissionLauncher
import java.lang.ClassCastException

class ImageWatchFragment : Fragment() {
    private lateinit var binding: FragmentImageWatchBinding
    private lateinit var viewModel: ImageWatchViewModel
    private val launcher = getCameraAndStoragePermissionLauncher(onGranted = {startCamera()}, onNotGranted = {})

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageWatchBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ImageWatchViewModel::class.java]
        binding.imageWatchImage.setImageURI(viewModel.currentUri)
        setClickListeners()
        return binding.root
    }

    private fun setClickListeners() {
        binding.imageWatchPickButton.setOnClickListener {
            launchCameraAndStoragePermissionLauncher(launcher)
        }
    }

    private fun startCamera() {
        setFragmentResultListener(IMAGE_RESULT_TAG) { _, bundle ->
            try {
                val uri : Uri? = bundle.getParcelable(IMAGE_BUNDLE_TAG)
                viewModel.currentUri = uri
                binding.imageWatchImage.setImageURI(uri)
            } catch (e : ClassCastException) {
            }

        }
        val action = ImageWatchFragmentDirections.actionImageWatchFragmentToGalleryFragment()
        findNavController().navigate(action)
    }

    companion object {
        const val IMAGE_RESULT_TAG = "ImageResultTag"
        const val IMAGE_BUNDLE_TAG = "ImageBundleTag"
    }
}