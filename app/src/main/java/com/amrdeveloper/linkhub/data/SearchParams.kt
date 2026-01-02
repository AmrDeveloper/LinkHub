package com.amrdeveloper.linkhub.data

data class SearchParams(
    val isLinksSelected: Boolean = true,
    val isFoldersSelected: Boolean = true,
    val isPinnedSelected: Boolean = false,
)