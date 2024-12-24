package day10

import org.junit.jupiter.api.Test
import util.InputUtils
import util.MapOfThings.Point
import kotlin.test.assertEquals

class HikingTrailsTest {

    @Test
    fun trivialMap() {
        val input = """
           0123
           1234
           8765
           9876""".trimIndent()

        val hikingTrails = HikingTrails(InputUtils.parseLines(input))
        val map = hikingTrails.parseMap()

        assertEquals(4, map.width)
        assertEquals(4, map.height)
        assertEquals(Height(0), map.thingAt(Point(0, 0)))
        assertEquals(Height(6), map.thingAt(Point(3, 3)))
        assertEquals(setOf(Point(0, 0)), hikingTrails.trailHeads())
        assertEquals(1, hikingTrails.score())
    }

    @Test
    fun hikingTrailsForSample() {
        val input = """
         ...0...
         ...1...
         ...2...
         6543456
         7.....7
         8.....8
         9.....9
        """.trimIndent()

        val hikingTrails = HikingTrails(InputUtils.parseLines(input))
        assertEquals(2, hikingTrails.score())
    }

    @Test
    fun hikingTrailsForSampleWithTwoTrailHeads() {
        val input = """
           10..9..
           2...8..
           3...7..
           4567654
           ...8..3
           ...9..2
           .....01
        """.trimIndent()

        val hikingTrails = HikingTrails(InputUtils.parseLines(input))
        assertEquals(setOf(Point(1, 0), Point(5, 6)), hikingTrails.trailHeads())
        assertEquals(3, hikingTrails.score())
    }

    @Test
    fun hikingTrailsForComplexSample() {
        val input = """
           89010123
           78121874
           87430965
           96549874
           45678903
           32019012
           01329801
           10456732
        """.trimIndent()

        val hikingTrails = HikingTrails(InputUtils.parseLines(input))
        assertEquals(9, hikingTrails.trailHeads().size)
        assertEquals(36, hikingTrails.score())
    }

}