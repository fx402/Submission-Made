package com.gilang.core.data.source.remote

import android.util.Log
import com.gilang.core.data.source.remote.network.ApiResponse
import com.gilang.core.data.source.remote.network.ApiService
import com.gilang.core.data.source.remote.response.ListUserResponse
import com.gilang.core.data.source.remote.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun getAllUsers(query: String?): Flow<ApiResponse<List<ListUserResponse>>> =
        flow {
            try {
                var search: UserResponse? = null
                if (query == null){
                    search = apiService.searchUsersDefault()
                }else{
                    search = apiService.searchUsers(query)
                }
                val userArrays = search.items
                if (userArrays.isEmpty()){
                    emit(ApiResponse.Error(null))
                }else{
                    emit(ApiResponse.Success(userArrays))
                }
            }catch (e:Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", "getAllUsers: $e", )
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getFollowers(username: String?): Flow<ApiResponse<List<ListUserResponse>>> =
        flow {
            try {
                val followers = apiService.userFollower(username)
                emit(ApiResponse.Success(followers))
            }catch (e: Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", "getFollowers: $e", )
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getFollowing(username: String?): Flow<ApiResponse<List<ListUserResponse>>> =
        flow {
            try {
                val following = apiService.userFollowing(username)
                emit(ApiResponse.Success(following))
            }catch (e: Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", "getError: $e", )
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getUserDetail(username: String): Flow<ApiResponse<ListUserResponse>> =
        flow {
            try {
                val userDetail = apiService.userDetail(username)
                emit(ApiResponse.Success(userDetail))
            }catch (e: Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", "getError: $e", )
            }
        }.flowOn(Dispatchers.IO)
}