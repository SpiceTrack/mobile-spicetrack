package com.dicoding.foodscanner

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spicetrack.R
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ScanFoodActivity : AppCompatActivity() {

    private lateinit var tflite: Interpreter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        // Load TensorFlow Lite model
        val model = assets.open("cancer_classification.tflite")
        val buffer = model.readBytes().let {
            ByteBuffer.allocateDirect(it.size).apply {
                order(ByteOrder.nativeOrder())
                put(it)
            }
        }
        tflite = Interpreter(buffer)

        findViewById<Button>(R.id.scanButton).setOnClickListener {
            val imageBitmap: Bitmap = captureImageFromCamera()
            val result = classifyImage(imageBitmap)
            Toast.makeText(this, "Prediction: $result", Toast.LENGTH_SHORT).show()
        }
    }

    private fun classifyImage(bitmap: Bitmap): String {
        val resizedImage = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
        val inputBuffer = convertBitmapToByteBuffer(resizedImage)
        val outputBuffer = FloatArray(1)

        // Run inference
        tflite.run(inputBuffer, outputBuffer)

        // Interpret results
        return if (outputBuffer[0] > 0.5) "Cancer Detected" else "No Cancer"
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(224 * 224)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (pixel in intValues) {
            byteBuffer.putFloat(((pixel shr 16) and 0xFF) / 255.0f)
            byteBuffer.putFloat(((pixel shr 8) and 0xFF) / 255.0f)
            byteBuffer.putFloat((pixel and 0xFF) / 255.0f)
        }
        return byteBuffer
    }

    private fun captureImageFromCamera(): Bitmap {
        // Tambahkan logika untuk menangkap gambar dari kamera
        // Placeholder untuk implementasi
        return Bitmap.createBitmap(224, 224, Bitmap.Config.ARGB_8888)
    }

    override fun onDestroy() {
        tflite.close()
        super.onDestroy()
    }
}
