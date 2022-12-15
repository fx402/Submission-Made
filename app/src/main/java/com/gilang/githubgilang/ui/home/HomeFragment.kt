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
import com.gilang.core.data.Resource
import com.gilang.core.ui.UserAdapter
import com.gilang.githubgilang.databinding.FragmentFavoriteBinding
import com.gilang.githubgilang.databinding.FragmentFollowBinding
import com.gilang.githubgilang.databinding.FragmentHomeBinding
import com.gilang.githubgilang.utils.ShowStateFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("DEPRECATION")
class HomeFragment : Fragment(), ShowStateFragment {
    private var _homeBinding: FragmentHomeBinding? = null
    private val homeBinding get() = _homeBinding!!
    private lateinit var homeAdapter: UserAdapter
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = resources.getString(R.string.menu_home)
        _homeBinding = FragmentHomeBinding.inflate(layoutInflater,container, false)
        return homeBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _homeBinding  = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeAdapter = UserAdapter(arrayListOf()){username, imageView ->
            findNavController().navigate(
                HomeFragmentDirections.actionNavHomeToDetailFragment(username),
                FragmentNavigatorExtras(imageView to username)
            )
        }

        homeBinding.rvUser.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = homeAdapter
        }

        homeBinding.rvUser.visibility = View.VISIBLE

        observeData()
    }

    private fun observeData() {
        homeViewModel.users.observe(viewLifecycleOwner) {
            if (it != null){
                when (it){
                    is Resource.Success -> {
                        onSuccessState(homeBinding = homeBinding)
                        it.data?.apply {
                            homeAdapter.setList(this)
                        }
                    }
                    is Resource.Error -> {
                        onErrorState(homeBinding = homeBinding, message = null)
                    }
                    is Resource.Loading -> {
                        onLoadingState(homeBinding)
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        val menuItem = menu.findItem(R.id.action_search)
        val mSearchView = MenuItemCompat.getActionView(menuItem)as SearchView
        mSearchView.setIconifiedByDefault(true)

        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                homeViewModel.setSearch(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                homeViewModel.setSearch(newText.toString())
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onSuccessState(
        homeBinding: FragmentHomeBinding?,
        followBinding: FragmentFollowBinding?,
        favoriteBinding: FragmentFavoriteBinding?
    ) {
        homeBinding?.apply {
            errorLayout.errorFragment.visibility = View.GONE
            pgBar.visibility = View.GONE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                pgBar.setProgress(100, false)
            }
            rvUser.visibility = View.VISIBLE
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                pgBar.setProgress(100, false)
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
            errorLayout.errorFragment.visibility = View.VISIBLE
            pgBar.visibility = View.GONE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                pgBar.setProgress(100, false)
            }
            rvUser.visibility = View.GONE
        }
    }
}