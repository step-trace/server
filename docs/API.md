# 발자취 API 문서

모든 API는 로그인 후 JWT를 담아 실행해야 합니다.

- content-type: application/json
- Authorization: Bearer {JWT}

## API 목차

- [x] 카카오 로그인
- [x] 구글 로그인
- [x] 현 위치 주변 맨홀 표시
- [x] 맨홀 상세 조회
- [x] 사진 업로드
- [x] 맨홀 등록
- [x] 나의 제보 목록 조회
- [x] 나의 제보 상세 조회
- [x] 회원 탈퇴
- [x] 처리 완료 맨홀 이미지 등록
- [x] 비정상 맨홀 판단 AI 호출 결과
- [x] 정상 맨홀 판단 AI 호출 결과
- [x] 현 위치 주변 위험 맨홀 확인 API 

## 카카오 로그인

카카오 로그인을 위한 API입니다. 카카오 로그인 후 Jwt Token을 발급받습니다.

### **POST** /api/auth/v1/kakao

### Request

```json
{
  "token_type": "bearer",
  "access_token": "CfrvEdQK2pmBVBasrfmuAB0MxahcgEPuAAAAAQoXEG8AAAGYWoed3FXuKbObXTiX",
  "id_token": "eyJraWQiOiI5ZjI1MmRhZGQ1ZjIzM2Y5M2QyZmE1MjhkMTJmZWEiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJhOTExYTljZDRlZTk2YTFjMWQ0NjMwNTA3NTFlZDc2NSIsInN1YiI6IjQzNjI5NTYxMjQiLCJhdXRoX3RpbWUiOjE3NTM4NjU0OTMsImlzcyI6Imh0dHBzOi8va2F1dGgua2FrYW8uY29tIiwibmlja25hbWUiOiLquYDshLjtm4giLCJleHAiOjE3NTM4ODcwOTMsImlhdCI6MTc1Mzg2NTQ5M30.dALIn0OqREXv3l4hmMTEcuhNA6VCp5GBZfq36zsPKx3eA3-SQBAnJ7dlUqa6YZX3b0GEPKjbA91pOzvfuphtlVDbAIWTuC2wWVxS8qvyNZg-v2dgEtJpvoIf3F6zS0nG2_V7RUAIA6wNRKobn8y8PgFEqzgZygv2BmllUR55-QwkkhjfXilBWNHCORqofkx5T21W2q2qf1VKTN-U-fy_B25meg4vDvqcGaczUNbGPb8l8b-gPZPUV57iXDC0FrW6KjiLmDXV9txgmRCVXu_1DDvwnTSubhYbW2bU-5jeHNMzQZQud6I0zUDaJpVrVZekmfkyinYb3MTrT6N_OIxGdw"
}
```

### Response

```json
{
  "name": "김세훈",
  "email": "shggm2000@naver.com",
  "picture": "http://img1.kakaocdn.net/thumb/R110x110.q70/?fname=http://t1.kakaocdn.net/account_images/default_profile.jpeg",
  "jwt": "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI0MzYyOTU2MTI0IiwiZXhwIjoxNzU2MDIzNzM2LCJlbWFpbCI6InNoZ2dtMjAwMEBuYXZlci5jb20iLCJuYW1lIjpudWxsLCJuaWNrbmFtZSI6Iuq5gOyEuO2biCIsImlzcyI6InN0ZXAtdHJhY2UifQ.0X8OFLiUL6-WHv3ymMoIEWm4TbSuYba-wHRpALgbBwj2aTeQi7YoLQyVqWaOUWkYfZ64NBmh1oyuPzUb43-kZA"
}
```

---

## 구글 로그인

구글 로그인을 위한 API입니다. 구글 로그인 후 Jwt Token을 발급받습니다.

### **POST** /api/auth/v1/google

### Request

