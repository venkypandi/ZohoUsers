package com.venkatesh.zohousers.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.venkatesh.zohousers.data.remote.model.Result

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<Result>)

    @Query("SELECT * FROM users WHERE first LIKE :queryString OR last LIKE :queryString")
    fun usersByName(queryString: String): PagingSource<Int, Result>

    @Query("SELECT * FROM users")
    fun getAllUsers(): PagingSource<Int, Result>

    @Query("DELETE FROM users")
    suspend fun clearUsers()

    @Query("SELECT * FROM users WHERE email = :email")
    fun getUserByEmail(email:String):Result
}