package com.steptrace.image.service

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.steptrace.config.AwsProperties
import com.steptrace.image.dto.ImageS3UploadRequest
import com.steptrace.image.dto.ImageS3UploadResponse
import org.springframework.stereotype.Service
import java.util.*

/**
 * AWS S3 이미지 업로드 서비스
 * 
 * 맨홀 이미지 업로드를 위한 Pre-signed URL을 생성합니다.
 * 클라이언트에서 직접 S3에 이미지를 업로드할 수 있도록 임시 URL을 제공합니다.
 */
@Service
class ImageService(
        private val amazonS3: AmazonS3,            // AWS S3 클라이언트
        private val awsProperties: AwsProperties    // AWS 설정 정보
) {

    /**
     * 이미지 업로드를 위한 Pre-signed URL들을 생성합니다.
     * 
     * 다중 이미지 업로드 요청을 처리하며, 각 이미지에 대해 고유한
     * S3 객체 키를 생성하고 15분간 유효한 Pre-signed URL을 발급합니다.
     * 
     * @param requests 이미지 업로드 요청 목록
     * @return Pre-signed URL을 포함한 업로드 응답 목록
     * @throws IllegalArgumentException 중복된 파일명이 있는 경우
     */
    fun getPresignedUrls(requests: List<ImageS3UploadRequest>): List<ImageS3UploadResponse> {
        validateNoDuplicates(requests)  // 중복 파일 검증

        return requests.map { request ->
            val objectKey = generateObjectKey(request.fileName)              // 고유한 S3 객체 키 생성
            val presignedUrl = generatePresignedUrl(objectKey, request.contentType)  // Pre-signed URL 생성

            ImageS3UploadResponse.of(request.fileName, request.contentType, presignedUrl)
        }
    }

    /**
     * 업로드 요청 중 중복된 파일명이 있는지 검증합니다.
     * 
     * 동일한 파일명과 컨텐츠 타입을 가진 요청이 여러 개 있는 경우
     * 예외를 발생시켜 중복 업로드를 방지합니다.
     * 
     * @param requests 검증할 업로드 요청 목록
     * @throws IllegalArgumentException 중복된 파일이 있는 경우
     */
    private fun validateNoDuplicates(requests: List<ImageS3UploadRequest>) {
        require(requests.distinct().size == requests.size) { "중복된 파일이 존재합니다." }
    }

    /**
     * S3 저장을 위한 고유한 객체 키를 생성합니다.
     * 
     * UUID의 첫 8자리를 사용하여 파일명 중복을 방지하고,
     * "manholes/" 폴더에 이미지들을 체계적으로 정리합니다.
     * 
     * @param fileName 원본 파일명
     * @return S3 저장용 고유 객체 키 ("manholes/{uuid}-{fileName}" 형식)
     */
    private fun generateObjectKey(fileName: String): String {
        val uuid = UUID.randomUUID().toString().substring(0, 8)  // 8자리 고유 ID 생성
        return "manholes/$uuid-$fileName"  // 체계적인 폴더 구조로 저장
    }

    /**
     * S3 객체 업로드를 위한 Pre-signed URL을 생성합니다.
     * 
     * 15분 동안 유효한 PUT 요청용 Pre-signed URL을 생성하여
     * 클라이언트가 직접 S3에 이미지를 업로드할 수 있도록 합니다.
     * 업로드된 이미지는 공개 읽기 권한으로 설정됩니다.
     * 
     * @param objectKey S3 객체 키
     * @param contentType 이미지 MIME 타입
     * @return 15분간 유효한 Pre-signed URL
     */
    private fun generatePresignedUrl(objectKey: String, contentType: String): String {
        val expiration = Date().apply {
            time += 1000 * 60 * 15  // 15분 후 만료 시간 설정
        }

        val generatePresignedUrlRequest = GeneratePresignedUrlRequest(
                awsProperties.s3.bucket,  // S3 버킷명
                objectKey                 // 저장될 객체 키
        )

        generatePresignedUrlRequest.method = HttpMethod.PUT              // PUT 메서드로 업로드
        generatePresignedUrlRequest.expiration = expiration              // 만료 시간 설정
        generatePresignedUrlRequest.contentType = contentType           // 컨텐츠 타입 지정
        generatePresignedUrlRequest.addRequestParameter("x-amz-acl", "public-read")  // 공개 읽기 권한 설정

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString()
    }
}