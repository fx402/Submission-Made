package com.gilang.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val id:Int?,
    val url:String?,
    val login:String?,
    val avatarUrl:String?,
    val location:String?,
    val name:String?,
    val type:String?,
    val publicRepos:Int?,
    val followers:Int?,
    val following:Int?,
    var isFavorite:Boolean?,
): Parcelable