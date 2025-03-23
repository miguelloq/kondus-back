package com.example.modules.items.domain

sealed class ItemErros(override val message: String): Exception(message) {

}