```json
{
  "token_type": "bearer",
  "access_token": "CfrvEdQK2pmBVBasrfmuAB0MxahcgEPuAAAAAQoXEG8AAAGYWoed3FXuKbObXTiX",
  "id_token": "eyJraWQiOiI5ZjI1MmRhZGQ1ZjIzM2Y5M2QyZmE1MjhkMTJmZWEiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJhOTExYTljZDRlZTk2YTFjMWQ0NjMwNTA3NTFlZDc2NSIsInN1YiI6IjQzNjI5NTYxMjQiLCJhdXRoX3RpbWUiOjE3NTM4NjU0OTMsImlzcyI6Imh0dHBzOi8va2F1dGgua2FrYW8uY29tIiwibmlja25hbWUiOiLquYDshLjtm4giLCJleHAiOjE3NTM4ODcwOTMsImlhdCI6MTc1Mzg2NTQ5M30.dALIn0OqREXv3l4hmMTEcuhNA6VCp5GBZfq36zsPKx3eA3-SQBAnJ7dlUqa6YZX3b0GEPKjbA91pOzvfuphtlVDbAIWTuC2wWVxS8qvyNZg-v2dgEtJpvoIf3F6zS0nG2_V7RUAIA6wNRKobn8y8PgFEqzgZygv2BmllUR55-QwkkhjfXilBWNHCORqofkx5T21W2q2qf1VKTN-U-fy_B25meg4vDvqcGaczUNbGPb8l8b-gPZPUV57iXDC0FrW6KjiLmDXV9txgmRCVXu_1DDvwnTSubhYbW2bU-5jeHNMzQZQud6I0zUDaJpVrVZekmfkyinYb3MTrT6N_OIxGdw"
}
```

### Response

```json
{
  "name": "김세훈",
  "email": "shggm2000@naver.com",
  "picture": "http://img1.kakaocdn.net/thumb/R110x110.q70/?fname=http://t1.kakaocdn.net/account_images/default_profile.jpeg",
  "jwt": "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI0MzYyOTU2MTI0IiwiZXhwIjoxNzU2MDIzNzM2LCJlbWFpbCI6InNoZ2dtMjAwMEBuYXZlci5jb20iLCJuYW1lIjpudWxsLCJuaWNrbmFtZSI6Iuq5gOyEuO2biCIsImlzcyI6InN0ZXAtdHJhY2UifQ.0X8OFLiUL6-WHv3ymMoIEWm4TbSuYba-wHRpALgbBwj2aTeQi7YoLQyVqWaOUWkYfZ64NBmh1oyuPzUb43-kZA"
}
```

---

## 현 위치 주변 맨홀 표시

지도에서 현 위치 주변의 맨홀을 표시하기 위한 API입니다.

### **GET** /api/v1/manholes?latitude={latitude}&longitude={longitude}

- latitude: 위도
- longitude: 경도

### Response

```json
[
  {
    "id": 1,
    "latitude": 37.5665,
    "longitude": 126.978,
    "status": "처리 중"
  },
  {
    "id": 2,
    "latitude": 37.5670,
    "longitude": 126.979,
    "status": "처리완료"
  }
  // ... more manholes
]
```

---

## 맨홀 상세 조회

맨홀의 상세 정보를 조회하기 위한 API입니다.

### **GET** /api/v1/manholes/{id}

- id: 맨홀 ID

### Response

### Pending Response

```json
{
  "id": 2,
  "latitude": 37.5665,
  "longitude": 126.978,
  "status": "PENDING",
  "place": "서울특별시 송파구 올림픽로 317",
  "before_image_urls": [
    "https://example.com/image1.jpg",
    "https://example.com/image2.jpg",
    "https://example.com/image3.jpg"
  ],
  "generated_description": [
    "위험도: 상",
    "표면 균열 및 파손 발생",
    "마감재 마모로 인한 내구성 저하",
    "보행자 발목 위험 존재"
  ],
  "created_at": "2025-08-01T23:25:15"
}
```

### Reported Response

```json
{
  "id": 2,
  "latitude": 37.5665,
  "longitude": 126.978,
  "status": "REPORTED",
  "place": "서울특별시 송파구 올림픽로 317",
  "before_image_urls": [
    "https://example.com/image1.jpg",
    "https://example.com/image2.jpg",
    "https://example.com/image3.jpg"
  ],
  "generated_description": [
    "위험도: 상",
    "표면 균열 및 파손 발생",
    "마감재 마모로 인한 내구성 저하",
    "보행자 발목 위험 존재"
  ],
  "created_at": "2025-08-01T23:25:15"
}
```

### completed Response

```json
{
  "id": 2,
  "latitude": 37.5670,
  "longitude": 126.979,
  "status": "COMPLETED",
  "place": "서울특별시 중구",
  "before_image_url": [
    "https://example.com/image3.jpg",
    "https://example.com/image4.jpg"
  ],
  "after_image_url": [
    "https://example.com/image5.jpg",
    "https://example.com/image6.jpg"
  ],
  "created_at": "2023-09-02T12:00:00Z"
}
```

---

## 사진 업로드

