package com.gilang.githubgilang.utils

import com.gilang.githubgilang.databinding.FragmentFavoriteBinding
import com.gilang.githubgilang.databinding.FragmentFollowBinding
import com.gilang.githubgilang.databinding.FragmentHomeBinding

interface ShowStateFragment {
    fun onSuccessState(homeBinding: FragmentHomeBinding? = null,
                        followBinding: FragmentFollowBinding? = null,
                        favoriteBinding: FragmentFavoriteBinding? = null)

    fun onLoadingState(homeBinding: FragmentHomeBinding? = null,
                       followBinding: FragmentFollowBinding? = null,
                       favoriteBinding: FragmentFavoriteBinding? = null)

    fun onErrorState(homeBinding: FragmentHomeBinding? = null,
                       followBinding: FragmentFollowBinding? = null,
                       favoriteBinding: FragmentFavoriteBinding? = null,
                        message: String?)
}