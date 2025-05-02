package com.example.modules.items.database.repository

import com.example.core.plugins.aws.AwsConfig
import com.example.core.plugins.aws.getAwsConfig
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest

class AwsService(
){
    private val awsConfig = getAwsConfig()

    private fun getAwsUrl(fileName: String) =
        "https://${awsConfig.bucketName}.s3.${awsConfig.region}.amazonaws.com/$fileName"

    fun uploadS3(name: String,byteArray: ByteArray): String{
        val s3Client = createS3Client(awsConfig)

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(awsConfig.bucketName)
            .key(name)
            .build()

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(byteArray))
        return getAwsUrl(name)
    }
}

private fun createS3Client(awsConfig: AwsConfig): S3Client {
    val awsCreds = AwsBasicCredentials.create(awsConfig.accessKeyId,awsConfig.secretAccessKey)
    return S3Client.builder()
        .region(Region.of(awsConfig.region))
        .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
        .build()
}