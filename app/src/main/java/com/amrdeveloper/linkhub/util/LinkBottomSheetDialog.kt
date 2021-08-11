package com.amrdeveloper.linkhub.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.databinding.BottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

object LinkBottomSheetDialog {

    fun launch(activity: Activity, link : Link) {
        val bottomSheetDialog = BottomSheetDialog(activity)
        val dialogBinding = BottomSheetDialogBinding.inflate(LayoutInflater.from(activity))
        bottomSheetDialog.setContentView(dialogBinding.root)

        dialogBinding.dialogOpenAction.setOnClickListener {
            try {
                openLinkIntent(activity, link.url)
            } catch (e : ActivityNotFoundException) {
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
            shareTextIntent(activity, link.title)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.dismissWithAnimation = true
        bottomSheetDialog.show()
    }
}