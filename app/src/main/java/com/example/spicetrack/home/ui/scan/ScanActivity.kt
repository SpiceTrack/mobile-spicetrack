package com.example.spicetrack.home.ui.scan

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.spicetrack.R
import com.example.spicetrack.home.ui.network.ApiResponse
import com.example.spicetrack.home.ui.network.ApiService
import com.google.android.material.progressindicator.LinearProgressIndicator
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import com.example.spicetrack.home.data.HerpsResponseItem
import com.example.spicetrack.home.ui.detail.DetailActivity
import android.util.Log

class ScanActivity : AppCompatActivity() {

    private lateinit var progressIndicator: LinearProgressIndicator
    private lateinit var imageView: ImageView
    private lateinit var resultTextView: TextView
    private lateinit var closeButton: ImageButton
    private lateinit var uploadButton: Button
    private lateinit var galleryButton: ImageButton
    private lateinit var cameraButton: Button

    private val apiUrl = "https://backend-spicetrack-1036509671472.asia-southeast2.run.app"
    private var selectedImageBitmap: Bitmap? = null

    private val CAMERA_PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        progressIndicator = findViewById(R.id.progressIndicator)
        imageView = findViewById(R.id.imageview)
        resultTextView = findViewById(R.id.resultTextView)
        closeButton = findViewById(R.id.btnClose)
        uploadButton = findViewById(R.id.uploadButton)
        galleryButton = findViewById(R.id.galleryButton)
        cameraButton = findViewById(R.id.cameraXButton)

        // Close button functionality
        closeButton.setOnClickListener { finish() }

        // Gallery button functionality
        galleryButton.setOnClickListener { openGallery() }

        // Camera button functionality
        cameraButton.setOnClickListener { checkCameraPermissionAndOpen() }

        // Upload button functionality
        uploadButton.setOnClickListener { uploadImageToApi() }
    }

    private val openGalleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            imageView.setImageURI(imageUri)
            imageUri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                selectedImageBitmap = bitmap
            }
        }
    }

    private val openCameraResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            selectedImageBitmap?.let {
                imageView.setImageBitmap(it)
            }
        } else {
            Toast.makeText(this, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to check for camera permissions before opening the camera
    private fun checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Handle camera permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, "Izin kamera diperlukan untuk mengambil gambar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Open the camera to take a picture
    private fun openCamera() {
        val photoUri = createImageUri()  // Create URI to save the photo
        photoUri?.let {
            openCameraResult.launch(it)
        }
    }

    // Upload image function
    private fun uploadImageToApi() {
        val bitmap = selectedImageBitmap
        if (bitmap != null) {
            // Show progress
            progressIndicator.visibility = LinearProgressIndicator.VISIBLE

            val byteArray = bitmapToByteArray(bitmap)
//            val encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT)

            // Create a request body for the image using MediaType.toMediaTypeOrNull()
            val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), byteArray)
            val body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile)

            val retrofit = Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)
            val call = apiService.uploadImage(body)
            call.enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    progressIndicator.visibility = LinearProgressIndicator.GONE
                    if (response.isSuccessful) {
                        Log.d("DetailActivity", "API Response Success")
                        val apiResponse = response.body()
                        apiResponse?.let {
                            Log.d("DetailActivity", "$apiResponse")
                            val classification = it.classification
                            resultTextView.text = "Classification: $classification"
                            // Pass data to DetailActivity

                            fetchHerbData(apiResponse.url)

//                            val intent = Intent(this@ScanActivity, Dactivity::class.java)
//                            intent.putExtra("classification", classification)
//                            intent.putExtra("url", it.url)
//                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(this@ScanActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    progressIndicator.visibility = LinearProgressIndicator.GONE
                    Toast.makeText(this@ScanActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    // Create URI for saving the image
    private fun createImageUri(): Uri? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir: File = getExternalFilesDir(null) ?: return null
        val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
        return FileProvider.getUriForFile(
            this,
            "${packageName}.provider",  // Ensure this matches the provider authorities in AndroidManifest.xml
            imageFile
        )
    }

    // Function to open gallery and choose image
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        openGalleryResult.launch(intent)
    }

    private fun fetchHerbData(url: String): HerpsResponseItem? {
        var data: HerpsResponseItem?
        data = null
        val retrofit = Retrofit.Builder()
            .baseUrl(apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getHerb(url)
        call.enqueue(object : Callback<HerpsResponseItem> {
            override fun onResponse(call: Call<HerpsResponseItem>, response: Response<HerpsResponseItem>) {
                if (response.isSuccessful) {
                    val herbsData = response.body()
                    val intent = Intent(this@ScanActivity, DetailActivity::class.java)
                    intent.putExtra("HERPS_ID", -1) // Default -1 jika tidak ada
                    intent.putExtra("HERPS_TITLE", herbsData?.title) // Default null jika tidak ada
                    intent.putExtra("HERPS_SUBTITLE", herbsData?.subtitle)
                    intent.putExtra("HERPS_IMAGE", herbsData?.imageUrl)
                    intent.putExtra("HERPS_CONTENT", herbsData?.content)
                    intent.putExtra("HERPS_TAGS", herbsData?.tags)
                    intent.putExtra("HERPS_ORIGIN", herbsData?.origin)
                    startActivity(intent)
                }
            }
            override fun onFailure(call: Call<HerpsResponseItem>, t: Throwable) {
                data = null
            }
        })
        return data

    }
}
