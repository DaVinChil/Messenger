package ru.ns.messenger.db.dao

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(userName: String) {
        withContext(Dispatchers.IO) {
            userDao.insertUser(User(name = userName))
        }
    }

    suspend fun getUser(): User {
        return withContext(Dispatchers.IO) {
            return@withContext userDao.getUser()
        }
    }
}