package com.gilang.githubgilang.utils

import com.gilang.githubgilang.core.data.source.local.entity.UserEntity
import com.gilang.githubgilang.core.data.source.remote.response.ListUserResponse
import com.gilang.githubgilang.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object DataMapper {

    fun mapResponseToDomain(input: ListUserResponse): Flow<User>{
        return flowOf(
            User(
                id = input.id,
                login = input.login,
                url = input.url,
                avatarUrl = input.avatarUrl,
                location = input.location,
                name = input.name,
                followers = input.follower,
                following = input.following,
                type = input.type,
                publicRepos = input.publicRepos,
                isFavorite = false
            )
        )
    }

    fun mapResponsesToDomain(input: List<ListUserResponse>): Flow<List<User>>{
        val dataArray = ArrayList<User>()
        input.map {
            val user = User(
                id = it.id,
                login = it.login,
                url = it.url,
                avatarUrl = it.avatarUrl,
                location = it.location,
                name = it.name,
                followers = it.follower,
                following = it.following,
                type = it.type,
                publicRepos = it.publicRepos,
                isFavorite = false
            )
            dataArray.add(user)
        }
        return flowOf(dataArray)
    }

    fun mapEntitiesToDomains(input: List<UserEntity>): List<User>{
        return input.map {
            User(
                id = it.id,
                login = it.login,
                url = it.url,
                avatarUrl = it.avatarUrl,
                location = it.location,
                name = it.name,
                followers = it.followers,
                following = it.following,
                type = it.type,
                publicRepos = it.publicRepos,
                isFavorite = it.isFavorite
            )
        }
    }

    fun mapEntityToDomain(input: UserEntity?): User{
        return User(
                id = input?.id,
                login = input?.login,
                url = input?.url,
                avatarUrl = input?.avatarUrl,
                location = input?.location,
                name = input?.name,
                followers = input?.followers,
                following = input?.following,
                type = input?.type,
                publicRepos = input?.publicRepos,
                isFavorite = input?.isFavorite
            )
    }

    fun mapDomainToEntity(input: User): UserEntity{
        return UserEntity(
            id = input.id,
            login = input.login,
            url = input.url,
            avatarUrl = input.avatarUrl,
            location = input.location,
            name = input.name,
            followers = input.followers,
            following = input.following,
            type = input.type,
            publicRepos = input.publicRepos,
            isFavorite = input.isFavorite
        )
    }
}