package com.amrdeveloper.linkhub.data

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DataPackage(

    @SerializedName("folders")
    var folders: List<Folder>? = listOf(),

    @SerializedName("links")
    var links: List<Link>?= listOf(),

    @SerializedName("showClickCounter")
    var showClickCounter: Boolean? = true,

    @SerializedName("autoSaving")
    var enableAutoSaving: Boolean? = false,

    @SerializedName("defaultFolderMode")
    var defaultFolderMode: Boolean? = true,

    @SerializedName("theme")
    var theme: Theme? = Theme.WHITE
)