package com.gilang.githubgilang.ui.follow

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.gilang.githubgilang.R
import com.gilang.githubgilang.core.data.Resource
import com.gilang.githubgilang.core.ui.UserAdapter
import com.gilang.githubgilang.databinding.FragmentFavoriteBinding
import com.gilang.githubgilang.databinding.FragmentFollowBinding
import com.gilang.githubgilang.databinding.FragmentHomeBinding
import com.gilang.githubgilang.utils.ShowStateFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class FollowFragment : Fragment(), ShowStateFragment {

    private lateinit var followBinding: FragmentFollowBinding
    private lateinit var followAdapter: UserAdapter
    private lateinit var username: String
    private var type: String? = null
    private val followViewModel: FollowViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(USERNAME).toString()
            type = it.getString(TYPE)
            Log.d(FollowFragment.javaClass.simpleName, "onCreate: $username + $type")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        followBinding = FragmentFollowBinding.inflate(layoutInflater, container, false)
        return followBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followAdapter = UserAdapter(arrayListOf()){user, _ ->
            Toast.makeText(context, user, Toast.LENGTH_SHORT).show()
        }
        followBinding.rvUserFollow.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = followAdapter
        }
        when(type){
            resources.getString(R.string.followers) -> {
                followViewModel.setFollow(username, FollowViewModel.TypeView.FOLLOWER)
            }
            resources.getString(R.string.following) -> {
                followViewModel.setFollow(username, FollowViewModel.TypeView.FOLLOWING)
            }
            else -> {
                onErrorState(followBinding = followBinding, message = null)
            }
        }

        observeFollow()
    }

    private fun observeFollow() {
        followViewModel.favoriteUsers.observe(viewLifecycleOwner){
            it.let {
                when(it){
                    is Resource.Success -> {
                        if (it.data.isNullOrEmpty()){
                            onErrorState(followBinding = followBinding, message = null)
                        }
                        else {
                            onSuccessState(followBinding = followBinding)
                            Log.d(FollowFragment.javaClass.simpleName, "observeFollow: ${it.data}")
                            followAdapter.run { setList(it.data) }
                        }
                    }
                    is Resource.Loading -> {
                        onLoadingState(followBinding = followBinding)
                    }
                    is Resource.Error -> {
                        onErrorState(followBinding = followBinding, message = it.message)
                    }
                }
            }
        }
    }

    override fun onSuccessState(
        homeBinding: FragmentHomeBinding?,
        followBinding: FragmentFollowBinding?,
        favoriteBinding: FragmentFavoriteBinding?
    ) {
        followBinding?.let {
            it.errorLayout.errorFragment.visibility = View.GONE
            it.rvUserFollow.visibility = View.VISIBLE
        }
    }

    override fun onLoadingState(
        homeBinding: FragmentHomeBinding?,
        followBinding: FragmentFollowBinding?,
        favoriteBinding: FragmentFavoriteBinding?
    ) {
        followBinding?.let {
            it.errorLayout.errorFragment.visibility = View.GONE
            it.rvUserFollow.visibility = View.GONE
        }
    }

    override fun onErrorState(
        homeBinding: FragmentHomeBinding?,
        followBinding: FragmentFollowBinding?,
        favoriteBinding: FragmentFavoriteBinding?,
        message: String?
    ) {
        followBinding?.let {
            it.errorLayout.errorFragment.visibility = View.VISIBLE
            it.rvUserFollow.visibility = View.VISIBLE

        }
    }

    companion object {
        fun newInstance(username: String, type: String): Fragment{
            return FollowFragment().apply {
                arguments = Bundle().apply {
                    putString(USERNAME, username)
                    putString(TYPE, type)
                }
            }
        }
        private const val USERNAME = "USERNAME"
        private const val TYPE = "TYPE"
    }

}