package com.amrdeveloper.linkhub.ui.importexport

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.FolderColor
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.source.FolderRepository
import com.amrdeveloper.linkhub.data.source.LinkRepository
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ImportExportHtmlViewModel @Inject constructor (
    private val folderRepository: FolderRepository,
    private val linkRepository: LinkRepository,
) : ViewModel() {

    private val _stateMessages = MutableLiveData<Int>()
    val stateMessages = _stateMessages

    fun importDataFile(data : String) {
        viewModelScope.launch {
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

                _stateMessages.value = R.string.message_data_imported
            } catch (e : JsonSyntaxException) {
                _stateMessages.value = R.string.message_invalid_data_format
            }
        }
    }

    fun exportDataFile(context: Context) {
        viewModelScope.launch {
            val foldersResult = folderRepository.getFolderList()
            if (foldersResult.isSuccess) {
                val folders = foldersResult.getOrDefault(listOf())
                createdExportedHtmlFile(context, folders)
                _stateMessages.value = R.string.message_data_exported
            } else {
                _stateMessages.value = R.string.message_invalid_export
            }
        }
    }
    private fun createdExportedHtmlFile(context: Context, folders: List<Folder>) {
        viewModelScope.launch {
            val fileName = System.currentTimeMillis().toString() + ".html"
            val htmlString = buildString {
                appendLine("<!DOCTYPE NETSCAPE-Bookmark-file-1>\n")
                appendLine("<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n")
                appendLine("<TITLE>Bookmarks</TITLE>\n")
                appendLine("<H1>Bookmarks</H1>\n")
                appendLine("<DL><p>\n")

                folders.forEach {
                    val res = linkRepository.getSortedFolderLinkList(it.id)
                    appendLine("<DT><H3>${it.name}</H3>\n")
                    if (res.isSuccess) {
                        val links = res.getOrDefault(listOf())
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
                if(bookmarks.isNotEmpty()) {
                    bookmarks.forEach { link ->
                        appendLine("<DT><A HREF=\"${link.url}\">${link.title}</A>\n")
                    }
                }
                appendLine("</DL><p>\n")

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val values = ContentValues()
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                values.put(MediaStore.MediaColumns.MIME_TYPE, "text/html")
                values.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DOWNLOADS
                )
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                val outputStream = uri?.let { resolver.openOutputStream(it) }
                outputStream?.write(htmlString.toByteArray())
            } else {
                val downloadDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                val dataFile = File(downloadDir, fileName)
                dataFile.writeText(htmlString)
            }

        }
    }


}