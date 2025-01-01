package day15

import day15.MapType.WIDE
import org.junit.jupiter.api.Test
import util.InputUtils
import kotlin.test.assertEquals

class WarehouseTest {

    @Test
    fun `simple warehouse robot`() {
        val input = """
         ########
         #..O.O.#
         ##@.O..#
         #...O..#
         #.#.O..#
         #...O..#
         #......#
         ########

         <^^>>>vv<v>>v<<
        """.trimIndent()

        val warehouse = Warehouse(InputUtils.parseLines(input))

        val gpsCoordinateSum = warehouse.walkAroundAndReturnSumOfGpsCoordinate()
        assertEquals(2028, gpsCoordinateSum)
    }

    @Test
    fun `larger sample warehouse robot`() {
        val input = """
            ##########
            #..O..O.O#
            #......O.#
            #.OO..O.O#
            #..O@..O.#
            #O#..O...#
            #O..O..O.#
            #.OO.O.OO#
            #....O...#
            ##########

            <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
            vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
            ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
            <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
            ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
            ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
            >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
            <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
            ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
            v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
        """.trimIndent()

        val warehouse = Warehouse(InputUtils.parseLines(input))

        val gpsCoordinateSum = warehouse.walkAroundAndReturnSumOfGpsCoordinate()
        assertEquals(10092, gpsCoordinateSum)
    }

    @Test
    fun `enlarge simple warehouse to wider version`() {
        val input = """
            #######
            #...#.#
            #.....#
            #..OO@#
            #..O..#
            #.....#
            #######
        """.trimIndent()

        val expectedOutput = """
            ##############
            ##......##..##
            ##..........##
            ##....[][]@.##
            ##....[]....##
            ##..........##
            ##############
        """.trimIndent()

        val warehouse = Warehouse(InputUtils.parseLines(input), mapType = WIDE)

        val widerVersion: String = warehouse.mapAsString()
        assertEquals(expectedOutput, widerVersion)
    }

}