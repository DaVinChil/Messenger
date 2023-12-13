package ru.ns.messenger.db.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_name")
data class User(@PrimaryKey(autoGenerate = true) val id: Long? = null, val name: String)