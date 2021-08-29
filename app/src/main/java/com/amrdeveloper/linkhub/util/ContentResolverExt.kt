package com.amrdeveloper.linkhub.util

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import java.io.BufferedReader
import java.io.InputStreamReader

fun ContentResolver.getFileText(fileUri: Uri): String {
    val inputStream = openInputStream(fileUri)
    val stringBuilder = StringBuilder()
    val bufferReader = BufferedReader(InputStreamReader(inputStream))
    var line: String? = bufferReader.readLine()
    while (line != null) {
        stringBuilder.appendLine(line)
        line = bufferReader.readLine()
    }
    inputStream?.close()
    return stringBuilder.toString()
}

fun ContentResolver.getFileName(fileUri: Uri): String {
    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }
    return name
}