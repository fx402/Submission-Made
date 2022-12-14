package com.gilang.githubgilang.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse (
    @field:SerializedName("items")
    val items: List<ListUserResponse>
)