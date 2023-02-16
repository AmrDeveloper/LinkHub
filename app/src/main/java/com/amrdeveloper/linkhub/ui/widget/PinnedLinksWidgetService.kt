package com.amrdeveloper.linkhub.ui.widget

import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.amrdeveloper.linkhub.BuildConfig
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.source.LinkRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class PinnedLinksWidgetService : RemoteViewsService() {

    @Inject lateinit var linkRepository : LinkRepository

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return PinnedWidgetItemFactory(linkRepository)
    }

    private inner class PinnedWidgetItemFactory (
        private val linkRepository: LinkRepository
    ) : RemoteViewsFactory {

        private lateinit var links: MutableList<Link>

        override fun onCreate() {
            links = mutableListOf()
        }

        override fun onDataSetChanged() = runBlocking {
            val result = linkRepository.getPinnedLinkList()
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    links.clear()
                    links.addAll(it)
                }
            }
        }

        override fun getViewAt(position: Int): RemoteViews {
            val link = links[position]

            val remoteView = RemoteViews(BuildConfig.APPLICATION_ID, R.layout.widget_item_link)
            remoteView.setTextViewText(R.id.link_title, link.title)
            remoteView.setTextViewText(R.id.link_subtitle, link.subtitle)

            val extras = bundleOf("url" to link.url)
            val fillInIntent = Intent()
            fillInIntent.putExtras(extras)
            remoteView.setOnClickFillInIntent(R.id.widget_item_layout, fillInIntent)

            return remoteView
        }

        override fun onDestroy() {
            links.clear()
        }

        override fun getCount(): Int = links.size

        override fun getLoadingView(): RemoteViews? = null

        override fun getViewTypeCount(): Int = 1

        override fun getItemId(position: Int): Long = links[position].id.toLong()

        override fun hasStableIds(): Boolean = true
    }
}