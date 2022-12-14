package com.gilang.favorite.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gilang.core.ui.UserAdapter
import com.gilang.githubgilang.databinding.FragmentFavoriteBinding
import com.gilang.githubgilang.databinding.FragmentFollowBinding
import com.gilang.githubgilang.databinding.FragmentHomeBinding
import com.gilang.githubgilang.utils.ShowStateFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteFragment : Fragment(), ShowStateFragment {

    private var _binding: FragmentFavoriteBinding? = null
    private lateinit var favoriteAdapter: UserAdapter
    private val favoriteViewModel: FavoriteViewModel by viewModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val favoriteViewModel =
//            ViewModelProvider(this).get(FavoriteViewModel::class.java)

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        loadKoinModules(com.gilang.favorite.di.favoriteModule)

//        val textView: TextView = binding.textGallery
//        galleryViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteAdapter = UserAdapter(arrayListOf()){username, imageView->
            findNavController().navigate(
                FavoriteFragmentDirections.actionNavFavoriteToDetailFragment(username),
                FragmentNavigatorExtras(imageView to username)
            )
        }

        binding.rvUserFavorite.let {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            it.adapter = favoriteAdapter
        }
        observeFavorite()
    }

    private fun observeFavorite() {
        onLoadingState(favoriteBinding = binding)
        favoriteViewModel.getFavoriteUsers.observe(viewLifecycleOwner){list ->
            list.apply {
                if (!isNullOrEmpty()){
                    onSuccessState(favoriteBinding = binding)
                    favoriteAdapter.setList(this)
                }else{
                    onErrorState(
                        favoriteBinding = binding,
                        message = "Nothing Favorite"
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSuccessState(
        homeBinding: FragmentHomeBinding?,
        followBinding: FragmentFollowBinding?,
        favoriteBinding: FragmentFavoriteBinding?
    ) {
        binding.apply {
            errorLayout.errorFragment.visibility = View.GONE
            rvUserFavorite.visibility = View.VISIBLE
            pgBar.visibility = View.GONE
        }
    }

    override fun onLoadingState(
        homeBinding: FragmentHomeBinding?,
        followBinding: FragmentFollowBinding?,
        favoriteBinding: FragmentFavoriteBinding?
    ) {
        binding.apply {
            errorLayout.errorFragment.visibility = View.GONE
            rvUserFavorite.visibility = View.GONE
            pgBar.visibility = View.VISIBLE
        }
    }

    override fun onErrorState(
        homeBinding: FragmentHomeBinding?,
        followBinding: FragmentFollowBinding?,
        favoriteBinding: FragmentFavoriteBinding?,
        message: String?
    ) {
        binding.apply {
            errorLayout.errorFragment.visibility = View.VISIBLE
            rvUserFavorite.visibility = View.GONE
            pgBar.visibility = View.GONE
        }
    }
}