package com.example.modules.locals.domain.error


sealed class LocalError(override val message: String): Exception(message)  {
    data class InvalidField(val fieldName: String, val reason: String):
        LocalError("The $fieldName is not valid because $reason.")

    data object InvalidLocalCategory : LocalError("The chosen local have a invalid type of category.")

    data object LocalDontExists: LocalError("The local informed dont exists.")
}