package day06

import org.junit.jupiter.api.Test
import util.InputUtils.parseLines
import kotlin.test.assertEquals
import kotlin.test.assertNull

private const val SAMPLE_INPUT_START_MAP = """
....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...
"""

private const val SAMPLE_INPUT_END_MAP = """
....#.....
....XXXXX#
....X...X.
..#.X...X.
..XXXXX#X.
..X.X.X.X.
.#XXXXXXX.
.XXXXXXX#.
#XXXXXXX..
......#X..    
"""

class LabMapWithGuardTest {
    @Test
    fun `parse Lab plan`() {
        val labMap = LabWithGuard.buildLabMap(parseLines(SAMPLE_INPUT_START_MAP))

        with(labMap) {
            assertEquals(10, height)
            assertEquals(10, width)
            assertEquals(MapMarker.EmptyTile, markerAt(Point(0, 0)))
            assertEquals(MapMarker.EmptyTile, markerAt(Point(9, 9)))
            assertEquals(MapMarker.Obstacle, markerAt(Point(4, 0)))
            assertEquals(MapMarker.GuardUp, markerAt(Point(4, 6)))
            assertEquals(Point(4, 6), guardLocation())
        }
    }

    @Test
    fun `send guard on patrol`() {
        val startMap = LabWithGuard.buildLabMap(parseLines(SAMPLE_INPUT_START_MAP))
        val endMap = LabWithGuard.buildLabMap(parseLines(SAMPLE_INPUT_END_MAP))

        val updatedLabMap = startMap.sendGuardOnPatrol()

        assertNull(updatedLabMap.guardLocation())
        assertEquals(41, updatedLabMap.visitedLocations())
        assertEquals(endMap, updatedLabMap)
    }

}