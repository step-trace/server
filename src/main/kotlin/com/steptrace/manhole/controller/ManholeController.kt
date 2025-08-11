package com.steptrace.manhole.controller

import com.steptrace.config.security.CustomUserDetails
import com.steptrace.manhole.dto.CompletedManholeResponse
import com.steptrace.manhole.dto.ManholeRequest
import com.steptrace.manhole.dto.ManholesFromMyReportResponse
import com.steptrace.manhole.dto.ProcessingManholeResponse
import com.steptrace.manhole.dto.ProcessingManholesFromMyReportResponse
import com.steptrace.manhole.dto.CompletedManholesFromMyReportResponse
import com.steptrace.manhole.dto.ManholeMakerResponse
import com.steptrace.manhole.mapper.ManholeMapper.toDto
import com.steptrace.manhole.service.ManholeService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ManholeController(
        private val manholeService: ManholeService
) {

    @GetMapping("/v1/manholes")
    fun manholeMarkers(
            @RequestParam latitude: Double,
            @RequestParam longitude: Double,
    ) : List<ManholeMakerResponse> {
        return manholeService.getManholeMarkers(latitude, longitude).map {
            ManholeMakerResponse.from(it)
        }
    }


    @GetMapping("/v1/manholes/processing/{id}")
    fun processingManhole(
            @PathVariable id: Long
    ) : ProcessingManholeResponse {
        return ProcessingManholeResponse.from(manholeService.getManholeWithAttachment(id))
    }

    @GetMapping("/v1/manholes/completed/{id}")
    fun completedManhole(
            @PathVariable id: Long
    ) : CompletedManholeResponse {
        return CompletedManholeResponse.from(manholeService.getManholeWithAttachment(id))
    }

    @PostMapping("/v1/manholes")
    fun manholes(
            @RequestBody manholeRequest: ManholeRequest,
            @AuthenticationPrincipal userAccount: CustomUserDetails
    ) {
        manholeService.create(toDto(manholeRequest, userAccount.password))
    }

    @PostMapping("/v1/manholes/completed/images/{id}")
    fun completedManholeImages(
            @PathVariable id: Long,
            @RequestBody afterImageUrls: List<String>
    ) {
        manholeService.addCompletedManholeImages(id, afterImageUrls)
    }

    @GetMapping("/v1/manholes/my-reports")
    fun myReportManholes(
            @AuthenticationPrincipal userAccount: CustomUserDetails
    ) : List<ManholesFromMyReportResponse> {
        return manholeService.getManholesFromMyReport(userAccount.password)
                .map { ManholesFromMyReportResponse.from(it) }
    }

    @GetMapping("/v1/manholes/my-reports/processing/{id}")
    fun myReportProcessingManhole(
            @PathVariable id: Long
    ) : ProcessingManholesFromMyReportResponse {
        return ProcessingManholesFromMyReportResponse.from(manholeService.getManholeWithAttachment(id))
    }

    @GetMapping("/v1/manholes/my-reports/completed/{id}")
    fun myReportCompletedManhole(
            @PathVariable id: Long
    ) : CompletedManholesFromMyReportResponse {
        return CompletedManholesFromMyReportResponse.from(manholeService.getManholeWithAttachment(id))
    }
}
