package com.steptrace.scheduler.service

import com.steptrace.manhole.dto.ManholeDto
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class HtmlReportService {
    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        private val DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    }

    fun generateDailyReport(manholes: List<ManholeDto>, date: LocalDateTime) {
        val fileName = "manhole_report_${date.format(DATE_FORMATTER)}.html"
        val htmlContent = buildHtmlFromTemplate(manholes, date)

        FileWriter(fileName).use { it.write(htmlContent) }
    }

    private fun buildHtmlFromTemplate(manholes: List<ManholeDto>, date: LocalDateTime): String {
        val template = ClassPathResource("templates/manhole-report.html").inputStream.bufferedReader().readText()

        return template
            .replace("{{date}}", date.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")))
            .replace("{{count}}", manholes.size.toString())
            .replace("{{cards}}", manholes.joinToString("") { buildManholeCard(it) })
    }

    private fun buildManholeCard(manhole: ManholeDto): String {
        return """
            <div class="manhole-card">
                <div class="card-header">
                    <h3>${manhole.title}</h3>
                    <span class="status-badge">⏱️ 접수 대기</span>
                </div>
                <div class="card-body">
                    <div class="info-grid">
                        <div class="info-item">
                            <span class="info-label">📍 위치</span>
                            <span class="info-value">${manhole.place}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">📍 좌표</span>
                            <span class="info-value coordinates">${String.format("%.4f", manhole.latitude)}, ${String.format("%.4f", manhole.longitude)}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">📅 신고일시</span>
                            <span class="info-value">${manhole.createdAt?.format(DATETIME_FORMATTER) ?: ""}</span>
                        </div>
                    </div>
                    
                    ${if (manhole.generatedDescription.isNotEmpty()) """
                        <div class="description">
                            <div class="info-label">📝 상세 설명</div>
                            ${manhole.generatedDescription.joinToString("<br>") { "• $it" }}
                        </div>
                    """ else ""}
                    
                    ${if (manhole.beforeImageUrls.isNotEmpty()) """
                        <div class="images-section">
                            <div class="images-title">📸 현장 사진 (${manhole.beforeImageUrls.size}개)</div>
                            <div class="images-grid">
                                ${manhole.beforeImageUrls.mapIndexed { index, url -> 
                                    """<div class="image-item">
                                        <img src="$url" alt="현장 사진 ${index + 1}" loading="lazy">
                                        <div class="image-overlay">사진 ${index + 1}</div>
                                    </div>"""
                                }.joinToString("")}
                            </div>
                        </div>
                    """ else """
                        <div class="no-data">📷 현장 사진이 없습니다</div>
                    """}
                </div>
            </div>
        """.trimIndent()
    }
}