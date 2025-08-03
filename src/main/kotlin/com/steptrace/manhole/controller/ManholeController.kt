package com.steptrace.manhole.controller

import com.steptrace.config.security.CustomUserDetails
import com.steptrace.manhole.dto.ManholeRequest
import com.steptrace.manhole.dto.ProcessingManholeResponse
import com.steptrace.manhole.mapper.ManholeMapper.toDto
import com.steptrace.manhole.service.ManholeService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ManholeController(
        private val manholeService: ManholeService
) {

    @GetMapping("/v1/manholes/processing/{id}")
    fun processingManhole(
            @PathVariable id: Long
    ) : ProcessingManholeResponse {
        return ProcessingManholeResponse.from(manholeService.getProcessingManhole(id))
    }


    @PostMapping("/v1/manholes")
    fun manholes(
            @RequestBody manholeRequest: ManholeRequest,
            @AuthenticationPrincipal userAccount: CustomUserDetails
    ) {
        manholeService.create(toDto(manholeRequest, userAccount.password))
    }
}