package com.steptrace.scheduler

import com.steptrace.manhole.code.ProcessStatus
import com.steptrace.manhole.dto.ManholeDto
import com.steptrace.manhole.mapper.ManholeMapper.toEntity
import com.steptrace.manhole.repository.ManholeRepository
import com.steptrace.scheduler.service.ExcelReportService
import com.steptrace.scheduler.service.HtmlReportService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ManholeReportScheduler(
        private val excelReportService: ExcelReportService,
        private val htmlReportService: HtmlReportService,
        private val manholeRepository: ManholeRepository
) {

    @Scheduled(cron = "0 0 0 * * ?")
    fun generateDailyManholeReport() {
        val yesterday = LocalDateTime.now().minusDays(1)

        val manholes = getPendingManholesWithAttachment(yesterday).ifEmpty {
            return
        }

        excelReportService.generateDailyReport(manholes, yesterday)

        htmlReportService.generateDailyReport(manholes, yesterday)

        modifyManholesStatusToProgress(manholes)
    }

    private fun getPendingManholesWithAttachment(yesterday: LocalDateTime): List<ManholeDto> {
        return manholeRepository.loadPendingManholesWithAttachmentBetween(
                startDateTime = yesterday,
                endDateTime = yesterday.plusDays(1)
        )
    }

    private fun modifyManholesStatusToProgress(manholes: List<ManholeDto>) {
        manholes.map { manhole ->
            manholeRepository.modifyManholeStatus(toEntity(manhole), ProcessStatus.REPORTED)
        }
    }
}
