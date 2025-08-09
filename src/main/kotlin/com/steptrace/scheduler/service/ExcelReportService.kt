package com.steptrace.scheduler.service

import com.steptrace.exception.ExcelGenerationException
import com.steptrace.manhole.dto.ManholeDto
import com.steptrace.scheduler.code.ManholeExcelColumn
import com.steptrace.scheduler.util.ExcelStyleProvider
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class ExcelReportService(
    private val s3Service: S3Service
) {

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        private val DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    }

    fun generateDailyReport(manholes: List<ManholeDto>, date: LocalDateTime) {
        val fileName = generateFileName(date)
        val excelData = generateExcelData(manholes)

        s3Service.uploadExcelFile(fileName, excelData, date.toLocalDate())
    }

    private fun generateFileName(date: LocalDateTime): String {
        return "manhole_report_${date.format(DATE_FORMATTER)}.xlsx"
    }

    private fun generateExcelData(manholes: List<ManholeDto>): ByteArray {
        return try {
            XSSFWorkbook().use { workbook ->
                val sheet = workbook.createSheet("맨홀 리포트")
                val styleProvider = ExcelStyleProvider(workbook)

                createHeader(sheet, styleProvider)
                createDataRows(sheet, manholes)
                autoSizeColumns(sheet)

                ByteArrayOutputStream().use { outputStream ->
                    workbook.write(outputStream)
                    outputStream.toByteArray()
                }
            }
        } catch (e: Exception) {
            throw ExcelGenerationException()
        }
    }

    private fun createHeader(sheet: Sheet, styleProvider: ExcelStyleProvider) {
        val headerRow = sheet.createRow(0)
        val columns = ManholeExcelColumn.values()

        columns.forEachIndexed { index, column ->
            val cell = headerRow.createCell(index)
            cell.setCellValue(column.headerName)
            cell.cellStyle = styleProvider.headerStyle
        }
    }

    private fun createDataRows(sheet: Sheet, manholes: List<ManholeDto>) {
        manholes.forEachIndexed { index, manhole ->
            val row = sheet.createRow(index + 1)
            ManholeExcelColumn.values().forEachIndexed { colIndex, column ->
                val cell = row.createCell(colIndex)
                setCellValue(cell, column.getValue(manhole))
            }
        }
    }

    private fun setCellValue(cell: Cell, value: Any?) {
        when (value) {
            is String -> cell.setCellValue(value)
            is Double -> cell.setCellValue(value)
            is Int -> cell.setCellValue(value.toDouble())
            is LocalDateTime -> cell.setCellValue(value.format(DATETIME_FORMATTER))
        }
    }

    private fun autoSizeColumns(sheet: Sheet) {
        repeat(ManholeExcelColumn.values().size) { columnIndex ->
            sheet.autoSizeColumn(columnIndex)
        }
    }
}