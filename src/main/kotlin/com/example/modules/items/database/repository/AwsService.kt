package com.example.modules.items.database.repository

import com.example.core.plugins.aws.AwsConfig
import com.example.core.plugins.aws.getAwsConfig
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.File

class AwsService(
){
    private val awsConfig = getAwsConfig()

    fun uploadS3(name: String,file: File){
        val s3Client = createS3Client(awsConfig)

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(awsConfig.bucketName)
            .key(name)
            .build()

        s3Client.putObject(putObjectRequest, file.toPath())
    }
}

private fun createS3Client(awsConfig: AwsConfig): S3Client {
    val awsCreds = AwsBasicCredentials.create(awsConfig.accessKeyId,awsConfig.secretAccessKey)
    return S3Client.builder()
        .region(Region.of(awsConfig.region))
        .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
        .build()
}