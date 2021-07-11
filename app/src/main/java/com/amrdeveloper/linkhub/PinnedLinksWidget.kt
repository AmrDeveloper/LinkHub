package com.amrdeveloper.linkhub

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.amrdeveloper.linkhub.util.WIDGET_ITEM_CLICK_ACTION
import com.amrdeveloper.linkhub.util.WIDGET_REFRESH_ACTION
import com.amrdeveloper.linkhub.util.openLinkIntent

class PinnedLinksWidget : AppWidgetProvider() {

    companion object {

        fun refresh(context: Context) {
            val intent = Intent(WIDGET_REFRESH_ACTION)
            intent.component = ComponentName(context, PinnedLinksWidget::class.java)
            context.sendBroadcast(intent)
        }

    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        when(intent?.action) {
            WIDGET_ITEM_CLICK_ACTION -> {
                val url = intent.getStringExtra("url")
                openLinkIntent(context, url.toString())
            }
            WIDGET_REFRESH_ACTION -> {
                val widgetManager = AppWidgetManager.getInstance(context)
                val componentName = ComponentName(context, PinnedLinksWidget::class.java)
                val widgetId = widgetManager.getAppWidgetIds(componentName)
                widgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.links_listview)
            }
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val serviceIntent = Intent(context, PinnedLinksWidgetService::class.java)
    serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    serviceIntent.data = Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.pinned_links_widget)
    views.setRemoteAdapter(R.id.links_listview, serviceIntent)
    views.setEmptyView(R.id.links_listview, R.id.links_widget_empty_view)

    // Setup View link in browser intent template
    val viewLinkIntent = Intent(context, PinnedLinksWidget::class.java)
    viewLinkIntent.action = WIDGET_ITEM_CLICK_ACTION
    viewLinkIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    val viewPendingIntent = PendingIntent.getBroadcast(context, 0, viewLinkIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    views.setPendingIntentTemplate(R.id.links_listview, viewPendingIntent)

    appWidgetManager.updateAppWidget(appWidgetId, views)
}