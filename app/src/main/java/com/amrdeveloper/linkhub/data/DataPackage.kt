package com.amrdeveloper.linkhub.data

import com.google.gson.annotations.SerializedName

data class DataPackage (

    @SerializedName("folders")
    val folders: List<Folder>,

    @SerializedName("links")
    val links  : List<Link>,

    @SerializedName("showClickCounter")
    val showClickCounter : Boolean?,

    @SerializedName("autoSaving")
    val enableAutoSaving : Boolean?,

    @SerializedName("defaultFolderMode")
    val defaultFolderMode : Boolean?,

    @SerializedName("theme")
    val theme : Theme?
)