presigned URL을 받아 업로드를 위한 API입니다.

### 고려 사항
- 해당 presigned URL을 사용하여 클라이언트에서 직접 S3에 이미지를 업로드합니다.
- 업로드 시 PUT 요청을 사용하며 요청 헤더에 Authorization을 제거하고 Content-Type을 포함해야 합니다.
- 해당 Content-Type은 요청 본문에서 지정한 이미지의 MIME 타입과 일치해야 합니다.
- 업로드한 사진에 접근할 때는 쿼리 파라미터를 제외한 URL을 사용하면 됩니다.

### **POST** /api/images/v1/manholes

### Request Body

```json
[
  {
    "file_name": "IMG_1234.jpg",
    "content_type": "image/jpeg"
  },
  {
    "file_name": "IMG_5678.jpg",
    "content_type": "image/jpeg"
  },
  {
    "file_name": "IMG_9101.jpg",
    "content_type": "image/jpeg"
  }
]
```

### Response

```json
[
  {
    "file_name": "IMG_1234.jpg",
    "content_type": "image/jpeg",
    "presigned_url": "https://example.com/presigned-url1"
  },
  {
    "file_name": "IMG_5678.jpg",
    "content_type": "image/jpeg",
    "presigned_url": "https://example.com/presigned-url2"
  },
  {
    "file_name": "IMG_9101.jpg",
    "content_type": "image/jpeg",
    "presigned_url": "https://example.com/presigned-url3"
  }
]
```

---

## 맨홀 등록

맨홀을 등록하기 위한 API입니다.

### **POST** /api/v1/manholes

### Request Body

- image_url은 사진 업로드 API를 통해 받은 presigned URL에서 query param을 제외한 url입니다.

```json
{
  "image_urls": [
    "https://example.com/image1.jpg",
    "https://example.com/image2.jpg",
    "https://example.com/image3.jpg"
  ],
  "latitude": 37.5665,
  "longitude": 126.978,
  "place": "서울특별시 송파구 올림픽로 317",
  "title": "부식된 맨홀 교체 요청합니다.",
  "user_description": "맨홀 뚜껑이 부식되어 깨짐 위험이 있어 보행 시 사고 우려가 큽니다. 교체 요청드립니다!",
  "generated_description": [
    "위험도: 상",
    "표면 균열 및 파손 발생",
    "마감재 마모로 인한 내구성 저하",
    "보행자 발목 위험 존재"
  ]
}
```

| 필드             | 설명          | 데이터 타입         | 필수 여부 |
|----------------|-------------|----------------|-------|
| imageUrls      | 이미지 URL 목록  | Array\<String> | 필수    |
| latitude       | 위도 좌표       | Double         | 필수    |
| longitude      | 경도 좌표       | Double         | 필수    |
| place          | 신고 위치 주소    | String         | 필수    |
| title          | 신고 제목       | String         | 필수    |
| description    | 신고 상세 내용    | String         | 선택    |
| ai_description | AI 분석 결과 목록 | Array\<String> | 필수    |

---

## 나의 제보 목록 조회

나의 제보 목록을 조회하기 위한 API입니다.

### **GET** /api/v1/manholes/my-reports

### Response

```json
[
  {
    "id": 1,
    "status": "처리 중",
    "title": "부식된 맨홀 교체 요청합니다.",
    "created_at": "2023-09-01T12:00:00Z",
    "image_url": "https://example.com/image1.jpg"
  },
  {
    "id": 2,
    "status": "완료",
    "title": "파손된 맨홀 뚜껑 교체 요청",
    "created_at": "2023-09-02T12:00:00Z",
    "image_url": "https://example.com/image2.jpg"
  },
  {
    "id": 3,
    "status": "접수 전",
    "title": "맨홀 주변 도로 파손 신고",
    "created_at": "2023-09-03T12:00:00Z",
    "image_url": "https://example.com/image3.jpg"
  }
]
```

---

## 나의 제보 상세 조회

나의 제보의 상세 정보를 조회하기 위한 API입니다.

### **GET** /api/v1/manholes/my-reports/{id}

- id: 제보 ID

### Response

- user_description은 nullable합니다.

### Pending Response

```json

{
  "id": 1,
  "status": "PENDING",
  "title": "뚜껑 흔들리는 맨홀 제보 요청",
  "created_at": "2023-09-01T12:00:00Z",
  "place": "서울특별시 중구",
  "before_image_url": [
    "https://example.com/image1.jpg",
    "https://example.com/image2.jpg",
    "https://example.com/image3.jpg"
  ],
  "user_description": "맨홀 뚜껑이 흔들려 보행 시 위험합니다. 조속한 처리 부탁드립니다."
}
```

