package com.gilang.githubgilang.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gilang.githubgilang.R
import com.gilang.githubgilang.core.data.Resource
import com.gilang.githubgilang.databinding.FragmentDetailBinding
import com.gilang.githubgilang.domain.model.User
import com.gilang.githubgilang.ui.follow.FollowFragment
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()
    private lateinit var user: User
    private var isFavorite = false
    private lateinit var viewBinding : FragmentDetailBinding
    private val detailViewModel: DetailViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentDetailBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        observeData()
        return viewBinding.root
    }

    private fun observeData() {
        detailViewModel.getDetail(args.username).observe(viewLifecycleOwner){
            when(it){
                is Resource.Error -> {
                    viewBinding.fabFavorite.hide()
                    viewBinding.progressBar2.visibility = View.GONE
                }

                is Resource.Loading -> {
                    viewBinding.fabFavorite.hide()
                    viewBinding.progressBar2.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    user = it.data!!
                    viewBinding.data = it.data
                    viewBinding.progressBar2.visibility = View.GONE
                    detailViewModel.getDetailState(args.username)?.observe(viewLifecycleOwner){
                        isFavorite = it.isFavorite == true
                        changedFavorite(isFavorite)
                    }
                    viewBinding.fabFavorite.show()
                }
            }
            changedFavorite(isFavorite)
            viewBinding.fabFavorite.setOnClickListener {
                addOrRemoveFavorite()
                changedFavorite(isFavorite)
            }
        }
    }

    private fun addOrRemoveFavorite() {
        if (isFavorite){
            user.isFavorite = !isFavorite
            detailViewModel.deleteFavorite(user)
            Toast.makeText(context, getString(R.string.favorite_removed), Toast.LENGTH_SHORT).show()
            isFavorite = !isFavorite
        }else {
            user.isFavorite = !isFavorite
            detailViewModel.insertFavorite(user)
            Toast.makeText(
                context, getString(R.string.favorite_added), Toast.LENGTH_SHORT
            ).show()
            isFavorite = !isFavorite
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun changedFavorite(state: Boolean){
        if (state){
            viewBinding.fabFavorite.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_unfavorite_24))
        }else{
            viewBinding.fabFavorite.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_favorite_24))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabList = arrayOf(resources.getString(R.string.followers), resources.getString(R.string.following))
        val pagerAdapter = PagerAdapter(tabList, args.username, this)
        viewBinding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager){tab, position ->
            tab.text = tabList[position]
        }.attach()
    }

    inner class PagerAdapter(
        private val tabList: Array<String>,
        private val username: String,
        fragment: Fragment
    ): FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return tabList.size
        }

        override fun createFragment(position: Int): Fragment {
            return FollowFragment.newInstance(username, tabList[position])
        }
    }
}