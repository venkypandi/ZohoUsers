package com.venkatesh.zohousers.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.venkatesh.zohousers.data.local.database.UserDatabase
import com.venkatesh.zohousers.data.local.db.RemoteKeys
import com.venkatesh.zohousers.data.remote.api.RandomUserApi
import com.venkatesh.zohousers.data.remote.model.Result
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
    private val query: String,
    private val service: RandomUserApi,
    private val userDatabase: UserDatabase
):RemoteMediator<Int,Result>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Result>): MediatorResult {
        val page:Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }
        try {
            val apiResponse = service.getRandomUsers( page, state.config.pageSize)
            Log.d("usererror", "load: 3")
            val users = apiResponse.results
            val endOfPaginationReached = users.isEmpty()
            userDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    userDatabase.remoteKeysDao().clearRemoteKeys()
                    userDatabase.userDao().clearUsers()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = users.map {
                    RemoteKeys(userId = it.email, prevKey = prevKey, nextKey = nextKey)
                }
                userDatabase.remoteKeysDao().insertAll(keys)
                userDatabase.userDao().insertAll(users)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            Log.d("usererror", "load: 1")
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            Log.d("usererror", "load: 2")
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Result>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { user ->
                // Get the remote keys of the last item retrieved
                userDatabase.remoteKeysDao().remoteKeysUserId(user.email)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Result>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { user ->
                // Get the remote keys of the first items retrieved
                userDatabase.remoteKeysDao().remoteKeysUserId(user.email)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Result>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.email?.let { userId ->
                userDatabase.remoteKeysDao().remoteKeysUserId(userId)
            }
        }
    }
}