package com.amrdeveloper.linkhub.util

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.showError(errorId: Int) {
    error = context.getString(errorId)
}