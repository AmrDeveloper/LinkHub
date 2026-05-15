package com.amrdeveloper.linkhub.util

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException

fun generateQrCodeBitmap(
    text: String,
    width: Int = 512,
    height: Int = 512,
    dataColor: Int = Color.rgb(0x03, 0x9B, 0xE5),
    emptyColor: Int = Color.WHITE
): Bitmap? {
    return try {
        // Encode the text into a 2D BitMatrix
        val bitMatrix = MultiFormatWriter().encode(
            text,
            BarcodeFormat.QR_CODE,
            width,
            height
        )

        val bitmap = createBitmap(width, height, config = Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap[x, y] = if (bitMatrix.get(x, y)) dataColor else emptyColor
            }
        }
        bitmap
    } catch (_: WriterException) {
        null
    }
}
