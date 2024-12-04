package com.example.core.models

data class RequestWrapDto<T>(
    val request: T,
    val coreUserId: CoreUser.Id
)