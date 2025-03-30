package com.example.modules.items.domain

import com.example.core.error.NotFoundError

sealed class ItemError(override val message: String): Exception(message) {
    data class InvalidField(val fieldName: String, val reason: String):
        ItemError("The $fieldName is not valid because $reason.")

    data class NotFound(val entityName: String):
            ItemError("$entityName not found(s)."), NotFoundError

    data object ActionNotPermitted:
            ItemError("The user cannot do that action.")
}