package com.gilang.githubgilang.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListUserResponse (
    @field:SerializedName("id")
    val id: Int?,

    @field:SerializedName("html_url")
    val url: String?,

    @field:SerializedName("login")
    val login: String?,

    @field:SerializedName("avatar_url")
    val avatarUrl: String?,

    @field:SerializedName("location")
    val location: String?,

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("type")
    val type: String?,

    @field:SerializedName("public_repos")
    val publicRepos: Int?,

    @field:SerializedName("follower")
    val follower: Int?,

    @field:SerializedName("following")
    val following: Int?,
)