package day04

import org.junit.jupiter.api.Test
import util.MapOfThings.Direction
import util.MapOfThings.Point
import kotlin.test.assertEquals

private const val SMALL_SAMPLE = """
            ..X...
            .SAMX.
            .A..A.
            XMAS.S
            .X....  
"""

private const val BIGGER_SAMPLE = """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX
"""

class WordSearchTest {

    @Test
    fun `search xmas in small sample`() {
        val count: Int = WordSearch.count(SMALL_SAMPLE.trimIndent(), "XMAS")

        assertEquals(4, count)
    }

    @Test
    fun `search xmas in bigger sample`() {
        val count: Int = WordSearch.count(BIGGER_SAMPLE.trimIndent(), "XMAS")

        assertEquals(18, count)
    }

    @Test
    fun `parse small input`() {
        val puzzle = WordSearch.parse(SMALL_SAMPLE)

        assertEquals(30, puzzle.pointCount())
        assertEquals(6, puzzle.width)
        assertEquals(5, puzzle.height)
        assertEquals('.', puzzle.thingAt(Point(0, 0)))
        assertEquals('X', puzzle.thingAt(Point(2, 0)))
        assertEquals('A', puzzle.thingAt(Point(1, 2)))
        assertEquals('.', puzzle.thingAt(Point(5, 4)))
    }

    @Test
    fun `points between horizontally`() {
        assertEquals(
            listOf(
                Point(0, 0),
                Point(1, 0),
                Point(2, 0),
                Point(3, 0),
            ), Point.pointsBetween(Point(0, 0), Point(3, 0))
        )

        assertEquals(
            listOf(
                Point(3, 0),
                Point(2, 0),
                Point(1, 0),
                Point(0, 0),
            ), Point.pointsBetween(Point(3, 0), Point(0, 0))
        )
    }

    @Test
    fun `points between vertically`() {
        assertEquals(
            listOf(
                Point(1, 1),
                Point(1, 2),
                Point(1, 3),
                Point(1, 4),
            ), Point.pointsBetween(Point(1, 1), Point(1, 4))
        )

        assertEquals(
            listOf(
                Point(1, 4),
                Point(1, 3),
                Point(1, 2),
                Point(1, 1),
            ), Point.pointsBetween(Point(1, 4), Point(1, 1))
        )
    }

    @Test
    fun `points between diagonal left to right`() {
        assertEquals(
            listOf(
                Point(1, 2),
                Point(2, 3),
                Point(3, 4),
            ), Point.pointsBetween(Point(1, 2), Point(3, 4))
        )

        assertEquals(
            listOf(
                Point(3, 4),
                Point(2, 3),
                Point(1, 2),
            ), Point.pointsBetween(Point(3, 4), Point(1, 2))
        )
    }

    @Test
    fun `points between diagonal right to left`() {
        assertEquals(
            listOf(
                Point(5, 4),
                Point(4, 3),
                Point(3, 2),
            ), Point.pointsBetween(Point(5, 4), Point(3, 2))
        )

        assertEquals(
            listOf(
                Point(3, 2),
                Point(4, 3),
                Point(5, 4),
            ), Point.pointsBetween(Point(3, 2), Point(5, 4))
        )
    }

    @Test
    fun `search most simple x-mas`() {
        val input = """
            M.S
            .A.
            M.S
        """.trimIndent()

        val count = WordSearch.countXmasPattern(input)

        assertEquals(1, count)
    }

    @Test
    fun `search most sample x-mas`() {
        val input = """
            .M.S......
            ..A..MSMS.
            .M.S.MAA..
            ..A.ASMSM.
            .M.S.M....
            ..........
            S.S.S.S.S.
            .A.A.A.A..
            M.M.M.M.M.
            ..........
        """.trimIndent()

        val count = WordSearch.countXmasPattern(input)

        assertEquals(9, count)
    }

    @Test
    fun `translate Points`() {
        assertEquals(Point(2, 1), Point(1, 1).translate(1, Direction.Right))
        assertEquals(Point(0, 1), Point(1, 1).translate(1, Direction.Left))
        assertEquals(Point(1, 0), Point(1, 1).translate(1, Direction.Up))
        assertEquals(Point(1, 2), Point(1, 1).translate(1, Direction.Down))

        assertEquals(Point(2, 0), Point(1, 1).translate(1, Direction.TopRight))
        assertEquals(Point(2, 2), Point(1, 1).translate(1, Direction.BottomRight))
        assertEquals(Point(0, 2), Point(1, 1).translate(1, Direction.BottomLeft))
        assertEquals(Point(0, 0), Point(1, 1).translate(1, Direction.TopLeft))
    }

}