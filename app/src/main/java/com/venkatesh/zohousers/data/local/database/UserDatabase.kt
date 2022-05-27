package com.venkatesh.zohousers.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.venkatesh.zohousers.data.local.dao.RemoteKeysDao
import com.venkatesh.zohousers.data.local.dao.UserDao
import com.venkatesh.zohousers.data.local.db.RemoteKeys
import com.venkatesh.zohousers.data.remote.model.Result

@Database(entities = [Result::class,RemoteKeys::class], version = 4, exportSchema = false)
abstract class UserDatabase:RoomDatabase() {

    abstract fun userDao():UserDao

    abstract fun remoteKeysDao(): RemoteKeysDao
}