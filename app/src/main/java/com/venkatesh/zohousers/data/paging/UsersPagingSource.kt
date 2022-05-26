package com.venkatesh.zohousers.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.venkatesh.zohousers.data.remote.api.RandomUserApi
import com.venkatesh.zohousers.data.remote.model.Result

class UsersPagingSource(private val randomUserApi: RandomUserApi):PagingSource<Int,Result>() {
    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val position = params.key ?: 1
            val response = randomUserApi.getRandomUsers(position,5)
            Log.d("userresponse", response.toString())

            return LoadResult.Page(
                data = response.results,
                prevKey = if(position == 1) null else position.minus(1),
                nextKey = if(position == 100) null else position.plus(1)
            )

        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}