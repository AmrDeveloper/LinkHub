package com.amrdeveloper.linkhub.data

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import com.amrdeveloper.linkhub.R
import com.google.gson.annotations.SerializedName

@Keep
enum class FolderColor(@DrawableRes val drawableId: Int) {
    @SerializedName("NONE")
    NONE(R.drawable.ic_directory_none),

    @SerializedName("BLUE")
    BLUE(R.drawable.ic_directory_blue),

    @SerializedName("RED")
    RED(R.drawable.ic_directory_red),

    @SerializedName("GREEN")
    GREEN(R.drawable.ic_directory_green),

    @SerializedName("YELLOW")
    YELLOW(R.drawable.ic_directory_yellow),

    @SerializedName("ORANGE")
    ORANGE(R.drawable.ic_directory_orange),

    @SerializedName("PURPLE")
    PURPLE(R.drawable.ic_directory_purple),

    @SerializedName("ROSE")
    ROSE(R.drawable.ic_directory_rose),

    @SerializedName("GRAY")
    GRAY(R.drawable.ic_directory_gray);
}