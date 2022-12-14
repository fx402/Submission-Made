package com.gilang.githubgilang.core.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gilang.githubgilang.databinding.ItemUserBinding
import com.gilang.githubgilang.domain.model.User

class UserAdapter(private val user: ArrayList<User>,
                  private val clickListener: (String, View)->Unit):
RecyclerView.Adapter<UserAdapter.ViewHolder>(){

    @SuppressLint("NotifyDataSetChanged")
    fun setList(items: List<User>?){
        user.apply {
            clear()
            items?.let { addAll(it) }
            notifyDataSetChanged()
        }
    }
    inner class ViewHolder(private val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, click: (String, View)->Unit){
            binding.data = user
            binding.root.transitionName = user.login
            binding.root.setOnClickListener { user.login?.let { i -> click(i, binding.root) } }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(user[position], clickListener)
    }

    override fun getItemCount(): Int {
        return user.size
    }
}