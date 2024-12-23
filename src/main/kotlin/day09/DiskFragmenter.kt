package day09


sealed interface FileMapEntry {
    fun text(): String
}

data class File(val index: Int) : FileMapEntry {
    override fun text() = index.toString()
}

data object Empty : FileMapEntry {
    override fun text() = "."
}

object DiskFragmenter {
    fun createMap(input: String): List<FileMapEntry> {
        return input.mapIndexed { index, char ->
            val number = char.digitToInt()
            if (index % 2 == 1) {
                MutableList(number) { Empty }.toList()
            } else {
                MutableList(number) { File(index / 2) }
            }
        }.flatten()
    }

    fun checksum(diskMap: List<FileMapEntry>): Long {
        return diskMap.mapIndexed { index, element ->
            when (element) {
                Empty -> 0L
                is File -> index.toLong() * element.index
            }
        }.sum()
    }

    fun defrag(diskMap: List<FileMapEntry>): List<FileMapEntry> {
        val result = mutableListOf<FileMapEntry>()
        val fragmentedElements = diskMap.count { it != Empty }

        val reversedNumbersToFillFreeBlocks = diskMap.reversed().filter { it != Empty }
        var fillingCursor = 0

        for (currentIndex in diskMap.indices) {
            val element = diskMap[currentIndex]
            if (element == Empty) {
                result.add(reversedNumbersToFillFreeBlocks[fillingCursor++])
            } else {
                result.add(element)
            }
            
            if (result.size == fragmentedElements) {
                break
            }
        }

        result.addAll(MutableList(diskMap.size - result.size) { Empty })
        return result.toList()
    }

    fun createMapDefragAndReturnChecksum(input: String): Long = checksum(defrag(createMap(input)))
}