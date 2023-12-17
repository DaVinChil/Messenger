package ru.ns.messenger.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("select * from user_name limit 1")
    fun getUser(): User

    @Insert(entity = User::class)
    fun insertUser(user: User)
}