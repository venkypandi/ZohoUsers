package com.venkatesh.zohousers.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.venkatesh.zohousers.data.local.db.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM RemoteKeys WHERE userId = :userId")
    suspend fun remoteKeysUserId(userId: String): RemoteKeys?

    @Query("DELETE FROM RemoteKeys")
    suspend fun clearRemoteKeys()
}