package com.gilang.core.data

import com.gilang.core.data.source.local.LocalDataSource
import com.gilang.core.data.source.remote.RemoteDataSource
import com.gilang.core.data.source.remote.network.ApiResponse
import com.gilang.core.data.source.remote.response.ListUserResponse
import com.gilang.core.domain.model.User
import com.gilang.core.domain.repository.IUserRepository
import com.gilang.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
): IUserRepository {
    override fun getAllUsers(query: String?): Flow<Resource<List<User>>> {
        return object : NetworkBoundResource<List<User>, List<ListUserResponse>>() {
            override fun loadFromNetwork(data: List<ListUserResponse>): Flow<List<User>> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override fun shouldFetch(data: List<ListUserResponse>): Boolean {
                return true
            }

            override suspend fun createCall(): Flow<ApiResponse<List<ListUserResponse>>> {
                return remoteDataSource.getAllUsers(query)
            }

            override suspend fun saveCallResult(data: List<ListUserResponse>) {

            }
        }.asFlow()
    }

    override fun getAllFollowers(username: String?): Flow<Resource<List<User>>> {
        return object : NetworkBoundResource<List<User>, List<ListUserResponse>>() {
            override fun loadFromNetwork(data: List<ListUserResponse>): Flow<List<User>> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override fun shouldFetch(data: List<ListUserResponse>): Boolean {
                return true
            }

            override suspend fun createCall(): Flow<ApiResponse<List<ListUserResponse>>> {
                return remoteDataSource.getFollowers(username)
            }

            override suspend fun saveCallResult(data: List<ListUserResponse>) {

            }
        }.asFlow()
    }

    override fun getAllFollowing(username: String?): Flow<Resource<List<User>>> {
        return object : NetworkBoundResource<List<User>, List<ListUserResponse>>() {
            override fun loadFromNetwork(data: List<ListUserResponse>): Flow<List<User>> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override fun shouldFetch(data: List<ListUserResponse>): Boolean {
                return true
            }

            override suspend fun createCall(): Flow<ApiResponse<List<ListUserResponse>>> {
                return remoteDataSource.getFollowing(username)
            }

            override suspend fun saveCallResult(data: List<ListUserResponse>) {

            }
        }.asFlow()
    }

    override fun getDetailUser(username: String): Flow<Resource<User>> {
        return object : NetworkBoundResource<User, ListUserResponse>() {
            override fun loadFromNetwork(data: ListUserResponse): Flow<User> {
                return DataMapper.mapResponseToDomain(data)
            }

            override fun shouldFetch(data: ListUserResponse): Boolean {
                return true
            }

            override suspend fun createCall(): Flow<ApiResponse<ListUserResponse>> {
                return remoteDataSource.getUserDetail(username)
            }

            override suspend fun saveCallResult(data: ListUserResponse) {

            }
        }.asFlow()
    }

    override fun getFavoriteUser(): Flow<List<User>> {
        return localDataSource.getFavoriteUser().map {
            DataMapper.mapEntitiesToDomains(it)
        }
    }

    override fun getDetailState(username: String): Flow<User>? {
        return localDataSource.getDetailState(username)?.map {
            DataMapper.mapEntityToDomain(it)
        }
    }

    override suspend fun insertUser(user: User) {
        val domainUser = DataMapper.mapDomainToEntity(user)
        localDataSource.insertUser(domainUser)
    }

    override suspend fun deleteUser(user: User): Int {
        val domainUser = DataMapper.mapDomainToEntity(user)
        return localDataSource.deleteUser(domainUser)
    }
}