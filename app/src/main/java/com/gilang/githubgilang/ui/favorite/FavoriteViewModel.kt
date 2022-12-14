package com.gilang.githubgilang.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.gilang.githubgilang.domain.usecase.UserUseCase

class FavoriteViewModel(val userUseCase: UserUseCase) : ViewModel() {


    val getFavoriteUsers = userUseCase.getFavoriteUser().asLiveData()
}