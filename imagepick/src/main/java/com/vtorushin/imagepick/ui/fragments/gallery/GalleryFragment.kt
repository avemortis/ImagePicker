package com.vtorushin.imagepick.ui.fragments.gallery

import android.app.Activity
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.common.util.concurrent.ListenableFuture
import com.vtorushin.imagepick.databinding.FragmentGalleryBinding
import com.vtorushin.imagepick.ui.fragments.imagewatch.ImageWatchFragment.Companion.IMAGE_BUNDLE_TAG
import com.vtorushin.imagepick.ui.fragments.imagewatch.ImageWatchFragment.Companion.IMAGE_RESULT_TAG
import com.vtorushin.imagepick.ui.recyclerview.*
import com.vtorushin.imagepick.utils.getMediaFilesPaths
import com.vtorushin.imagepick.utils.bindCameraFutureToLifecycle
import com.vtorushin.imagepick.utils.dispatchTakePictureIntentAndReturnFilePath
import java.io.File

class GalleryFragment : Fragment() {
    private lateinit var viewModel: GalleryViewModel
    private lateinit var bind: FragmentGalleryBinding
    private lateinit var cameraProvideFuture: ListenableFuture<ProcessCameraProvider>

    private val preview = Preview.Builder().build()

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && viewModel.photoFilePath != null) {
            galleryAddPic(viewModel.photoFilePath!!)
            findNavController().navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cameraProvideFuture = ProcessCameraProvider.getInstance(requireContext())
        viewModel = ViewModelProvider(this)[GalleryViewModel::class.java]
        bind = FragmentGalleryBinding.inflate(layoutInflater, container, false)
        viewModel.files = getMediaFilesPaths()
        prepareRecyclerView()
        return bind.root
    }

    private fun prepareRecyclerView() {
        bind.galleryFragmentRecyclerview.adapter =
            GalleryRecyclerViewAdapter(
                size = viewModel.files.size + ConstGalleryItems::class.sealedSubclasses.size,
                onBing = { holder, position -> bindHolder(holder, position) },
                onConstHolderCreate = { holder -> constHoldersCreateHandle(holder) },
                onClick = { holder, position -> holdersClickHandle(holder, position) })
    }

    private fun bindHolder(holder: GalleryItems, position: Int) {
        when (holder) {
            is GalleryViewHolder -> {
                Glide
                    .with(this)
                    .load(viewModel.files[position - ConstGalleryItems::class.sealedSubclasses.size])
                    .centerCrop()
                    .into(holder.image)
            }
            is ThumbnailViewHolder -> {
                preview.setSurfaceProvider(holder.thumbnail.surfaceProvider)
            }
        }
    }

    private fun constHoldersCreateHandle(holder: ConstGalleryItems) {
        when (holder) {
            is ThumbnailViewHolder -> bindCameraFutureToLifecycle(
                cameraProvideFuture,
                preview
            )
        }
    }

    private fun holdersClickHandle(holder: GalleryItems, position: Int) {
        when (position >= ConstGalleryItems::class.sealedSubclasses.size) {
            true -> {
                onGalleryHolderClick(position - ConstGalleryItems::class.sealedSubclasses.size)
            }
            false -> {
                onConstHolderClick(holder as ConstGalleryItems)
            }
        }
    }

    private fun onGalleryHolderClick(position: Int) {
        val uri = Uri.fromFile(File(viewModel.files[position]))
        setFragmentResult(IMAGE_RESULT_TAG, bundleOf(IMAGE_BUNDLE_TAG to uri))
        findNavController().navigateUp()
    }

    private fun onConstHolderClick(holder: ConstGalleryItems) {
        when (holder) {
            is ThumbnailViewHolder -> {
                viewModel.photoFilePath = dispatchTakePictureIntentAndReturnFilePath(launcher)
            }
        }
    }

    private fun galleryAddPic(path: String) {
        val f = File(path)
        MediaScannerConnection.scanFile(requireContext(), arrayOf(f.toString()), null, null)
        val contentUri = Uri.fromFile(f)
        setFragmentResult(IMAGE_RESULT_TAG, bundleOf(IMAGE_BUNDLE_TAG to contentUri))
    }
}