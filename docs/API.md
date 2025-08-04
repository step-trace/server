# 발자취 API 문서

모든 API는 로그인 후 IdToken을 담아 실행해야 합니다.

- content-type: application/json
- Authorization: Bearer {IdToken}

## API 목차

- [x] 카카오 로그인
- [x] 구글 로그인
- [ ] 현 위치 주변 맨홀 표시
- [x] 처리 중인 맨홀 조회
- [ ] 처리 완료된 맨홀 조회
- [ ] 사진 업로드
- [x] 맨홀 등록
- [ ] 나의 제보 목록 조회
- [ ] 나의 처리 중인 제보 상세 조회
- [ ] 나의 처리 완료된 제보 상세 조회
- [x] 회원 탈퇴

## 카카오 로그인

카카오 로그인을 위한 API입니다. 카카오 로그인 후 Jwt Token을 발급받습니다.

### **POST** /api/auth/v1/kakao

### Request

```json
{
  "token_type": "bearer",
  "access_token": "CfrvEdQK2pmBVBasrfmuAB0MxahcgEPuAAAAAQoXEG8AAAGYWoed3FXuKbObXTiX",
  "id_token": "eyJraWQiOiI5ZjI1MmRhZGQ1ZjIzM2Y5M2QyZmE1MjhkMTJmZWEiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJhOTExYTljZDRlZTk2YTFjMWQ0NjMwNTA3NTFlZDc2NSIsInN1YiI6IjQzNjI5NTYxMjQiLCJhdXRoX3RpbWUiOjE3NTM4NjU0OTMsImlzcyI6Imh0dHBzOi8va2F1dGgua2FrYW8uY29tIiwibmlja25hbWUiOiLquYDshLjtm4giLCJleHAiOjE3NTM4ODcwOTMsImlhdCI6MTc1Mzg2NTQ5M30.dALIn0OqREXv3l4hmMTEcuhNA6VCp5GBZfq36zsPKx3eA3-SQBAnJ7dlUqa6YZX3b0GEPKjbA91pOzvfuphtlVDbAIWTuC2wWVxS8qvyNZg-v2dgEtJpvoIf3F6zS0nG2_V7RUAIA6wNRKobn8y8PgFEqzgZygv2BmllUR55-QwkkhjfXilBWNHCORqofkx5T21W2q2qf1VKTN-U-fy_B25meg4vDvqcGaczUNbGPb8l8b-gPZPUV57iXDC0FrW6KjiLmDXV9txgmRCVXu_1DDvwnTSubhYbW2bU-5jeHNMzQZQud6I0zUDaJpVrVZekmfkyinYb3MTrT6N_OIxGdw",
  "expires_in": 21599,
  "refresh_token": "nXL2bUxDRMGxOsQC3Oldza01EpnU9imhAAAAAgoXEG8AAAGYWoed2FXuKbObXTiX",
  "refresh_token_expires_in": 5183999
}
```

### Response

```json
{
"jwt_token": "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMTgxMzYyNjIyNTc1NjA1MDczOTkiLCJleHAiOjE3NTQyNzUwNTcsImVtYWlsIjoic2hnZ20yMDAwQGdtYWlsLmNvbSIsIm5hbWUiOiLquYDshLjtm4giLCJuaWNrbmFtZSI6bnVsbCwiaXNzIjoic3RlcC10cmFjZSJ9.JenLcjgz8iPxvVZrYxzDZ_PgRFfMvT3Y9k7cg_WBlKSDGrtnGILw5eXQ6WfLqSFOaCChTzd6e0zdZgbKamCB9w"
}
```

---

## 구글 로그인

구글 로그인을 위한 API입니다. 구글 로그인 후 Jwt Token을 발급받습니다.

### **POST** /api/auth/v1/google

### Request

```json
{
  "access_token": "ya29.a0AS3H6NxL4KPHR9xTJ36qRXzCd30TwUCOfmp5ufWpVeIa1EJ0M3yyqW5ooCL-euheKZAamHW9MmFrMU513_veUOktuTHCmIDOvmydRM8tORLXWd7COIqDKMjljUq6bsnhOIuTKutQrVY-HoiKDZ8Ajq4dhjdVpp9kC7e8bB20mAaCgYKARASARYSFQHGX2MiKtTMf9Uob3l66hsJPgQ6QA0177",
  "expires_in": 3598,
  "token_type": "Bearer",
  "id_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6ImRkNTMwMTIwNGZjMWQ2YTBkNjhjNzgzYTM1Y2M5YzEwYjI1ZTFmNGEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0NDExOTYxMDQ0NzktM2txYzloMDhwNjc2Z290ZTVocTR0cnVxaWlqZmRmY28uYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0NDExOTYxMDQ0NzktM2txYzloMDhwNjc2Z290ZTVocTR0cnVxaWlqZmRmY28uYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTgxMzYyNjIyNTc1NjA1MDczOTkiLCJlbWFpbCI6InNoZ2dtMjAwMEBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXRfaGFzaCI6IllkLUFCaGFZa2k4S1llNXY2bkxiN0EiLCJuYW1lIjoi6rmA7IS47ZuIIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hL0FDZzhvY0pWd3kySXpoYnZXaVNZY1VaUmNYbHVRQzBaQkwwOU1UaEJsWXdUZzEwR3d0R1Q0dz1zOTYtYyIsImdpdmVuX25hbWUiOiLshLjtm4giLCJmYW1pbHlfbmFtZSI6Iuq5gCIsImlhdCI6MTc1Mzg2NTQ4OSwiZXhwIjoxNzUzODY5MDg5fQ.PB_JTkSGJqwKljIdAPiHTiipPBthYi6DVsfGJ75Evq8EeOJSBZBCYIBACIOb-UaU6XTWNYsHL5o_KFtXcBFfXQXYZA6M9DTI2JkaGhHbYxi_Dcf835DbkbU-ZwMsiG9QuPIpoVY5_7FPIh5NNRIAdFrpH2sL9agMRuG8NyJMtKimheeJptymYwClMG4OyLSxlAhH2PfArKArILkmAj6ftvA_twfGj8hLLwDKoNFKXTPIoj_y1Cw05c7cH5i0jXI_RwcfUUgBflyZaVDMENE6HiQMahCgzqAw8LCTsggYWw9uL2qxKuVo9lfkgnYg13bMVGFqH6BDXJilUKBIMwoIiA",
  "scope": "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile openid"
}
```

