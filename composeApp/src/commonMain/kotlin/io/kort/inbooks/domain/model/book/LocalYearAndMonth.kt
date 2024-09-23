package io.kort.inbooks.domain.model.book

data class LocalYearAndMonth(
    val year: Int,
    val month: Int,
) : Comparable<LocalYearAndMonth> {
    override fun compareTo(other: LocalYearAndMonth): Int {
        return compareValuesBy(this, other, { it.year }, { it.month })
    }
}