### Reported Response

```json

{
  "id": 1,
  "status": "REPORTED",
  "title": "뚜껑 흔들리는 맨홀 제보 요청",
  "created_at": "2023-09-01T12:00:00Z",
  "place": "서울특별시 중구",
  "before_image_url": [
    "https://example.com/image1.jpg",
    "https://example.com/image2.jpg",
    "https://example.com/image3.jpg"
  ],
  "user_description": "맨홀 뚜껑이 흔들려 보행 시 위험합니다. 조속한 처리 부탁드립니다."
}
```

### Completed Response

```json
{
  "id": 2,
  "status": "COMPLETED",
  "title": "파손된 맨홀 뚜껑 교체 요청",
  "created_at": "2023-09-02T12:00:00Z",
  "place": "서울특별시 중구",
  "before_image_url": [
    "https://example.com/before_image1.jpg",
    "https://example.com/before_image2.jpg"
  ],
  "after_image_url": [
    "https://example.com/after_image1.jpg",
    "https://example.com/after_image2.jpg"
  ],
  "user_description": "맨홀 뚜껑이 흔들려 보행 시 위험합니다. 조속한 처리 부탁드립니다."
}
```

---

## 회원 탈퇴

회원 탈퇴를 위한 API입니다.

### **DELETE** /api/auth/users

---

## 처리 완료 맨홀 이미지 등록

처리 완료된 맨홀의 이미지를 등록하기 위한 API입니다.

### **POST** /api/v1/manholes/completed/images/{id}

- id: 맨홀 ID

### Request

```json
{
  "after_image_url": [
    "https://example.com/completed_image1.jpg",
    "https://example.com/completed_image2.jpg"
  ]
}
```

---

## 비정상 맨홀 판단 AI 호출 결과

비정상 맨홀 판단 AI 호출 결과를 조회하기 위한 API입니다.

### **POST** /api/v1/ai/abnormal-manhole/image

### Request

```json
[
  {
    "image_url": "https://step-trace.s3.ap-northeast-2.amazonaws.com/images/manholes/7ec5c64ecb4011988ec5f9bdacc60fb95ebdd229.png",
    "content_type": "image/png"
  },
  {
    "image_url": "https://step-trace.s3.ap-northeast-2.amazonaws.com/images/manholes/455a0c487bb36213834e2f3536292a3fa04114e3.png",
    "content_type": "image/png"
  }
]
```

### Response

- 이미지 중 하나라도 정상 또는 허용 범위의 맨홀이 있는 경우
- 이미지 중 하나라도 맨홀이 아닌 경우

```String
false
```

- 모든 이미지가 비정상 맨홀로 판단된 경우에만 아래 형식으로 응답합니다.
- 위험도: [상/중/하], [주요 손상], [문제점], [안전 위험]

```String
위험도: 중, 표면 균열 발생, 테라조 재질 균열로 인한 내구성 저하, 보행자 발목 부상 위험 존재
```

## 정상 맨홀 판단 AI 호출 결과

정상 맨홀 판단 AI 호출 결과를 조회하기 위한 API입니다.

### **POST** /api/v1/ai/normal-manhole/image

### Request

```json
[
  {
    "image_url": "https://step-trace.s3.ap-northeast-2.amazonaws.com/images/manholes/7ec5c64ecb4011988ec5f9bdacc60fb95ebdd229.png",
    "content_type": "image/png"
  },
  {
    "image_url": "https://step-trace.s3.ap-northeast-2.amazonaws.com/images/manholes/455a0c487bb36213834e2f3536292a3fa04114e3.png",
    "content_type": "image/png"
  }
]
```

### Response

- 이미지 중 하나라도 비정상 맨홀이 있는 경우
- 이미지 중 하나라도 맨홀이 아닌 경우

```String
false
```

- 이미지들 전부가 정상 맨홀일 경우에만 true를 반환합니다.

```String
true
```

---

## 현 위치 주변 위험 맨홀 확인 API

현 위치 주변 위험 맨홀 확인 후 FCM을 보내는 API입니다.
가중치는 0.001로 설정했습니다.

### **GET** /api/v1/manholes/push/fcm?latitude={latitude}&longitude={longitude}&token={fcmToken}

- latitude: 위도
- longitude: 경도
- token: FCM token