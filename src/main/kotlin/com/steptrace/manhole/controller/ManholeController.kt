package com.steptrace.manhole.controller

import com.steptrace.config.security.CustomUserDetails
import com.steptrace.manhole.code.ProcessStatus
import com.steptrace.manhole.dto.*
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

    @GetMapping("/v1/manholes/{id}")
    fun processingManhole(
            @PathVariable id: Long
    ) : ManholeResponse {
        val manhole = manholeService.getManholeWithAttachment(id)

        return when(manhole.status) {
            ProcessStatus.PENDING -> PendingManholeResponse.from(manholeService.getManholeWithAttachment(id))
            ProcessStatus.REPORTED -> ReportedManholeResponse.from(manholeService.getManholeWithAttachment(id))
            ProcessStatus.COMPLETED -> CompletedManholeResponse.from(manholeService.getManholeWithAttachment(id))
        }
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

    @GetMapping("/v1/manholes/my-reports/{id}")
    fun myReportProcessingManhole(
            @PathVariable id: Long
    ) : ManholeFromMyReportResponse {
        val manhole = manholeService.getManholeWithAttachment(id)

        return when(manhole.status) {
            ProcessStatus.PENDING -> PendingManholeFromMyReportResponse.from(manhole)
            ProcessStatus.REPORTED -> ReportedManholeFromMyReportResponse.from(manhole)
            ProcessStatus.COMPLETED -> CompletedManholeFromMyReportResponse.from(manhole)
        }
    }

    @GetMapping("/v1/manholes/push/fcm/")
    fun pushFcm(
            @RequestParam latitude: Double,
            @RequestParam longitude: Double,
            @RequestParam token: String
    ) {
        manholeService.pushFcm(latitude, longitude, token)
    }
}
