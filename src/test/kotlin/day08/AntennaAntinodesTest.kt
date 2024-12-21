package day08

import day08.MapElement.Antenna
import org.junit.jupiter.api.Test
import util.InputUtils.parseLines
import kotlin.test.assertEquals

private const val SAMPLE_INPUT = """
......#....#
...#....0...
....#0....#.
..#....0....
....0....#..
.#....A.....
...#........
#......#....
........A...
.........A..
..........#.
..........#.
"""

class AntennaAntinodesTest {

    @Test
    fun `antinodes for sample`() {
        // replace antinodes from sample as there're just for illustration
        val antennaAntinodes = AntennaAntinodes(parseLines(SAMPLE_INPUT.replace("#", ".")))

        assertEquals(setOf(Antenna('A'), Antenna('0')), antennaAntinodes.distinctAntennaTypes())
        assertEquals(14, antennaAntinodes.countAntinodes())
    }

    @Test
    fun `resonant harmonic antinodes for sample`() {
        // replace antinodes from sample as there're just for illustration
        val antennaAntinodes = AntennaAntinodes(parseLines(SAMPLE_INPUT.replace("#", ".")))

        assertEquals(34, antennaAntinodes.countAntinodesWithResonantHarmonics())
    }

}