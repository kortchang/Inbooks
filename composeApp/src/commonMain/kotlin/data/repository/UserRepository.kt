package data.repository

import com.benasher44.uuid.uuid4
import domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface UserRepository {
    fun get(): Flow<User>
}

class MockUserRepository : UserRepository {
    override fun get(): Flow<User> = flowOf(
        User(
            id = uuid4().toString(),
            avatarUrl = """https://avatars.githubusercontent.com/u/30483921?v=4""",
            displayName = "Kort Chang"
        )
    )
}