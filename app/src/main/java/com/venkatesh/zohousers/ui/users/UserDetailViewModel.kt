package com.venkatesh.zohousers.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venkatesh.zohousers.data.remote.model.Result
import com.venkatesh.zohousers.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private var _userDetails = MutableLiveData<Result>()
    val userDetails:LiveData<Result> = _userDetails

    fun getUserByEmail(email:String){
        viewModelScope.launch {
            _userDetails.value =  repository.getUserByEmail(email)
        }

    }
}