### Response

```json
{
    "jwt_token": "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI0MzYyOTU2MTI0IiwiZXhwIjoxNzU0Mjc1MjQ1LCJlbWFpbCI6bnVsbCwibmFtZSI6bnVsbCwibmlja25hbWUiOiLquYDshLjtm4giLCJpc3MiOiJzdGVwLXRyYWNlIn0.EdVPWOG6eoarKxdkHIyhxjNHsFGSJgwhaFqx9AB40qdXMBbpc40XompcNkZO-Dfz_L5eTF5Oeqt8YgLPSfFaPg"
}

```

---

## 현 위치 주변 맨홀 표시

지도에서 현 위치 주변의 맨홀을 표시하기 위한 API입니다.

### **GET** /api/v1/manholes?lat={latitude}&lng={longitude}&level={level}

- latitude: 위도
- longitude: 경도
- level: 지도 레벨 (1~5)

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

## 처리 중인 맨홀 조회

처리 중인 맨홀의 상세 정보를 조회하기 위한 API입니다.

### **GET** /api/v1/manholes/processing/{id}

- id: 맨홀 ID

### Response

```json
{
  "id": 1,
  "latitude": 37.5665,
  "longitude": 126.978,
  "status": "처리 중",
  "place": "서울특별시 중구",
  "image_urls": [
    "https://example.com/image1.jpg",
    "https://example.com/image2.jpg"
  ],
  "generated_description": [
    "위험도: 상",
    "표면 균열 및 파손 발생",
    "마감재 마모로 인한 내구성 저하",
    "보행자 발목 위험 존재"
  ],
  "created_at": "2023-09-01T12:00:00Z"
}
```

---

## 처리 완료된 맨홀 조회

처리 완료된 맨홀의 상세 정보를 조회하기 위한 API입니다.

### **GET** /api/v1/manholes/completed/{id}h

- id: 맨홀 ID

### Response

```json
{
  "id": 2,
  "latitude": 37.5670,
  "longitude": 126.979,
  "status": "처리완료",
  "place": "서울특별시 중구",
  "before_image_url": [
    "https://example.com/image3.jpg",
    "https://example.com/image4.jpg"
  ],
  "after_image_url": [
    "https://example.com/image5.jpg",
    "https://example.com/image6.jpg"
  ],
  "description": [
    "철제 맨홀 교체 완료하였음."
  ],
  "created_at": "2023-09-02T12:00:00Z"
}
```

---

## 사진 업로드

presigned URL을 받아 업로드를 위한 API입니다.

### **POST** /api/v1/manholes/images

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
    "presigned_url": "https://example.com/presigned-url1"
  },
  {
    "file_name": "IMG_5678.jpg",
    "presigned_url": "https://example.com/presigned-url2"
  },
  {
    "file_name": "IMG_9101.jpg",
    "presigned_url": "https://example.com/presigned-url3"
  }
]
```

---

## 맨홀 등록

맨홀을 등록하기 위한 API입니다.

### **POST** /api/v1/manholes

### Request Body

```json
{
  "imageUrls": [
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

### **GET** /api/my-reports/v1/manholes

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

// 접수 전 페이지도 필요할 것 같습니다.

## 나의 처리 중인 제보 상세 조회

처리 중인 제보의 상세 정보를 조회하기 위한 API입니다.

### **GET** /api/my-reports/v1/manholes/processing/{id}

- id: 제보 ID

### Response

```json
// ai_description 필드는 필요 없나? (기획)

{
  "id": 1,
  "status": "처리 중",
  "title": "뚜껑 흔들리는 맨홀 제보 요청",
  "created_at": "2023-09-01T12:00:00Z",
  "place": "서울특별시 중구",
  "image_url": [
    "https://example.com/image1.jpg",
    "https://example.com/image2.jpg"
  ],
  "user_description": "맨홀 뚜껑이 흔들려 보행 시 위험합니다. 조속한 처리 부탁드립니다."
}
```

---

## 나의 처리 완료된 제보 상세 조회

처리 완료된 제보의 상세 정보를 조회하기 위한 API입니다.

### **GET** /api/my-reports/v1/manholes/completed/{id}

- id: 제보 ID

### Response

```json
{
  "id": 2,
  "status": "완료",
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
  "user_description": "맨홀 뚜껑이 흔들려 보행 시 위험합니다. 조속한 처리 부탁드립니다.",
  "process_agency": "중랑구 도시환경국 공원녹지과",
  "process_description": "맨홀 뚜껑을 교체하였습니다. 안전하게 사용하시기 바랍니다."
}
```

---

## 회원 탈퇴

회원 탈퇴를 위한 API입니다.

### **DELETE** /api/auth/v1/delete

