package com.amrdeveloper.linkhub

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.Result
import com.amrdeveloper.linkhub.data.source.LinkRepository
import kotlinx.coroutines.runBlocking

class PinnedLinksWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        val linkRepository = (application as LinkApplication).linkRepository
        return PinnedWidgetItemFactory(this, linkRepository)
    }

    private inner class PinnedWidgetItemFactory(
        private val context: Context,
        private val linkRepository: LinkRepository
    ) : RemoteViewsFactory {

        private var links: List<Link> = listOf()

        override fun onCreate() {}

        override fun onDataSetChanged() = runBlocking {
            val result = linkRepository.getPinnedLinkList()
            if(result is Result.Success) links = result.data
        }

        override fun getViewAt(position: Int): RemoteViews {
            val link = links[position]

            val remoteView = RemoteViews(context.packageName, R.layout.widget_item_link)
            remoteView.setTextViewText(R.id.link_title, link.title)
            remoteView.setTextViewText(R.id.link_subtitle, link.subtitle)

            val extras = bundleOf("url" to link.url)
            val fillInIntent = Intent()
            fillInIntent.putExtras(extras)
            remoteView.setOnClickFillInIntent(R.id.layout_test, fillInIntent)

            return remoteView
        }

        override fun onDestroy() {}

        override fun getCount(): Int = links.size

        override fun getLoadingView(): RemoteViews? = null

        override fun getViewTypeCount(): Int = 1

        override fun getItemId(position: Int): Long = links[position].id.toLong()

        override fun hasStableIds(): Boolean = true
    }
}