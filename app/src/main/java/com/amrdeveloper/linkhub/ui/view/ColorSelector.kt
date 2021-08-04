package com.amrdeveloper.linkhub.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.FolderColor

class ColorSelector @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var folderColorsArray = FolderColor.values()
    private var folderColorsArraySize = folderColorsArray.size
    private var selectedFolderColorIndex = 0

    private val previewFolderImg: ImageView
    private val prevSelectorImg: ImageView
    private val nextSelectorImg: ImageView

    private var onFolderColorChangeListener: ((FolderColor) -> Unit)? = null

    init {
        orientation = HORIZONTAL
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.color_selector, this)
        previewFolderImg = view.findViewById(R.id.preview_folder_img)

        prevSelectorImg = view.findViewById(R.id.selector_prev_img)
        prevSelectorImg.setOnClickListener {
            selectPrevFolderColor()
        }

        nextSelectorImg = view.findViewById(R.id.selector_next_img)
        nextSelectorImg.setOnClickListener {
            selectNextFolderColor()
        }
    }

    private fun selectPrevFolderColor() {
        if (selectedFolderColorIndex == 0) selectedFolderColorIndex = folderColorsArraySize
        selectedFolderColorIndex--
        val folderColor = folderColorsArray[selectedFolderColorIndex]
        previewFolderImg.setImageResource(folderColor.drawableId)
        onFolderColorChangeListener?.let { callback -> callback(folderColor) }
    }

    private fun selectNextFolderColor() {
        if (selectedFolderColorIndex == folderColorsArray.lastIndex) selectedFolderColorIndex = -1
        selectedFolderColorIndex++
        val folderColor = folderColorsArray[selectedFolderColorIndex]
        previewFolderImg.setImageResource(folderColor.drawableId)
        onFolderColorChangeListener?.let { callback -> callback(folderColor) }
    }

    fun setOnFolderColorChangeListener(listener: ((FolderColor) -> Unit)) {
        onFolderColorChangeListener = listener
    }

    fun setCurrentFolderColor(folderColor: FolderColor) {
        selectedFolderColorIndex = folderColorsArray.indexOf(folderColor)
        previewFolderImg.setImageResource(folderColor.drawableId)
    }

    fun getCurrentFolderColor(): FolderColor = folderColorsArray[selectedFolderColorIndex]
}