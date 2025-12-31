package com.amrdeveloper.linkhub.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.util.openLinkIntent
import com.amrdeveloper.linkhub.util.shareTextIntent


@Composable
fun LinkActionsBottomSheet(link: Link, onDialogDismiss: () -> Unit) {
    val actions = listOf(
        BottomSheetAction(name = "Open", R.drawable.ic_open_in_browser),
        BottomSheetAction(name = "Copy", R.drawable.ic_copy),
        BottomSheetAction(name = "Share", R.drawable.ic_share)
    )

    var selectedAction by remember { mutableStateOf<BottomSheetAction?>(value = null) }

    BottomSheetWithActions(
        actions,
        onActionSelected = { action ->
            selectedAction = action
        },
        onDialogDismiss = onDialogDismiss
    )

    if (selectedAction != null) {
        when (selectedAction?.name) {
            "Open" -> {
                val context = LocalContext.current
                try {
                    openLinkIntent(context = context, link = link.url)
                } catch (_: Exception) {

                }
            }
            "Copy" -> {
                val clipboardManager = LocalClipboard.current
                clipboardManager.nativeClipboard.text = AnnotatedString(link.url)
            }
            "Share" -> {
                val context = LocalContext.current
                shareTextIntent(context = context, text = "${link.title}\n${link.url}")
            }
        }
    }
}