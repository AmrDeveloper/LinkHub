package com.amrdeveloper.linkhub.ui.components

import androidx.annotation.Keep
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.util.openLinkIntent
import com.amrdeveloper.linkhub.util.shareTextIntent

@Keep
private enum class LinkActions {
    Open,
    Edit,
    Copy,
    Share
}

@Composable
fun LinkActionsBottomSheet(
    link: Link,
    navController: NavController,
    onDialogDismiss: () -> Unit
) {
    val actions = listOf(
        BottomSheetAction(name = LinkActions.Open.name, R.drawable.ic_open_in_browser),
        BottomSheetAction(name = LinkActions.Edit.name, R.drawable.ic_pin_edit),
        BottomSheetAction(name = LinkActions.Copy.name, R.drawable.ic_copy),
        BottomSheetAction(name = LinkActions.Share.name, R.drawable.ic_share)
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
        val linkAction = LinkActions.valueOf(value = selectedAction!!.name)
        when (linkAction) {
            LinkActions.Open -> {
                val context = LocalContext.current
                try {
                    openLinkIntent(context = context, link = link.url)
                } catch (_: Exception) {

                }
            }

            LinkActions.Edit -> {
                val bundle = bundleOf("link" to link)
                navController.navigate(
                    R.id.linkFragment,
                    bundle
                )
            }

            LinkActions.Copy -> {
                val clipboardManager = LocalClipboard.current
                clipboardManager.nativeClipboard.text = AnnotatedString(link.url)
            }

            LinkActions.Share -> {
                val context = LocalContext.current
                shareTextIntent(context = context, text = "${link.title}\n${link.url}")
            }
        }
    }
}