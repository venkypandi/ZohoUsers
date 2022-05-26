package com.venkatesh.zohousers.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.venkatesh.zohousers.data.remote.model.Result
import com.venkatesh.zohousers.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

//    private val _result = MutableLiveData<PagingData<Result>>()
    lateinit var result: LiveData<PagingData<Result>>

    init {
        viewModelScope.launch {
            result = repository.getUserList()
        }
    }
}