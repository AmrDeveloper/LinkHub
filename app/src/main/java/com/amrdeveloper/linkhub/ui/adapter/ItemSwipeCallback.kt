package com.amrdeveloper.linkhub.ui.adapter

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ItemSwipeCallback(
    private val icon : Drawable?,
    private val background : ColorDrawable,
    private val onSwipeCallback: (RecyclerView.ViewHolder) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val intrinsicHeight = icon!!.intrinsicHeight
    private val intrinsicWidth = icon!!.intrinsicWidth

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwipeCallback(viewHolder)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                             dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val itemView: View = viewHolder.itemView
        val iconMargin: Int = (itemView.height - intrinsicHeight) / 2
        val iconTop: Int = itemView.top + (itemView.height - intrinsicHeight) / 2
        val iconBottom = iconTop + intrinsicHeight

        when {
            // Swipe left
            dX > 0 -> {
                val iconLeft: Int = itemView.left + iconMargin
                val iconRight = iconLeft + intrinsicWidth
                icon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
            }
            // Swipe right
            dX < 0 -> {
                val iconLeft: Int = itemView.right - iconMargin - intrinsicWidth
                val iconRight: Int = itemView.right - iconMargin
                icon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
            }
            // Not swiped
            else -> {
                background.setBounds(0, 0, 0, 0)
                icon?.setBounds(0, 0, 0, 0)
            }
        }

        background.draw(c)
        icon?.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}