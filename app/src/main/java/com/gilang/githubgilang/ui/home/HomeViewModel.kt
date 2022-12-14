package com.gilang.githubgilang.ui.home

import androidx.lifecycle.*
import com.gilang.githubgilang.core.data.Resource
import com.gilang.githubgilang.domain.model.User
import com.gilang.githubgilang.domain.usecase.UserUseCase

class HomeViewModel(val userUseCase: UserUseCase) : ViewModel() {

    private var username: MutableLiveData<String> = MutableLiveData()

    fun setSearch(query: String){
        if (username.value == query){
            return
        }
        username.value = query
    }

    val users: LiveData<Resource<List<User>>> = Transformations.switchMap(username){
        userUseCase.getAllUsers(it).asLiveData()
    }
}