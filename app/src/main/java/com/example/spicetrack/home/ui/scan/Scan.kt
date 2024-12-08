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
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.spicetrack.DetailActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi tampilan
        previewView = findViewById(R.id.PreviewView)
        resultTextView = findViewById(R.id.resultTextView)
        uploadButton = findViewById(R.id.uploadButton)
        galleryButton = findViewById(R.id.galleryButton)
        cameraXButton = findViewById(R.id.cameraXButton)

        // Menyiapkan kamera
        cameraExecutor = Executors.newSingleThreadExecutor()
        startCamera()

        // Tombol untuk memulai pemindaian
        cameraXButton.setOnClickListener {
            captureImage()
        }

        // Tombol untuk mengupload gambar
        uploadButton.setOnClickListener {
            // Kode untuk mengupload gambar
        }

        // Tombol untuk membuka galeri
        galleryButton.setOnClickListener {
            openGallery()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Menyiapkan use case Preview
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(previewView.surfaceProvider)


            imageCapture = ImageCapture.Builder().build()

            // Menyiapkan CameraSelector untuk kamera belakang
            val cameraSelector = androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA

            // Menghubungkan use case dengan kamera
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun captureImage() {
        // Membuat file untuk menyimpan gambar
        val file = File(externalMediaDirs.first(), "scan_${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        // Mengambil gambar dan menyimpannya ke file
        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // Setelah gambar disimpan, kirimkan ke API
                    sendImageToApi(file)
                }

                override fun onError(exception: ImageCaptureException) {
                    // Menangani error jika pengambilan gambar gagal
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
        val imagePart = MultipartBody.Part.createFormData(
            "image", file.name, file.asRequestBody("image/jpeg".toMediaType())
        )

        // Mengirimkan permintaan untuk mengupload gambar
        apiService.uploadImage(imagePart).enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(call: Call<FileUploadResponse>, response: Response<FileUploadResponse>) {
                if (response.isSuccessful) {
                    val fileUploadResponse = response.body()
                    if (fileUploadResponse != null) {
                        // Menampilkan hasil prediksi
                        resultTextView.text = fileUploadResponse.prediction
                        // Menavigasi ke DetailActivity dengan hasil prediksi
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

    private fun showToast(message: String) {
        // Menampilkan pesan toast
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun openGallery() {
        // Membuka galeri untuk memilih gambar
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 1
    }
}
