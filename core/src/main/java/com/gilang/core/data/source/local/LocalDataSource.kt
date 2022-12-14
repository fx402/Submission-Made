package com.gilang.core.data.source.local

import com.gilang.core.data.source.local.entity.UserEntity
import com.gilang.core.data.source.local.room.UserDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val userDao: UserDao){
    fun getFavoriteUser(): Flow<List<UserEntity>> = userDao.getFavoriteUser()

    fun getDetailState(username: String?): Flow<UserEntity>? = userDao.getDetailState(username)

    suspend fun insertUser(userEntity: UserEntity)= userDao.insertUser(userEntity)

    suspend fun deleteUser(userEntity: UserEntity) = userDao.deleteUser(userEntity)
}