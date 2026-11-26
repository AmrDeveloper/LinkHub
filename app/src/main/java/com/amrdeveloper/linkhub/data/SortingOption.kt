package com.amrdeveloper.linkhub.data

import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.ui.components.ShowOption

val sortingOptions = listOf(
    ShowOption("Default", R.drawable.ic_sorting),
    ShowOption("Name A-Z", R.drawable.ic_sort_az),
    ShowOption("Name Z-A", R.drawable.ic_sort_za),
    ShowOption("Clicks Asc", R.drawable.ic_sort_12),
    ShowOption("Clicks Desc", R.drawable.ic_sort_21),
)

enum class SortingOptions {
    NAME_ASC,
    NAME_DESC,
    CLICK_COUNT_ASC,
    CLICK_COUNT_DESC,
    DEFAULT
}
