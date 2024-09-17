package com.amrdeveloper.linkhub.data

import androidx.annotation.Keep

@Keep
enum class ImportExportFileType(
    val mimeType: String,
    val extension: String,
    val fileTypeName: String
) {
    JSON("application/json", ".json", "Json"),
    HTML("text/html", ".html", "HTML")
}