package day16

import org.junit.jupiter.api.Test
import util.InputUtils
import kotlin.test.assertEquals

class ReindeerMazeTest {

    @Test
    fun `solve sample maze`() {
        val input = """
          ###############
          #.......#....E#
          #.#.###.#.###.#
          #.....#.#...#.#
          #.###.#####.#.#
          #.#.#.......#.#
          #.#.#####.###.#
          #...........#.#
          ###.#.#####.#.#
          #...#.....#.#.#
          #.#.#.###.#.#.#
          #.....#...#.#.#
          #.###.#.#.#.#.#
          #S..#.....#...#
          ###############
        """.trimIndent()

        val reindeerMaze = ReindeerMaze(InputUtils.parseLines(input))

        assertEquals(7036, reindeerMaze.shortestPathCost())
    }

    @Test
    fun `second example`() {
        val input = """
            #################
            #...#...#...#..E#
            #.#.#.#.#.#.#.#.#
            #.#.#.#...#...#.#
            #.#.#.#.###.#.#.#
            #...#.#.#.....#.#
            #.#.#.#.#.#####.#
            #.#...#.#.#.....#
            #.#.#####.#.###.#
            #.#.#.......#...#
            #.#.###.#####.###
            #.#.#...#.....#.#
            #.#.#.#####.###.#
            #.#.#.........#.#
            #.#.#.#########.#
            #S#.............#
            #################
        """.trimIndent()

        val reindeerMaze = ReindeerMaze(InputUtils.parseLines(input))

        assertEquals(11048, reindeerMaze.shortestPathCost())
    }
}