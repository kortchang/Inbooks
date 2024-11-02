package io.kort.inbooks.common.service.book

object ISBNConverter {
    fun convertTo13(isbn: String): Result<String> = runCatching {
        // 移除可能的分隔符號
        val isbnCleaned = isbn.replace("-", "").replace(" ", "")
        if (isbnCleaned.length == 13) {
            return@runCatching isbnCleaned
        } else if (isbnCleaned.length != 10) { // 驗證輸入是否為有效的 ISBN
            throw IllegalArgumentException("Invalid ISBN format")
        }

        // 構建 ISBN-13 的前綴部分
        val isbn13Prefix = "978" + isbnCleaned.substring(0, 9)

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
        isbn13Prefix + checkDigit
    }
}