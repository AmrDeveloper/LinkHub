package com.amrdeveloper.linkhub.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.os.bundleOf
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.ui.MainActivity
import com.google.gson.Gson

// Static shortcuts actions
const val ACTION_CREATE_LINK = "com.amrdeveloper.linkhub.action.create_link"
const val ACTION_CREATE_FOLDER = "com.amrdeveloper.linkhub.action.create_folder"

// Dynamic shortcuts actions
const val ACTION_OPEN_LINK = "com.amrdeveloper.linkhub.action.open_link"
const val ACTION_OPEN_FOLDER = "com.amrdeveloper.linkhub.action.open_folder"

fun createLinkDynamicPinnedShortcut(context: Context, link: Link): Boolean {
    return createDynamicPinnedShortcut(
        context = context,
        shortLabel = link.title,
        longLabel = link.subtitle.ifEmpty { link.title },
        iconId = R.drawable.ic_link,
        shortcutId = link.hashCode().toString(),
        shortcutAction = ACTION_OPEN_LINK,
        populateIntentWithExtras = { intent ->
            val bundle = bundleOf(
                "link_id" to link.id.toString(),
                "link_url" to link.url
            )
            intent.putExtras(bundle)
        },
    )
}

fun createFolderDynamicPinnedShortcut(context: Context, folder: Folder): Boolean {
    return createDynamicPinnedShortcut(
        context = context,
        shortLabel = folder.name,
        longLabel = folder.name,
        iconId = R.drawable.ic_link,
        shortcutId = folder.hashCode().toString(),
        shortcutAction = ACTION_OPEN_FOLDER,
        populateIntentWithExtras = { intent ->
            val folderJson = Gson().toJson(folder)
            intent.putExtra("folder_json", folderJson)
        },
    )
}

private fun createDynamicPinnedShortcut(
    context: Context,
    shortLabel: String,
    longLabel: String,
    iconId: Int,
    shortcutId: String,
    shortcutAction: String,
    populateIntentWithExtras: (Intent) -> Unit,
): Boolean {
    if (!ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
        return false
    }

    val shortcut = ShortcutInfoCompat.Builder(context, shortcutId)
        .setShortLabel(shortLabel)
        .setLongLabel(longLabel)
        .setIcon(IconCompat.createWithResource(context, iconId))
        .setIntent(
            Intent(context, MainActivity::class.java).apply {
                action = shortcutAction
                populateIntentWithExtras(this)
            }
        )
        .build()

    val pinnedShortcutCallbackIntent =
        ShortcutManagerCompat.createShortcutResultIntent(context, shortcut)

    val successCallback = PendingIntent.getBroadcast(
        context, 0,
        pinnedShortcutCallbackIntent, PendingIntent.FLAG_IMMUTABLE
    )

    return ShortcutManagerCompat.requestPinShortcut(context, shortcut, successCallback.intentSender)
}

