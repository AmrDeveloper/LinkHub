package com.amrdeveloper.linkhub.util

import android.content.*
import android.net.Uri
import android.view.LayoutInflater
import android.widget.Toast
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.databinding.BottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

object LinkBottomSheetDialog {

    fun launch(context: Context, link : Link) {
        val bottomSheetDialog = BottomSheetDialog(context)
        val dialogBinding = BottomSheetDialogBinding.inflate(LayoutInflater.from(context))
        bottomSheetDialog.setContentView(dialogBinding.root)

        dialogBinding.dialogOpenAction.setOnClickListener {
            val openIntent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(link.url))
            try {
                context.startActivity(openIntent)
            } catch (e : ActivityNotFoundException) {
                Toast.makeText(context, R.string.link_invalid, Toast.LENGTH_SHORT).show()
            }
            bottomSheetDialog.dismiss()
        }

        dialogBinding.dialogCopyAction.setOnClickListener {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(link.title, link.url))
            bottomSheetDialog.dismiss()
            Toast.makeText(context, R.string.link_copy, Toast.LENGTH_SHORT).show()
        }

        dialogBinding.dialogShareAction.setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.putExtra(Intent.EXTRA_TEXT, link.title)
            sendIntent.type = "text/plain"
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.dismissWithAnimation = true
        bottomSheetDialog.show()
    }
}