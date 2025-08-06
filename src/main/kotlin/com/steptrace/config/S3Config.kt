package com.steptrace.config

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Config(private val awsProperties: AwsProperties) {

    @Bean
    fun amazonS3Client(): AmazonS3 {
        val credentials: AWSCredentials = BasicAWSCredentials(
                awsProperties.credentials.accessKey,
                awsProperties.credentials.secretKey
        )

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(AWSStaticCredentialsProvider(credentials))
                .withRegion(awsProperties.region.static)
                .build()
    }
}

@ConfigurationProperties(prefix = "cloud.aws")
data class AwsProperties(
        val credentials: Credentials,
        val s3: S3,
        val region: Region
) {
    data class Credentials(
            val accessKey: String,
            val secretKey: String
    )
    data class S3(
            val bucket: String
    )
    data class Region(
            val static: String
    )
}