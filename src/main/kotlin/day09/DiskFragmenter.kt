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

    sealed class Block(open val length: Int)
    data class FileBlock(val index: Int, override val length: Int) : Block(length)
    data class EmptyBlock(override val length: Int) : Block(length)

    fun defragWholeFilesOnly(diskmap: List<FileMapEntry>): List<FileMapEntry> {
        val blocks = mapBlocks(diskmap)
        val reversedBlocksToFillFreeBlocks = blocks.reversed().filterNot { it is EmptyBlock }
        val defragmentedBlocks = blocks.toMutableList() // copy list

        reversedBlocksToFillFreeBlocks.forEach { block ->
            val emptyBlock =
                defragmentedBlocks.filterIsInstance<EmptyBlock>()
                    .firstOrNull { emptyBlock -> emptyBlock.length >= block.length }
            if (emptyBlock != null) {
                // remove from old position by replacing with empty
                val oldIndex = defragmentedBlocks.indexOf(block)
                defragmentedBlocks.set(oldIndex, EmptyBlock(block.length))

                // insert at new position
                val remainingSpace = emptyBlock.length - block.length
                val newIndex = defragmentedBlocks.indexOf(emptyBlock)
                defragmentedBlocks.set(newIndex, block)
                if (remainingSpace > 0) {
                    defragmentedBlocks.add(newIndex + 1, EmptyBlock(remainingSpace))
                }
            }
        }

        val result = mutableListOf<FileMapEntry>()
        defragmentedBlocks.forEach { block ->
            val entry = when (block) {
                is EmptyBlock -> Empty
                is FileBlock -> File(block.index)
            }
            IntRange(0, block.length - 1).forEach { _ -> result.add(entry) }
        }

        return result.toList()
    }

    private fun mapBlocks(diskmap: List<FileMapEntry>): MutableList<Block> {
        val blocks = mutableListOf<Block>()
        var currentEntryWithStartIndex: Pair<FileMapEntry, Int> = Pair(diskmap.first(), 0)
        diskmap.mapIndexed { index, entry ->
            val (currentEntry, startIndex) = currentEntryWithStartIndex
            val isLastEntry = index == diskmap.indices.last
            if (entry != currentEntry || isLastEntry) {
                val block = when (currentEntry) {
                    Empty -> EmptyBlock(index - startIndex)
                    is File -> FileBlock(currentEntry.index, index - startIndex + if (isLastEntry) 1 else 0)
                }
                blocks.add(block)
                currentEntryWithStartIndex = Pair(entry, index)
            }
        }
        return blocks
    }

    fun createMapDefragAndReturnChecksum(input: String): Long = checksum(defrag(createMap(input)))

    fun createMapDefragAndReturnChecksumWholeFiles(input: String): Long =
        checksum(defragWholeFilesOnly(createMap(input)))
}