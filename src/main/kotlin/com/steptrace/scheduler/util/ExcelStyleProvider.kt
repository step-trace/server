package com.steptrace.scheduler.util

import org.apache.poi.ss.usermodel.*

class ExcelStyleProvider(private val workbook: Workbook) {

    val headerStyle: CellStyle by lazy {
        workbook.createCellStyle().apply {
            fillForegroundColor = IndexedColors.LIGHT_BLUE.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            setBorderStyle(BorderStyle.THIN)
            setFont(headerFont)
        }
    }

    private val headerFont: Font by lazy {
        workbook.createFont().apply {
            bold = true
            fontHeightInPoints = 12
        }
    }

    private fun CellStyle.setBorderStyle(borderStyle: BorderStyle) {
        borderBottom = borderStyle
        borderTop = borderStyle
        borderLeft = borderStyle
        borderRight = borderStyle
    }
}