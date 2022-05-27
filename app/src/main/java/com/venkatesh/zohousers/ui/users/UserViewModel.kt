package com.venkatesh.zohousers.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.venkatesh.zohousers.data.remote.model.Result
import com.venkatesh.zohousers.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

//    private var _searchResult = MutableLiveData<PagingData<Result>>()
    val pagingFlow = repository.searchUsers("Diane")
    .cachedIn(viewModelScope)


    val result:LiveData<PagingData<Result>> = repository.getUserList().cachedIn(viewModelScope)

//    init {
//        viewModelScope.launch {
//            result = repository.getUserList()
//        }
//    }

//    fun searchUsers(query:String){
//        val queryFlow= MutableStateFlow(query)
//        searchResult = repository.searchUsers(query).cachedIn(viewModelScope).combine(queryFlow){
//                data: PagingData<Result>, query: String ->
//            data.filter { it.name.first.startsWith(query) }
//        }


}