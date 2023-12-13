package ru.ns.messenger.db.dao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database (
    version = 1,
    entities = [
        User::class
    ]
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
}