package com.amrdeveloper.linkhub.common

data class LazyValue<T>(
    var data: T,
    var isLoading: Boolean
)