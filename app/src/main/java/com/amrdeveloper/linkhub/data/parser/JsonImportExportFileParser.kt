package com.amrdeveloper.linkhub.data.parser

import com.amrdeveloper.linkhub.data.DataPackage
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.FolderColor
import com.amrdeveloper.linkhub.data.ImportExportFileType
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.Theme
import com.amrdeveloper.linkhub.data.source.FolderRepository
import com.amrdeveloper.linkhub.data.source.LinkRepository
import com.amrdeveloper.linkhub.util.UiPreferences
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException

class JsonImportExportFileParser : ImportExportFileParser {
    override fun getFileType(): ImportExportFileType = ImportExportFileType.JSON
    override suspend fun importData(
        data: String,
        folderRepository: FolderRepository,
        linkRepository: LinkRepository
    ): Result<DataPackage?> {
        try {
            val jsonElement = JsonParser.parseString(data)
            if (!jsonElement.isJsonObject) {
                return Result.success(null)
            }

            val jsonObject = jsonElement.asJsonObject
            val jsonObjectKeys = jsonObject.keySet()

            // TODO: Replace with reflection later
            val validKeysSet = setOf("folders", "links", "showClickCounter", "autoSaving", "defaultFolderMode", "theme")
            if (jsonObjectKeys.size != validKeysSet.size) {
                return Result.success(null)
            }

            val gson = Gson()
            var dataPackage = DataPackage()

            if (jsonObjectKeys == validKeysSet) {
                dataPackage = gson.fromJson(data, DataPackage::class.java)
            } else {
                val keysAsList = jsonObjectKeys.toList()
                jsonObject.getAsJsonArray(keysAsList[0])?.let { jsonLinks ->
                    dataPackage.folders = gson.fromJson(jsonLinks, Array<Folder>::class.java).toList()
                }

                jsonObject.getAsJsonArray(keysAsList[1])?.let { jsonLinks ->
                    dataPackage.links = gson.fromJson(jsonLinks, Array<Link>::class.java).toList()
                }

                dataPackage.showClickCounter = jsonObject[keysAsList[2]].asBoolean
                dataPackage.enableAutoSaving = jsonObject[keysAsList[3]].asBoolean
                dataPackage.defaultFolderMode = jsonObject[keysAsList[4]].asBoolean

                jsonObject[keysAsList[5]]?.asString?.let { jsonTheme ->
                    dataPackage.theme = Theme.entries.firstOrNull { it.name.equals(jsonTheme, ignoreCase = true) }
                }
            }

            // This code should be removed after found why it not serialized on some devices (see Issue #23)
            // folderColor field is declared as non nullable type but in this case GSON will break the null safety feature
            dataPackage.folders?.forEach { if (it.folderColor == null) it.folderColor = FolderColor.BLUE }

            dataPackage.folders?.let { folderRepository.insertFolders(it) }
            dataPackage.links?.let { linkRepository.insertLinks(it) }
            return Result.success(dataPackage)
        } catch (e: JsonSyntaxException) {
            return Result.failure(e)
        }
    }

    override suspend fun exportData(
        folderRepository: FolderRepository,
        linkRepository: LinkRepository,
        uiPreferences: UiPreferences
    ): Result<String> {
        val foldersResult = folderRepository.getFolderList()
        val linksResult = linkRepository.getLinkList()
        if (foldersResult.isSuccess && linksResult.isSuccess) {
            val folders = foldersResult.getOrDefault(listOf())
            val links = linksResult.getOrDefault(listOf())
            val showClickCounter = uiPreferences.isClickCounterEnabled()
            val autoSaving = uiPreferences.isAutoSavingEnabled()
            val defaultFolder = uiPreferences.isDefaultFolderEnabled()
            val lastTheme = uiPreferences.getThemeType()
            val dataPackage =
                DataPackage(folders, links, showClickCounter, autoSaving, defaultFolder, lastTheme)
            return Result.success(Gson().toJson(dataPackage))
        } else {
            return Result.failure(Throwable());
        }
    }

}