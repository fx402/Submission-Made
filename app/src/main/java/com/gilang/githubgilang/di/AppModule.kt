package com.gilang.githubgilang.di

import com.gilang.core.domain.usecase.UserInteractor
import com.gilang.core.domain.usecase.UserUseCase
import com.gilang.githubgilang.ui.detail.DetailViewModel
import com.gilang.githubgilang.ui.follow.FollowViewModel
import com.gilang.githubgilang.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory <UserUseCase>{ UserInteractor(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get())}
    viewModel { DetailViewModel(get())}
    viewModel { FollowViewModel(get())}
}