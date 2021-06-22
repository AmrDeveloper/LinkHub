package com.amrdeveloper.linkhub.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openLinkIntent(context: Context, link : String) {
    val openIntent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(link))
    context.startActivity(openIntent)
}

fun shareTextIntent(context: Context, text : String) {
    val sendIntent = Intent(Intent.ACTION_SEND)
    sendIntent.putExtra(Intent.EXTRA_TEXT, text)
    sendIntent.type = "text/plain"
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}