package com.example.spicetrack.home.ui.scan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.spicetrack.home.ui.detail.DetailActivity
import com.example.spicetrack.R
import com.example.spicetrack.databinding.ActivityScanBinding
import com.example.spicetrack.home.ui.network.ApiService
import com.example.spicetrack.home.ui.network.FileUploadResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanActivity : ComponentActivity() {

    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var binding: ActivityScanBinding

    private lateinit var previewView: ImageView
    private lateinit var resultTextView: TextView
    private lateinit var uploadButton: Button
    private lateinit var galleryButton: ImageButton
    private lateinit var cameraXButton: ImageButton
    private var cameraSelector = androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        previewView = findViewById(R.id.PreviewView)
        resultTextView = findViewById(R.id.resultTextView)
        uploadButton = findViewById(R.id.uploadButton)
        galleryButton = findViewById(R.id.galleryButton)
        cameraXButton = findViewById(R.id.cameraXButton)

        val switchCameraButton = findViewById<ImageButton>(R.id.switchCamera)

        cameraExecutor = Executors.newSingleThreadExecutor()
        startCamera()

        cameraXButton.setOnClickListener { captureImage() }
        uploadButton.setOnClickListener { }
        galleryButton.setOnClickListener { openGallery() }
        switchCameraButton.setOnClickListener { toggleCamera() }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.PreviewView.surfaceProvider)

            imageCapture = ImageCapture.Builder().build()

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun toggleCamera() {
        cameraSelector = if (cameraSelector == androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA) {
            androidx.camera.core.CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
        }
        startCamera()
    }

    private fun captureImage() {
        val file = File(externalMediaDirs.first(), "scan_${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    sendImageToApi(file)
                }

                override fun onError(exception: ImageCaptureException) {
                    showToast("Pengambilan gambar gagal: ${exception.message}")
                }
            }
        )
    }

    private fun sendImageToApi(file: File) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://backend-spicetrack-1036509671472.asia-southeast2.run.app/classification/infer/")  // URL API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // Creating the image request body to upload as multipart form data
        val imagePart = MultipartBody.Part.createFormData(
            "image", file.name, file.asRequestBody("image/jpeg".toMediaType())
        )

        // Make API request to upload the image
        apiService.uploadImage(imagePart).enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(call: Call<FileUploadResponse>, response: Response<FileUploadResponse>) {
                if (response.isSuccessful) {
                    val fileUploadResponse = response.body()
                    if (fileUploadResponse != null) {
                        resultTextView.text = fileUploadResponse.prediction
                        val intent = Intent(this@ScanActivity, DetailActivity::class.java)
                        intent.putExtra("PREDICTION", fileUploadResponse.prediction)
                        startActivity(intent)
                    }
                } else {
                    showToast("Gagal mendapatkan prediksi: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                showToast("Error saat mengupload gambar: ${t.message}")
            }
        })
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 1
    }
}
