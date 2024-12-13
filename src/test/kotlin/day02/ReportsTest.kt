package day02

import day02.Reports.Report
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

const val SAMPLE_INPUT = """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9   
            """

class ReportsTest {

    @Test
    fun `extract reports from sample`() {
        val reports = Reports.extractReports(SAMPLE_INPUT.trimIndent())

        assertEquals(6, reports.size)
        assertEquals(Report(listOf(7, 6, 4, 2, 1)), reports[0])
    }

    @Test
    fun `decreasing report`() {
        val decreasingReport = Report(listOf(7, 6, 4, 2, 1))

        assertTrue(decreasingReport.isDecreasing())
        assertFalse(decreasingReport.isIncreasing())
    }

    @Test
    fun `stable reports`() {
        assertTrue(Report(listOf(7, 6, 4, 2, 1)).isStable())
        assertTrue(Report(listOf(1, 3, 6, 7, 9)).isStable())

        assertFalse(Report(listOf(1, 3, 3, 7, 9)).isStable())
    }

    @Test
    fun `neither in- nor decreasing report`() {
        val decreasingReport = Report(listOf(7, 6, 4, 5, 1))

        assertFalse(decreasingReport.isDecreasing())
        assertFalse(decreasingReport.isIncreasing())
    }

    @Test
    fun `safe reports with sample`() {
        val count: Int = Reports.countSafeReports(SAMPLE_INPUT.trimIndent())

        assertEquals(2, count)
    }
}