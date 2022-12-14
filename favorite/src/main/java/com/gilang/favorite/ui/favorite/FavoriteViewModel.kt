package com.gilang.favorite.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.gilang.core.domain.usecase.UserUseCase

class FavoriteViewModel(val userUseCase: UserUseCase) : ViewModel() {


    val getFavoriteUsers = userUseCase.getFavoriteUser().asLiveData()
}