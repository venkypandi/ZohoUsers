package com.venkatesh.zohousers.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKeys(
    @PrimaryKey
    val userId:String,
    val prevKey:Int?,
    val nextKey:Int?
)
