package com.steptrace.scheduler.code

import com.steptrace.manhole.dto.ManholeDto

enum class ManholeExcelColumn(
        val headerName: String,
        val getValue: (ManholeDto) -> Any?
) {
    LATITUDE("위도", { it.latitude }),
    LONGITUDE("경도", { it.longitude }),
    PLACE("장소", { it.place }),
    DESCRIPTION("설명", { it.generatedDescription.joinToString(", ") }),
    BEFORE_IMAGES_URLS("이미지URL", { it.beforeImageUrls.joinToString("\n") }),
    CREATED_AT("생성일시", { it.createdAt })
}