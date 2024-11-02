package io.kort.inbooks.common.service.book

import io.kort.inbooks.common.model.book.BookRemoteModel
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class BooksUrlAnalyzerTest : FunSpec({
    context("analyze") {
        val booksUrlAnalyzer = BooksUrlAnalyzer()
        val url = "https://www.books.com.tw/products/0010995752?loc=P_br_r0vq68ygz_D_2aabd0_B_2"
        val book = booksUrlAnalyzer.analyze(url)
        val notNullBook = book.shouldNotBeNull()

        test("can analyze") {
            booksUrlAnalyzer.canAnalyze(url).shouldBe(true)
        }

        test("coverUrl") {
            notNullBook.coverUrl shouldBe "https://im1.book.com.tw/image/getImage?i=https://www.books.com.tw/img/001/099/57/0010995752_bc_01.jpg&h=600"
        }

        test("title") {
            notNullBook.title shouldBe "你願意，人生就會值得：蔡康永的情商課3"
        }

        test("authors") {
            notNullBook.authors shouldBe listOf("蔡康永")
        }

        test("publisher") {
            notNullBook.publisher shouldBe "如何"
        }

        test("publishedDate") {
            notNullBook.publishedDate shouldBe "2024/08/01"
        }

        test("pageCount") {
            notNullBook.pageCount shouldBe 288
        }

        test("categories") {
            notNullBook.categories shouldBe listOf("心理勵志")
        }

        test("externalIds") {
            notNullBook.externalIds.shouldContainAll(
                BookRemoteModel.ExternalIdRemoteModel(
                    type = BookRemoteModel.ExternalIdRemoteModel.Type.ISBN13,
                    value = "9789861366968"
                ),
                BookRemoteModel.ExternalIdRemoteModel(
                    type = BookRemoteModel.ExternalIdRemoteModel.Type.BooksUrl,
                    value = "https://www.books.com.tw/products/0010995752"
                )
            )
        }

        test("description") {
            fun String.trimAll() =
                replace(" ", "")
                    .replace("　", "")
                    .replace("\n", "")
                    .replace("\t", "")

            notNullBook.description?.trimAll() shouldBe DescriptionHtml.trimAll()
        }
    }
})

private val DescriptionHtml = """
        你是一個善待自己的人嗎？
    <br>
    　　你有沒有瞧不起你的感受、冷落你的心？
    <br>
    　　太多人活得太費力，我想為大家、包括我自己，找到比較省力、又能活得更舒服、也更滿足的方法。所以我寫了這本書。&nbsp;&nbsp; ──蔡康永<br>
    <br>
    <strong>　　橫掃各大書店暢銷冠軍的《蔡康永的情商課》系列<br>
    　　在眾多讀者期盼下，終於推出三部曲完結篇！</strong><br>
    <br>
    　　康永把情商比喻成陽光、微風、水滴。<br>
    　　陽光就是「明白」，《為你自己活一次》談的是怎麼變得明白；<br>
    　　而微風是「剛剛好」，《因為這是你的人生》談的是找出適合自己、恰如其分的條件，讓我們這個獨一無二的自己活得更自在；<br>
    　　這本《你願意，人生就會值得》，則談情商要像水滴般的累積，而不是一躍過龍門。那些一步登天的美麗傳說，確實會發生在某些人身上，但通常那樣的故事，背後總是藏了不少其他的原因，只是被講得像一步登天而已。<br>
    　　「一步一步的來」，就是加加減減，移動界線，拿捏輕重。<br>
    <br>
    　　拒絕別人對你的無聊要求，減少你對自己的無聊要求。<br>
    <br>
    　　★有什麼好不願意的呢？<br>
    　　－你不需要了不起的故事，你只需要你的故事<br>
    　　★懶很好<br>
    　　－我佩服「更好」，但我喜歡的是「更好過」<br>
    　　★輕輕揉捏成習慣<br>
    　　－不累的小改變，比累死人的大改變，容易發生<br>
    　　★跟自己，一切好商量<br>
    　　－廣告鋪天蓋地，剛好供我們練習眼力<br>
    　　★別當回事，然後自在<br>
    　　－日子是拿來過的，不是拿來換錢的<br>
    &nbsp;
""".trimIndent()