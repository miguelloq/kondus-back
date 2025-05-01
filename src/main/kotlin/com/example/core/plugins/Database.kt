package com.example.core.plugins

import io.ktor.server.application.Application
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun Application.configureDatabase(){
    val url = System.getenv("DATABASE_URL") ?: error("DATABASE_URL not found")
    val user = System.getenv("DATABASE_USER") ?: error("DATABASE_USER not found")
    val password = System.getenv("DATABASE_PASSWORD") ?: error("DATABASE_PASSWORD not found")
    val driver = "org.postgresql.Driver"

    Database.connect(
        url,
        user = user,
        password = password,
        driver = driver
    )

//    Flyway
//        .configure()
//        .dataSource(url, user, password)
//        .validateMigrationNaming(true)
//        .load()
//        .migrate()
}

suspend fun <T> suspendTransaction(block: suspend Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)