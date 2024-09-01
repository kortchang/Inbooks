package domain.book

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id: String,
    val source: Source,
    val idBySource: String,
    val isbn13: String?,
    val coverUrl: String?,
    val title: String,
    val subtitle: String?,
    val authors: List<String>?,
    val description: String?,
    val publishedDate: String?,
    val publisher: String?,
    val pageCount: Int?,
    val categories: List<String>?,
) {

    @Serializable
    enum class Source(val serialName: String) {
        @SerialName("google_books")
        GoogleBooks("google_books"),
    }
}

fun isbn10ToIsbn13(isbn10: String): String {
    // 移除可能的分隔符號
    val isbn10Cleaned = isbn10.replace("-", "").replace(" ", "")

    // 驗證輸入是否為有效的 ISBN-10
    if (isbn10Cleaned.length != 10) {
        throw IllegalArgumentException("Invalid ISBN-10 format")
    }

    // 構建 ISBN-13 的前綴部分
    val isbn13Prefix = "978" + isbn10Cleaned.substring(0, 9)

    // 計算檢查碼
    var sum = 0
    for (i in isbn13Prefix.indices) {
        sum += if (i % 2 == 0) {
            isbn13Prefix[i].digitToInt() * 1
        } else {
            isbn13Prefix[i].digitToInt() * 3
        }
    }
    val checkDigit = (10 - (sum % 10)) % 10

    // 返回完整的 ISBN-13
    return isbn13Prefix + checkDigit
}