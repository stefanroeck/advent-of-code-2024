package day09

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test
import kotlin.test.assertEquals

class DiskFragmenterTest {

    @ParameterizedTest
    @CsvSource(
        ignoreLeadingAndTrailingWhitespace = true,
        textBlock = """
         '12345',                 '0..111....22222',
         '12345',                 '0..111....22222',
         '111111111111111111111', '0.1.2.3.4.5.6.7.8.9.10',
         '90909',                 '000000000111111111222222222',
         '2333133121414131402',   '00...111...2...333.44.5555.6666.777.888899'"""
    )
    fun `parse simple disk map`(input: String, expected: String) {
        val result: List<FileMapEntry> = DiskFragmenter.createMap(input)

        assertEquals(expected, result.joinToString("") { it.text() })
    }

    @Test
    fun `checksum for block`() {
        val degfragmentedBlock = "0099811188827773336446555566.............."

        assertEquals(1928, DiskFragmenter.checksum(parseDiskmap(degfragmentedBlock)))
    }

    @ParameterizedTest
    @CsvSource(
        ignoreLeadingAndTrailingWhitespace = true, textBlock = """
        '0..111....22222',                            '022111222......',
        '00...111...2...333.44.5555.6666.777.888899', '0099811188827773336446555566..............',
        '012',                                        '012',
        '0.1.2',                                      '021..',
        '01...',                                      '01...',
        '0.1..',                                      '01...',
        '0..1.',                                      '01...',
        '0...1',                                      '01...',
        '1.2345',                                     '15234.',
        '12.345',                                     '12534.',
        '123.45',                                     '12354.',
        '1234.5',                                     '12345.'
        '12345.',                                     '12345.'"""
    )
    fun `defrag disk map`(diskMap: String, expected: String) {
        assertEquals(parseDiskmap(expected), DiskFragmenter.defrag(parseDiskmap(diskMap)))
    }

    @Test
    fun `create disk map, degrag and return checksum`() {
        val input = "2333133121414131402"

        assertEquals(1928, DiskFragmenter.createMapDefragAndReturnChecksum(input))
    }

    private fun parseDiskmap(diskMap: String) =
        diskMap.map { if (it == '.') Empty else File(it.digitToInt()) }
}