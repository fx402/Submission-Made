package com.gilang.githubgilang.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.gilang.core.domain.model.User
import com.gilang.core.domain.usecase.UserUseCase
import kotlinx.coroutines.launch

class DetailViewModel(val userUseCase: UserUseCase): ViewModel() {
    fun getDetail(username: String) = userUseCase.getDetailUser(username).asLiveData()

    fun getDetailState(username: String) = userUseCase.getDetailState(username)?.asLiveData()

    fun insertFavorite(user: User) = viewModelScope.launch {
        userUseCase.insertUser(user)
    }
    fun deleteFavorite(user: User)= viewModelScope.launch {
        userUseCase.deleteUser(user)
    }
}