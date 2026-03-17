package com.amrdeveloper.linkhub.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.os.bundleOf
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.ui.MainActivity

// Static shortcuts actions
const val ACTION_CREATE_LINK = "com.amrdeveloper.linkhub.action.create_link"
const val ACTION_CREATE_FOLDER = "com.amrdeveloper.linkhub.action.create_folder"

// Dynamic shortcuts actions
const val ACTION_OPEN_LINK = "com.amrdeveloper.linkhub.action.open_link"

fun createLinkDynamicPinnedShortcut(context: Context, link: Link): Boolean {
    return createDynamicPinnedShortcut(
        context = context,
        shortLabel = link.title,
        longLabel = link.subtitle.ifEmpty { link.title },
        iconId = R.drawable.ic_link,
        shortcutId = link.hashCode().toString(),
        shortcutAction = ACTION_OPEN_LINK,
        extras = bundleOf(
            "link_id" to link.id.toString(),
            "link_url" to link.url
        ),
    )
}

private fun createDynamicPinnedShortcut(
    context: Context,
    shortLabel: String,
    longLabel: String,
    iconId: Int,
    shortcutId: String,
    shortcutAction: String,
    extras: Bundle,
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
                putExtras(extras)
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

