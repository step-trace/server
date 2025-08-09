package com.steptrace.scheduler.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.steptrace.config.AwsProperties
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.time.LocalDate

@Service
class S3Service(
    private val amazonS3: AmazonS3,
    private val awsProperties: AwsProperties
) {
    
    fun uploadFile(key: String, data: ByteArray, contentType: String): String {
        val metadata = ObjectMetadata().apply {
            contentLength = data.size.toLong()
            setContentType(contentType)
        }
        
        val inputStream = ByteArrayInputStream(data)
        val putRequest = PutObjectRequest(awsProperties.s3.bucket, key, inputStream, metadata)
        
        amazonS3.putObject(putRequest)
        
        return amazonS3.getUrl(awsProperties.s3.bucket, key).toString()
    }
    
    fun uploadExcelFile(fileName: String, data: ByteArray, date: LocalDate) {
        uploadFile("reports/$date/$fileName", data, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    }
    
    fun uploadHtmlFile(fileName: String, htmlContent: String, date: LocalDate) {
        uploadFile("reports/$date/$fileName", htmlContent.toByteArray(Charsets.UTF_8), "text/html; charset=utf-8")
    }
}