package com.amrdeveloper.linkhub.data

import androidx.annotation.DrawableRes
import com.amrdeveloper.linkhub.R

enum class FolderColor(@DrawableRes val drawableId: Int) {
    NONE(R.drawable.ic_directory_none),
    BLUE(R.drawable.ic_directory_blue),
    RED(R.drawable.ic_directory_red),
    GREEN(R.drawable.ic_directory_green),
    YELLOW(R.drawable.ic_directory_yellow),
    ORANGE(R.drawable.ic_directory_orange),
    PURPLE(R.drawable.ic_directory_purple),
    ROSE(R.drawable.ic_directory_rose),
    GRAY(R.drawable.ic_directory_gray);
}