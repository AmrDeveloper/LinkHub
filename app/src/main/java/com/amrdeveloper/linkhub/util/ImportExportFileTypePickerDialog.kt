package com.amrdeveloper.linkhub.util

import android.app.AlertDialog
import android.content.Context
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.ImportExportFileType

object ImportExportFileTypePickerDialog {

    fun launch(context: Context, onFileTypeSelected: (ImportExportFileType)->Unit) {
        val fileTypes = ImportExportFileType.entries.map { it.fileTypeName }
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.import_export_choose_file_type))
        builder.setItems(fileTypes.toTypedArray()) { dialog, which ->
            val selectedFileType = ImportExportFileType.entries[which]
            onFileTypeSelected(selectedFileType)
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}