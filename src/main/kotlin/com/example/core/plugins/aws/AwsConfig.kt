package com.example.core.plugins.aws

data class AwsConfig(
    val accessKeyId: String,
    val secretAccessKey: String,
    val region: String,
    val bucketName: String
)
