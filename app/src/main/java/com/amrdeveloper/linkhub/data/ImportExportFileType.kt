package com.amrdeveloper.linkhub.data

enum class ImportExportFileType(val mimeType: String, val extension: String) {

    JSON( "application/json",".json"),
    HTML("text/html", ".html")
}