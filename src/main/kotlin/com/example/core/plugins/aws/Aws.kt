package com.example.core.plugins.aws

import io.ktor.server.application.Application

fun getAwsConfig() = AwsConfig(
    accessKeyId = System.getenv("AWS_ACCESS_KEY_ID") ?: error("AWS_ACCESS_KEY_ID not found"),
    secretAccessKey = System.getenv("AWS_SECRET_ACCESS_KEY") ?: error("AWS_SECRET_ACCESS_KEY not found"),
    region = System.getenv("AWS_REGION") ?: error("AWS_REGION not found"),
    bucketName = System.getenv("AWS_BUCKET_NAME") ?: error("AWS_BUCKET_NAME not found")
)

fun Application.configureAws(){
    getAwsConfig()
}

