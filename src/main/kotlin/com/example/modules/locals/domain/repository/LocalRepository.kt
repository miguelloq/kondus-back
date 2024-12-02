package com.example.modules.locals.domain.repository

import com.example.modules.locals.domain.model.LocalModel

interface LocalRepository{
    suspend fun create(model: LocalModel, creatorUsedId: Long): Long
}