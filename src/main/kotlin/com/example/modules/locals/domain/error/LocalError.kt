package com.example.modules.locals.domain.error


sealed class LocalError(override val message: String): Exception(message)  {
    data class InvalidField(val fieldName: String, val reason: String):
        LocalError("The $fieldName is not valid because $reason.")
}