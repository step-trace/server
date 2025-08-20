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

/**
 * 일일 맨홀 리포트 스케줄러
 * 
 * 매일 자정에 실행되어 전날 신고된 PENDING 상태의 맨홀들을
 * Excel 및 HTML 형식의 리포트로 생성하고, S3에 업로드한 후
 * 맨홀 상태를 REPORTED로 변경합니다.
 */
@Component
class ManholeReportScheduler(
        private val excelReportService: ExcelReportService,  // Excel 리포트 생성 서비스
        private val htmlReportService: HtmlReportService,    // HTML 리포트 생성 서비스
        private val manholeRepository: ManholeRepository     // 맨홀 데이터 저장소
) {

    /**
     * 일일 맨홀 리포트 생성 대오
     * 
     * 매일 자정(00:00:00)에 실행되는 스케줄러로,
     * 전날 PENDING 상태인 맨홀들에 대한 리포트를 생성하고
     * 맨홀 상태를 REPORTED로 업데이트합니다.
     * 
     * 실행 주기: 매일 자정 (cron: "0 0 0 * * ?")
     */
    @Scheduled(cron = "0 0 0 * * ?")
    fun generateDailyManholeReport() {
        val yesterday = LocalDateTime.now().minusDays(1)  // 어제 날짜 계산

        // 어제 PENDING 상태인 맨홀들 조회
        val manholes = getPendingManholesWithAttachment(yesterday).ifEmpty {
            return  // 어제 신고된 맨홀이 없으면 종료
        }

        // Excel 형식의 일일 리포트 생성 및 S3 업로드
        excelReportService.generateDailyReport(manholes, yesterday)

        // HTML 형식의 일일 리포트 생성 및 S3 업로드
        htmlReportService.generateDailyReport(manholes, yesterday)

        // 맨홀 상태를 PENDING에서 REPORTED로 변경
        modifyManholesStatusToProgress(manholes)
    }

    /**
     * 어제 PENDING 상태인 맨홀들을 첨부파일과 함께 조회합니다.
     * 
     * 어제 하루 동안(00:00:00 ~ 23:59:59) 신고된 PENDING 상태의 맨홀들만
     * 리포트 대상으로 가져옵니다.
     * 
     * @param yesterday 어제 날짜
     * @return PENDING 상태의 맨홀 목록 (첨부파일 포함)
     */
    private fun getPendingManholesWithAttachment(yesterday: LocalDateTime): List<ManholeDto> {
        return manholeRepository.loadPendingManholesWithAttachmentBetween(
                startDateTime = yesterday,           // 어제 00:00:00
                endDateTime = yesterday.plusDays(1)  // 오늘 00:00:00 (어제 23:59:59까지 포함)
        )
    }

    /**
     * 맨홀들의 상태를 REPORTED로 변경합니다.
     * 
     * 리포트 생성이 완료된 맨홀들의 상태를 PENDING에서 REPORTED로 변경하여
     * 리포트가 생성되었음을 표시합니다. 이후 맨홀은 작업자가 수리 완료 시
     * COMPLETED 상태로 업데이트될 수 있습니다.
     * 
     * @param manholes 상태를 변경할 맨홀 목록
     */
    private fun modifyManholesStatusToProgress(manholes: List<ManholeDto>) {
        manholes.map { manhole ->
            // 각 맨홀을 REPORTED 상태로 변경
            manholeRepository.modifyManholeStatus(toEntity(manhole), ProcessStatus.REPORTED)
        }
    }
}
