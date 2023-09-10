package com.github.mrmitew.bankapp.features.users.repository.internal

import com.github.mrmitew.bankapp.features.users.entity.toEntity
import com.github.mrmitew.bankapp.features.users.repository.LocalUsersRepository
import com.github.mrmitew.bankapp.features.users.vo.User

/**
 * Class that wraps a Room DAO and exposes application-wide (domain) objects.
 * If we migrate from Room, then we won't need to modify the rest of the system, but only
 * create a new mapping in this layer of the app.
 */
class LocalUsersRepositoryImpl(private val userDao: UserDao) :
    LocalUsersRepository {
    private var cachedUser: User? = null

    override suspend fun login(user: User) {
        cachedUser = user
    }

    override suspend fun getLoggedInUser(): User? {
        return cachedUser
    }

    override suspend fun createUser(user: User) {
        userDao.createUser(user.toEntity())
    }

    override suspend fun deleteUserSettings() {
        cachedUser = null
        // TODO: Remove token cache
    }
}
