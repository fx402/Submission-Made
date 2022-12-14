package com.gilang.githubgilang.domain.usecase

import com.gilang.githubgilang.core.data.Resource
import com.gilang.githubgilang.domain.model.User
import com.gilang.githubgilang.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow

class UserInteractor(private val userRepository: IUserRepository): UserUseCase {
    override fun getAllUsers(query: String?): Flow<Resource<List<User>>> {
        return userRepository.getAllUsers(query)
    }

    override fun getAllFollowers(username: String?): Flow<Resource<List<User>>> {
        return userRepository.getAllFollowers(username)
    }

    override fun getAllFollowing(username: String?): Flow<Resource<List<User>>> {
        return userRepository.getAllFollowing(username)
    }

    override fun getDetailUser(username: String): Flow<Resource<User>> {
        return userRepository.getDetailUser(username)
    }

    override fun getFavoriteUser(): Flow<List<User>> {
        return userRepository.getFavoriteUser()
    }

    override fun getDetailState(username: String): Flow<User>? {
        return userRepository.getDetailState(username)
    }

    override suspend fun insertUser(user: User) {
        return userRepository.insertUser(user)
    }

    override suspend fun deleteUser(user: User): Int {
        return userRepository.deleteUser(user)
    }
}