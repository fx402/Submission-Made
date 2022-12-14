package com.gilang.githubgilang.ui.follow

import androidx.lifecycle.*
import com.gilang.githubgilang.core.data.Resource
import com.gilang.githubgilang.domain.model.User
import com.gilang.githubgilang.domain.usecase.UserUseCase

class FollowViewModel(private val userUseCase: UserUseCase): ViewModel() {
    private var username: MutableLiveData<String> = MutableLiveData()
    private lateinit var typeView: TypeView
    enum class TypeView{
        FOLLOWER,
        FOLLOWING
    }

    fun setFollow(userName: String, type: TypeView){
        if (username.value == userName){
            return
        }
        username.value = userName
        typeView = type
    }

    val favoriteUsers: LiveData<Resource<List<User>>> = Transformations.switchMap(username){
        when(typeView){
            TypeView.FOLLOWING -> userUseCase.getAllFollowing(it).asLiveData()
            TypeView.FOLLOWER -> userUseCase.getAllFollowers(it).asLiveData()
        }
    }
}