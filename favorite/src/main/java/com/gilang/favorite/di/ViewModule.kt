package com.gilang.favorite.di

import com.gilang.favorite.ui.favorite.FavoriteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteModule = module{
    viewModel { FavoriteViewModel(get())}
}