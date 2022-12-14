package com.gilang.githubgilang.ui.home

import android.app.SearchManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gilang.githubgilang.R
import com.gilang.githubgilang.core.data.Resource
import com.gilang.githubgilang.core.ui.UserAdapter
import com.gilang.githubgilang.databinding.FragmentFavoriteBinding
import com.gilang.githubgilang.databinding.FragmentFollowBinding
import com.gilang.githubgilang.databinding.FragmentHomeBinding
import com.gilang.githubgilang.utils.ShowStateFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), ShowStateFragment {
    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var homeAdapter: UserAdapter
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var menuItem: MenuItem
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val actionBar = (activity as AppCompatActivity).supportActionBar
//        actionBar?.title = getString(R.string.home)
        homeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        homeBinding.errorLayout.emptyText.text = getString(R.string.not_found)

        homeAdapter = UserAdapter(arrayListOf()) { username, iv ->
            findNavController().navigate(
                HomeFragmentDirections.actionNavHomeToDetailFragment(username),
                FragmentNavigatorExtras(iv to username)
            )
        }

        homeBinding.rvUser.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = homeAdapter
        }

        homeBinding.rvUser.visibility = View.VISIBLE
        resources

        observeData()
    }

    private fun observeData() {
        homeViewModel.users.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is Resource.Success -> {
                        onSuccessState(homeBinding)

                        it.data?.apply {
                            homeAdapter.setList(this)
                        }
                    }
                    is Resource.Error -> {
                        onErrorState(homeBinding, message = it.message)
                    }
                    is Resource.Loading -> {
                        onLoadingState(homeBinding)
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
        homeBinding?.apply {
            errorLayout.errorFragment.visibility = View.GONE
            pgBar.visibility = View.GONE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                pgBar.setProgress(100, false)
            }
            rvUser.visibility = View.VISIBLE
            resources
        }
    }

    override fun onLoadingState(
        homeBinding: FragmentHomeBinding?,
        followBinding: FragmentFollowBinding?,
        favoriteBinding: FragmentFavoriteBinding?
    ) {
        homeBinding?.apply {
            errorLayout.errorFragment.visibility = View.GONE
            pgBar.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                pgBar.setProgress(100, true)
            }
            rvUser.visibility = View.GONE
        }
    }

    override fun onErrorState(
        homeBinding: FragmentHomeBinding?,
        followBinding: FragmentFollowBinding?,
        favoriteBinding: FragmentFavoriteBinding?,
        message: String?
    ) {
        homeBinding?.apply {
            errorLayout.apply {
                errorFragment.visibility = View.VISIBLE
                pgBar.visibility = View.GONE

            rvUser.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main,menu)
        menuItem = menu.findItem(R.id.action_search)
        searchView = MenuItemCompat.getActionView(menuItem) as SearchView
        searchView.setIconifiedByDefault(true)

        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                homeViewModel.setSearch(query = query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                homeViewModel.setSearch(query = newText.toString())
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }
}