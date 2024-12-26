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
        assertEquals(40, regionPlantA.priceWithPerimeter)
        assertEquals(4, regionPlantA.sides)
        assertEquals(16, regionPlantA.priceWithSides)

        val regionPlantC = regions.first { it.plant == Plant('C') }
        assertEquals(4, regionPlantC.area)
        assertEquals(10, regionPlantC.perimeter)
        assertEquals(40, regionPlantC.priceWithPerimeter)
        assertEquals(8, regionPlantC.sides)
        assertEquals(32, regionPlantC.priceWithSides)

        val regionPlantD = regions.first { it.plant == Plant('D') }
        assertEquals(setOf(Point(3, 1)), regionPlantD.points)
        assertEquals(1, regionPlantD.area)
        assertEquals(4, regionPlantD.perimeter)
        assertEquals(4, regionPlantD.priceWithPerimeter)
        assertEquals(4, regionPlantD.sides)
        assertEquals(4, regionPlantD.priceWithSides)

        assertEquals(140, gardenPlots.calculatePriceWithPerimeter())
        assertEquals(80, gardenPlots.calculatePriceWithSides())
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
        assertEquals(772, gardenPlots.calculatePriceWithPerimeter())
        assertEquals(436, gardenPlots.calculatePriceWithSides())
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
                regions.firstOrNull { it.plant == Plant(letter) && it.priceWithPerimeter == price },
                "$letter with price $price not found"
            )
        }
        assertEquals(1930, gardenPlots.calculatePriceWithPerimeter())
        assertEquals(1206, gardenPlots.calculatePriceWithSides())
    }

    @Test
    fun consecutiveNumbers() {
        assertEquals(
            listOf(listOf(1, 2), listOf(4)),
            listOf(1, 2, 4).consecutiveNumbers()
        )
        assertEquals(
            listOf(listOf(1, 2, 3)),
            listOf(3, 2, 1).consecutiveNumbers()
        )
        assertEquals(
            listOf(listOf(1), listOf(3), listOf(5)),
            listOf(5, 1, 3).consecutiveNumbers()
        )
    }

}