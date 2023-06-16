package com.pc609.potholesense.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pc609.potholesense.R
import com.pc609.potholesense.ui.SharedPreferencesHelper
import com.pc609.potholesense.ui.viewmodel.PredictViewModel
import com.pc609.potholesense.ui.viewmodel.ViewModelFactory

class PredictActivity : AppCompatActivity() {

    private lateinit var predictViewModel: PredictViewModel
    private lateinit var imagePreview: ImageView
    private lateinit var resultText: TextView
    private lateinit var loadingView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predict)
        supportActionBar?.setTitle(R.string.issue_activity_predict)

        imagePreview = findViewById(R.id.img_preview)
        resultText = findViewById(R.id.tv_resultText)
        val selectButton = findViewById<Button>(R.id.btn_gallery)
        val cameraButton = findViewById<Button>(R.id.btn_camera)
        val predictButton = findViewById<Button>(R.id.predict)
        loadingView = findViewById(R.id.progress_bar)

        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        val uploadViewModel =
            ViewModelProvider(this, ViewModelFactory(this, sharedPreferencesHelper)).get(
                PredictViewModel::class.java
            )
        uploadViewModel.resultText.observe(this, Observer { result ->
            resultText.text = result
            hideLoading()
        })

        uploadViewModel.predictionResult.observe(this, Observer { result ->
            val resultDescription = uploadViewModel.getResultDescription(result)
            if (resultDescription != null) {
                showToast("Hasil prediksi: $resultDescription")
                Log.d(TAG, "Hasil prediksi: $resultDescription")
            } else {
                showToast("Hasil prediksi tidak valid: $result")
                Log.e(TAG, "Hasil prediksi tidak valid: $result")
            }
        })

        // Mengamati pesan error
        uploadViewModel.errorMessage.observe(this, Observer { errorMessage ->
            showToast(errorMessage)
        })

        selectButton.setOnClickListener {
            if (hasStoragePermission()) {
                openGallery()
            } else {
                requestStoragePermission()
            }
        }

        cameraButton.setOnClickListener {
            if (hasCameraPermission()) {
                openCamera()
            } else {
                requestCameraPermission()
            }
        }

        predictButton.setOnClickListener {
            val drawable = imagePreview.drawable as? BitmapDrawable
            val bitmap = drawable?.bitmap

            if (bitmap != null) {
                showLoading()
                uploadViewModel.processImage(bitmap)
            } else {
                showToast("Pilih gambar terlebih dahulu")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading() {
        loadingView.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingView.visibility = View.GONE
    }

    private fun hasStoragePermission(): Boolean {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        val result = ContextCompat.checkSelfPermission(this, permission)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_PERMISSION_CODE)
    }

    private fun hasCameraPermission(): Boolean {
        val permission = Manifest.permission.CAMERA
        val result = ContextCompat.checkSelfPermission(this, permission)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        val permission = Manifest.permission.CAMERA
        ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_PERMISSION_CODE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAMERA)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            if (permissions[0] == Manifest.permission.READ_EXTERNAL_STORAGE) {
                openGallery()
            } else if (permissions[0] == Manifest.permission.CAMERA) {
                openCamera()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_GALLERY -> {
                    val imageUri = data?.data
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                    imagePreview.setImageBitmap(bitmap)
                }
                REQUEST_IMAGE_CAMERA -> {
                    val bitmap = data?.extras?.get("data") as? Bitmap
                    imagePreview.setImageBitmap(bitmap)
                }
            }
        }
    }

    companion object {
        const val TAG = "UploadActivity"
        private const val REQUEST_PERMISSION_CODE = 1
        private const val REQUEST_IMAGE_GALLERY = 2
        private const val REQUEST_IMAGE_CAMERA = 3
    }
}
