package com.gilang.core.domain.usecase

import com.gilang.core.data.Resource
import com.gilang.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserUseCase {
    fun getAllUsers(query: String? = "dummy"): Flow<Resource<List<User>>>
    fun getAllFollowers(username: String?): Flow<Resource<List<User>>>
    fun getAllFollowing(username: String?): Flow<Resource<List<User>>>
    fun getDetailUser(username: String): Flow<Resource<User>>
    fun getFavoriteUser(): Flow<List<User>>
    fun getDetailState(username: String): Flow<User>?
    suspend fun insertUser(user: User)
    suspend fun deleteUser(user: User): Int
}