package com.example.modules.users.domain.error

import com.example.modules.locals.domain.error.LocalError

sealed class UserError(override val message: String): Exception(message) {
    data class InvalidField(val fieldName: String, val reason: String):
        UserError("The $fieldName is not valid because $reason.")

    data object NotFindedInDatabase : UserError("User not finded in the database.")

    data object EmailAlreadyRegistered: UserError("Email is already in use.")

    data object WrongPassword: UserError("Wrong password!")

    data object HouseDontExists: UserError("The house informed dont exists.")

    data object Unknown: UserError("Unknown Error.")
}