package com.amrdeveloper.linkhub.data

data class DataPackage (
    val folders: List<Folder>,
    val links  : List<Link>,
    val showClickCounter : Boolean?,
    val theme : Theme?
)