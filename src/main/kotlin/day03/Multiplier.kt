package day03

object Multiplier {
    private val extractRegExp = Regex("""(mul\(\d+,\d+\))""")

    data class Product(private val s1: Int, private val s2: Int) {
        fun product() = s1 * s2
    }

    fun multiply(input: String): Int {
        val products = extractRegExp.findAll(input)
            .map { it.value }
            .map { it.replace("mul(", "") }
            .map { it.replace(")", "") }
            .map { it.split(",") }
            .map { Product(it[0].toInt(), it[1].toInt()) }

        return products.sumOf { it.product() }
    }
}