package com.example.modules.items.database.repository

import com.example.core.plugins.aws.AwsConfig
import com.example.core.plugins.aws.getAwsConfig
import com.example.modules.items.presenter.ImageDto
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.security.SecureRandom

class AwsService(
){
    private val awsConfig = getAwsConfig()

    private fun getAwsUrl(fileName: String) =
        "https://${awsConfig.bucketName}.s3.${awsConfig.region}.amazonaws.com/$fileName"

    fun uploadS3(imageDto: ImageDto): String{
        val s3Client = createS3Client(awsConfig)

        val s3Name = imageDto.generateValidS3Name()

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(awsConfig.bucketName)
            .key(s3Name)
            .contentType(imageDto.contentType)
            .acl("public-read")
            .build()

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageDto.bytes))
        return getAwsUrl(s3Name)
    }
}

private fun createS3Client(awsConfig: AwsConfig): S3Client {
    val awsCreds = AwsBasicCredentials.create(awsConfig.accessKeyId,awsConfig.secretAccessKey)
    return S3Client.builder()
        .region(Region.of(awsConfig.region))
        .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
        .build()
}

private fun ImageDto.generateValidS3Name(): String{
    val cryptoName = (generateSecureRandomString(15) + "-" + name)
        .replace("+","-") //"+" symbol are replaced by %2B in the aws s3 object url, so they are invalid and other symbols must be used.
    return cryptoName
}

private fun generateSecureRandomString(length: Int): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789&()-_=+"
    val secureRandom = SecureRandom()
    return (1..length)
        .map { chars[secureRandom.nextInt(chars.length)] }
        .joinToString("")
}