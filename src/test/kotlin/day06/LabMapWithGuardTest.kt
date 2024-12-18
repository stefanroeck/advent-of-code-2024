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

private const val SAMPLE_OBSTACLE_MAP = """
....#.....
.........#
..........
..#.......
.......#..
..........
.#.O^.....
......OO#.
#O.O......
......#O..
"""


class LabMapWithGuardTest {
    @Test
    fun `parse Lab plan`() {
        val labMap = LabWithGuard.buildLabMap(parseLines(SAMPLE_OBSTACLE_MAP))

        with(labMap) {
            assertEquals(10, height)
            assertEquals(10, width)
            assertEquals(MapMarker.EmptyTile, markerAt(Point(0, 0)))
            assertEquals(MapMarker.EmptyTile, markerAt(Point(9, 9)))
            assertEquals(MapMarker.Obstacle, markerAt(Point(4, 0)))
            assertEquals(guardLocation, Point(4, 6))
            assertEquals(guardDirection, Direction.Up)
            assertEquals(MapMarker.LoopTrapObstruction, markerAt(Point(3, 6)))
            assertEquals(MapMarker.LoopTrapObstruction, markerAt(Point(7, 9)))
            assertEquals(Point(4, 6), guardLocation)
        }
    }

    @Test
    fun `send guard on patrol`() {
        val startMap = LabWithGuard.buildLabMap(parseLines(SAMPLE_INPUT_START_MAP))

        val updatedLabMap = LabWithGuard.sendGuardOnPatrol(startMap)

        assertNull(updatedLabMap.guardLocation)
        assertEquals(41, updatedLabMap.visitedLocations().size)
    }

    @Test
    fun `find loop trap obstructions`() {
        val startMap = LabWithGuard.buildLabMap(parseLines(SAMPLE_INPUT_START_MAP))
        val expectedObstacleMap = LabWithGuard.buildLabMap(parseLines(SAMPLE_OBSTACLE_MAP))

        val foundObstructions = LabWithGuard.findLoopTrapObstructions(startMap)

        assertEquals(6, expectedObstacleMap.obstructions().size)
        assertEquals(expectedObstacleMap.obstructions(), foundObstructions)
    }

    @Test
    fun `ensure guard detects loop`() {
        val obstacleMap = LabWithGuard.buildLabMap(parseLines(SAMPLE_OBSTACLE_MAP))

        val loopAtGuardPosition = LabWithGuard.sendGuardUntilEntersLoop(obstacleMap)
        assertEquals(Point(4, 6), loopAtGuardPosition.get())
    }

}