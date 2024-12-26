package day12

import org.junit.jupiter.api.Test
import util.InputUtils
import util.MapOfThings.Point
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GardenPlotsTest {

    @Test
    fun `small sample input`() {
        val input = """
         AAAA
         BBCD
         BBCC
         EEEC
        """.trimIndent()

        val gardenPlots = GardenPlots(InputUtils.parseLines(input))
        val regions = gardenPlots.findRegions()

        assertEquals(5, regions.size)
        val regionPlantA = regions.first { it.plant == Plant('A') }
        assertEquals(setOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0)), regionPlantA.points)
        assertEquals(4, regionPlantA.area)
        assertEquals(10, regionPlantA.perimeter)

        val regionPlantD = regions.first { it.plant == Plant('D') }
        assertEquals(setOf(Point(3, 1)), regionPlantD.points)
        assertEquals(1, regionPlantD.area)
        assertEquals(4, regionPlantD.perimeter)

        val price = gardenPlots.calculatePrice()
        assertEquals(140, price)
    }

    @Test
    fun `garden with holes`() {
        val input = """
            OOOOO
            OXOXO
            OOOOO
            OXOXO
            OOOOO
        """.trimIndent()

        val gardenPlots = GardenPlots(InputUtils.parseLines(input))
        val regions = gardenPlots.findRegions()

        assertEquals(5, regions.size)
        assertEquals(1, regions.count { it.plant == Plant('O') })
        assertEquals(4, regions.count { it.plant == Plant('X') })
        assertEquals(36, regions.first { it.plant == Plant('O') }.perimeter)
        assertEquals(772, gardenPlots.calculatePrice())
    }

    @Test
    fun `larger example`() {
        val input = """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE
        """.trimIndent()

        val gardenPlots = GardenPlots(InputUtils.parseLines(input))
        val regions = gardenPlots.findRegions()

        assertEquals(11, regions.size)
        listOf(
            Pair('R', 216),
            Pair('I', 32),
            Pair('C', 392),
            Pair('F', 180),
            Pair('V', 260),
            Pair('J', 220),
            Pair('C', 4),
            Pair('E', 234),
            Pair('I', 308),
            Pair('M', 60),
            Pair('S', 24),
        ).forEach { (letter, price) ->
            assertNotNull(
                regions.firstOrNull { it.plant == Plant(letter) && it.price == price },
                "$letter with price $price not found"
            )
        }
        assertEquals(1930, gardenPlots.calculatePrice())
    }

}