package com.example.modules.users.domain.service

import com.example.core.model.TokenConfig

interface CreateTokenService {
    fun getToken(userId: Long, tokenConfig: TokenConfig): String
}