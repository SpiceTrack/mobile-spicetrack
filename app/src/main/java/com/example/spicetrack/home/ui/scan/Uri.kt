import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

fun createImageUri(context: Context): Uri? {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_${timeStamp}_"
    val storageDir: File = context.getExternalFilesDir(null) ?: return null
    val imageFile = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
    val imageUri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider", // Pastikan ini sesuai dengan yang ada di AndroidManifest.xml
        imageFile
    )
    return imageUri
}
