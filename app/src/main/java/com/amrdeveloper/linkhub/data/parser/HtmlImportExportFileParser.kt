package com.amrdeveloper.linkhub.data.parser

import com.amrdeveloper.linkhub.data.DataPackage
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.FolderColor
import com.amrdeveloper.linkhub.data.ImportExportFileType
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.source.FolderRepository
import com.amrdeveloper.linkhub.data.source.LinkRepository
import com.amrdeveloper.linkhub.util.UiPreferences
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Selector

class HtmlImportExportFileParser: ImportExportFileParser {
    override fun getFileType(): ImportExportFileType = ImportExportFileType.HTML

    override suspend fun importData(data: String, folderRepository: FolderRepository, linkRepository: LinkRepository): Result<DataPackage?> {
        try {
            val doc: Document = Jsoup.parse(data)
            val folders = doc.select("h3")
            for (i in folders.indices) {
                val folder = Folder(folders[i].text()).apply {
                    folderColor = FolderColor.BLUE
                }
                //the default case - no folder id
                var folderId = -1
                val getFolderRes = folderRepository.getFolderByName(folder.name)
                //a case when a folder does already exists
                if (getFolderRes.isSuccess && getFolderRes.getOrNull() != null) {
                    val existingFolder = getFolderRes.getOrNull()!!
                    folderId = existingFolder.id
                } else {
                    //a case when a folder does not exists
                    val addFolderRes = folderRepository.insertFolder(folder)
                    if (addFolderRes.isSuccess) {
                        folderId = addFolderRes.getOrDefault(-1).toInt()
                    }
                }

                val folderLinks = mutableListOf<Link>()
                val nextDL = folders[i].nextElementSibling()
                val links = nextDL.select("a")
                for (j in links.indices) {
                    val link = links[j]
                    val title = link.text()
                    val url = link.attr("href")
                    //subtitle = title = link name
                    folderLinks.add(Link(title, title, url, folderId = folderId))
                }
                linkRepository.insertLinks(folderLinks)
            }
            // If there are bookmarks without a folder, add then individually
            val rootDL = doc.select("dl").firstOrNull()
            val folderLinks = mutableListOf<Link>()
            if (rootDL != null) {
                val individualBookmarks = rootDL.select("> dt > a")
                if (individualBookmarks.isNotEmpty()) {
                    for (bookmarkElement in individualBookmarks) {
                        val title = bookmarkElement.text()
                        val url = bookmarkElement.attr("href")
                        folderLinks.add(Link(title, title, url))
                    }
                }
            }
            linkRepository.insertLinks(folderLinks)
            return Result.success(null)
        } catch (e: Selector.SelectorParseException){
            return Result.failure(e)
        }
    }
    override suspend fun exportData(
        folderRepository: FolderRepository,
        linkRepository: LinkRepository,
        uiPreferences: UiPreferences
    ): Result<String> {
        val foldersResult = folderRepository.getFolderList()
        if (foldersResult.isSuccess) {
            val folders = foldersResult.getOrDefault(listOf())
            val htmlString = buildString {
                appendLine("<!DOCTYPE NETSCAPE-Bookmark-file-1>\n")
                appendLine("<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n")
                appendLine("<TITLE>Bookmarks</TITLE>\n")
                appendLine("<H1>Bookmarks</H1>\n")
                appendLine("<DL><p>\n")

                folders.forEach {
                    val linksGetResult = linkRepository.getSortedFolderLinkList(it.id)
                    appendLine("<DT><H3>${it.name}</H3>\n")
                    if (linksGetResult.isSuccess) {
                        val links = linksGetResult.getOrDefault(listOf())
                        appendLine("<DL><p>\n")
                        links.forEach { link ->
                            appendLine("<DT><A HREF=\"${link.url}\">${link.title}</A>\n")
                        }
                        appendLine("</DL><p>\n")
                    }

                }

                val bookmarks: List<Link> = linkRepository.getSortedFolderLinkList(-1).getOrDefault(
                    listOf()
                )
                if (bookmarks.isNotEmpty()) {
                    bookmarks.forEach { link ->
                        appendLine("<DT><A HREF=\"${link.url}\">${link.title}</A>\n")
                    }
                }
                appendLine("</DL><p>\n")

            }
            return Result.success(htmlString)
        }
        return Result.failure(Throwable())
    }

}