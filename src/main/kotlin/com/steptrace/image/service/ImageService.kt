package com.steptrace.image.service

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.steptrace.config.AwsProperties
import com.steptrace.image.dto.ImageS3UploadRequest
import com.steptrace.image.dto.ImageS3UploadResponse
import org.springframework.stereotype.Service
import java.util.*

@Service
class ImageService(
        private val amazonS3: AmazonS3,
        private val awsProperties: AwsProperties
) {

    fun getPresignedUrls(requests: List<ImageS3UploadRequest>): List<ImageS3UploadResponse> {
        return requests.map { request ->
            val objectKey = generateObjectKey(request.fileName)
            val presignedUrl = generatePresignedUrl(objectKey, request.contentType)

            ImageS3UploadResponse.of(request.fileName, request.contentType, presignedUrl)
        }
    }

    private fun generateObjectKey(fileName: String): String {
        val uuid = UUID.randomUUID().toString().substring(0, 8)
        return "manholes/$uuid-$fileName"
    }

    private fun generatePresignedUrl(objectKey: String, contentType: String): String {
        val expiration = Date().apply {
            time += 1000 * 60 * 15
        }

        val generatePresignedUrlRequest = GeneratePresignedUrlRequest(
                awsProperties.s3.bucket,
                objectKey
        )

        generatePresignedUrlRequest.method = HttpMethod.PUT
        generatePresignedUrlRequest.expiration = expiration
        generatePresignedUrlRequest.contentType = contentType
        generatePresignedUrlRequest.addRequestParameter("x-amz-acl", "public-read")

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString()
    }
}