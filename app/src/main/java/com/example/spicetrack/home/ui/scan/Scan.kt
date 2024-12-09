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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi tampilan UI (Preview, TextView, Button, dll)
        previewView = findViewById(R.id.PreviewView)
        resultTextView = findViewById(R.id.resultTextView)
        uploadButton = findViewById(R.id.uploadButton)
        galleryButton = findViewById(R.id.galleryButton)
        cameraXButton = findViewById(R.id.cameraXButton)

        // Menyiapkan kamera untuk digunakan
        cameraExecutor = Executors.newSingleThreadExecutor()
        startCamera()

        // Mengatur aksi ketika tombol untuk memulai pemindaian ditekan
        cameraXButton.setOnClickListener {
            captureImage()
        }

        // Tombol untuk mengupload gambar yang diambil
        uploadButton.setOnClickListener {
            // Kode untuk mengupload gambar ke API
        }

        // Tombol untuk membuka galeri dan memilih gambar dari galeri
        galleryButton.setOnClickListener {
            openGallery()
        }
    }

    private fun startCamera() {
        // Memulai kamera menggunakan CameraX API
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Menyiapkan use case Preview untuk menampilkan pratinjau kamera
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.PreviewView.surfaceProvider)

            // Membuat instance ImageCapture untuk menangkap gambar
            imageCapture = ImageCapture.Builder().build()

            // Menggunakan kamera belakang sebagai kamera yang aktif
            val cameraSelector = androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA

            // Menghubungkan use case Preview dan ImageCapture dengan kamera
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun captureImage() {
        // Membuat file untuk menyimpan gambar yang diambil
        val file = File(externalMediaDirs.first(), "scan_${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        // Mengambil gambar dan menyimpannya ke file yang sudah ditentukan
        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // Setelah gambar berhasil disimpan, kirimkan gambar tersebut ke API
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
        // Membuat Retrofit instance untuk mengirim permintaan API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://backend-spicetrack-1036509671472.asia-southeast2.run.app/classification/infer/")  // URL API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // Membuat MultipartBody.Part untuk mengirim gambar sebagai form data
        val imagePart = MultipartBody.Part.createFormData(
            "image", file.name, file.asRequestBody("image/jpeg".toMediaType())
        )

        // Mengirim gambar ke server API untuk diproses
        apiService.uploadImage(imagePart).enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(call: Call<FileUploadResponse>, response: Response<FileUploadResponse>) {
                // Menangani respons dari server setelah gambar diupload
                if (response.isSuccessful) {
                    val fileUploadResponse = response.body()
                    if (fileUploadResponse != null) {
                        // Menampilkan hasil prediksi yang diterima dari API
                        resultTextView.text = fileUploadResponse.prediction
                        // Menavigasi ke DetailActivity untuk menampilkan hasil prediksi lebih lanjut
                        val intent = Intent(this@ScanActivity, DetailActivity::class.java)
                        intent.putExtra("PREDICTION", fileUploadResponse.prediction)
                        startActivity(intent)
                    }
                } else {
                    // Menampilkan pesan kesalahan jika API gagal memberikan prediksi
                    showToast("Gagal mendapatkan prediksi: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                // Menangani kesalahan saat menghubungi API
                showToast("Error saat mengupload gambar: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        // Menampilkan pesan toast untuk memberitahukan status atau kesalahan
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun openGallery() {
        // Membuka galeri untuk memilih gambar yang sudah ada
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 1  // Kode permintaan untuk memilih gambar dari galeri
    }
}

