package com.amrdeveloper.linkhub.util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.databinding.BottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

object LinkBottomSheetDialog {

    fun launch(
        activity: Activity,
        link: Link,
        folder: Folder? = null,
        onFolderClick: (Folder?) -> Unit = {}
    ) {
        val bottomSheetDialog = BottomSheetDialog(activity)
        val dialogBinding = BottomSheetDialogBinding.inflate(LayoutInflater.from(activity))
        bottomSheetDialog.setContentView(dialogBinding.root)

        folder?.let {
            dialogBinding.dialogFolderName.visibility = View.VISIBLE
            dialogBinding.dialogFolderName.text = folder.name
            dialogBinding.dialogFolderName.setCompoundDrawablesWithIntrinsicBounds(
                folder.folderColor.drawableId,
                0,
                0,
                0
            );
            dialogBinding.dialogFolderName.setOnClickListener {
                bottomSheetDialog.dismiss()
                onFolderClick(folder)
            }
        }

        dialogBinding.dialogOpenAction.setOnClickListener {
            try {
                openLinkIntent(activity, link.url)
            } catch (_: Exception) {
                // TODO: improve error message, and replace try catch with tag union errors
                activity.showSnackBar(R.string.message_link_invalid)
            }
            bottomSheetDialog.dismiss()
        }

        dialogBinding.dialogCopyAction.setOnClickListener {
            val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(link.title, link.url))
            bottomSheetDialog.dismiss()
            activity.showSnackBar(R.string.message_link_copy)
        }

        dialogBinding.dialogShareAction.setOnClickListener {
            shareTextIntent(activity, "${link.title}\n${link.url}")
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.dismissWithAnimation = true
        bottomSheetDialog.show()
    }
}