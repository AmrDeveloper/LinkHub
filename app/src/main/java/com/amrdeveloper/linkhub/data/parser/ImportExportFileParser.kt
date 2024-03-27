package com.amrdeveloper.linkhub.data.parser

import com.amrdeveloper.linkhub.data.DataPackage
import com.amrdeveloper.linkhub.data.ImportExportFileType
import com.amrdeveloper.linkhub.data.source.FolderRepository
import com.amrdeveloper.linkhub.data.source.LinkRepository
import com.amrdeveloper.linkhub.util.UiPreferences

interface ImportExportFileParser {

    companion object {
        fun getDataParser(fileType: ImportExportFileType): ImportExportFileParser {
            return when (fileType) {
                ImportExportFileType.JSON -> JsonImportExportFileParser()
                ImportExportFileType.HTML -> HtmlImportExportFileParser()
            }
        }
    }
    suspend fun importData(data: String, folderRepository: FolderRepository, linkRepository: LinkRepository): Result<DataPackage?>
    suspend fun exportData(
        folderRepository: FolderRepository,
        linkRepository: LinkRepository,
        uiPreferences: UiPreferences
    ): Result<String>
    fun getFileType(): ImportExportFileType
}