package com.amrdeveloper.linkhub.data.parser

import com.amrdeveloper.linkhub.data.DataPackage
import com.amrdeveloper.linkhub.data.FolderColor
import com.amrdeveloper.linkhub.data.ImportExportFileType
import com.amrdeveloper.linkhub.data.source.FolderRepository
import com.amrdeveloper.linkhub.data.source.LinkRepository
import com.amrdeveloper.linkhub.util.UiPreferences
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class JsonImportExportFileParser: ImportExportFileParser {
    override fun getFileType(): ImportExportFileType = ImportExportFileType.JSON
    override suspend fun importData(
        data: String,
        folderRepository: FolderRepository,
        linkRepository: LinkRepository
    ): Result<DataPackage?> {
        try{
            val dataPackage = Gson().fromJson(data, DataPackage::class.java)

            val folders = dataPackage.folders
            // This code should be removed after found why it not serialized on some devices (see Issue #23)
            // folderColor field is declared as non nullable type but in this case GSON will break the null safty feature
            folders.forEach { if (it.folderColor == null) it.folderColor = FolderColor.BLUE }
            folderRepository.insertFolders(folders)

            linkRepository.insertLinks(dataPackage.links)
            return Result.success(dataPackage)
        } catch (e : JsonSyntaxException) {
            return Result.failure(e)
        }
    }
    override  suspend fun exportData(
        folderRepository: FolderRepository,
        linkRepository: LinkRepository,
        uiPreferences: UiPreferences
    ): Result<String>{
        val foldersResult = folderRepository.getFolderList()
        val linksResult = linkRepository.getLinkList()
        if (foldersResult.isSuccess && linksResult.isSuccess) {
            val folders = foldersResult.getOrDefault(listOf())
            val links = linksResult.getOrDefault(listOf())
            val showClickCounter = uiPreferences.isClickCounterEnabled()
            val autoSaving = uiPreferences.isAutoSavingEnabled()
            val defaultFolder = uiPreferences.isDefaultFolderEnabled()
            val lastTheme = uiPreferences.getThemeType()
            val dataPackage = DataPackage(folders, links, showClickCounter, autoSaving, defaultFolder, lastTheme)
            return Result.success(Gson().toJson(dataPackage))
        } else {
            return Result.failure(Throwable());
        }
    }

}