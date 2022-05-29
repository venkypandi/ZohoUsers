package com.venkatesh.zohousers.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.venkatesh.zohousers.data.remote.model.Result
import com.venkatesh.zohousers.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private var _searchResult = MutableLiveData<PagingData<Result>>()
    var searchResult:LiveData<PagingData<Result>> = _searchResult


    val result:LiveData<PagingData<Result>> = repository.getUserList().cachedIn(viewModelScope)

    fun searchUsers(query:String){
       viewModelScope.launch {
           searchResult = repository.searchUsers(query).cachedIn(viewModelScope)
       }
    }

}