package io.kort.inbooks.ui.screen.app

import androidx.compose.ui.graphics.vector.ImageVector
import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.domain.model.book.BookId
import io.kort.inbooks.ui.resource.Book
import io.kort.inbooks.ui.resource.BookStack
import io.kort.inbooks.ui.resource.HomeSimpleDoor
import io.kort.inbooks.ui.resource.Icons
import io.kort.inbooks.ui.resource.Search
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
sealed interface Screen {
    @Serializable
    data object Onboarding : Screen

    @Serializable
    data object Dashboard : Screen

    @Serializable
    data object Search : Screen

    @Serializable
    data object BookList : Screen

    @Serializable
    data object TopicList : Screen

    @Serializable
    class SearchedBookDetail private constructor(
        private val externalIdsRawJson: String,
    ) : Screen {
        constructor(externalIds: List<Book.ExternalId>) : this(Json.encodeToString(externalIds))

        val externalIds: List<Book.ExternalId> by lazy { Json.decodeFromString(externalIdsRawJson) }
    }

    @Serializable
    data class CollectedBookDetail(
        val bookId: BookId,
    ) : Screen

    @Serializable
    data class AddOrEditTopic(val topicIdForEdit: String?) : Screen {
        @Serializable
        data object Main : Screen

        @Serializable
        data object SelectBook : Screen
    }

    @Serializable
    data class TopicDetail(
        val topicId: String,
    ) : Screen

    companion object {
        val MajorScreens: List<Pair<Screen, ImageVector>> =
            listOf(
                Dashboard to Icons.HomeSimpleDoor,
                Search to Icons.Search,
                BookList to Icons.Book,
                TopicList to Icons.BookStack,
            )
    }


    @Serializable
    data object SignUp : Screen {
        @Serializable
        data object Email : Screen
    }

    @Serializable
    data object Login : Screen {
        @Serializable
        data object Email : Screen

        @Serializable
        data class VerifyEmail(val email: String) : Screen

        @Serializable
        data object Success : Screen
    }

    @Serializable
    data object Settings : Screen
}