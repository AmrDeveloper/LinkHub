package com.amrdeveloper.linkhub.common

import androidx.annotation.StringRes

sealed class TaskState {
    object Idle : TaskState()
    object Success : TaskState()
    data class Error(@StringRes val message: Int) : TaskState()
}