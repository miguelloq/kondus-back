package com.example.core.presenter.dto

import com.example.core.models.CoreUser

data class RequestWrapDto<T>(
    val request: T,
    val coreUserId: CoreUser.Id
)