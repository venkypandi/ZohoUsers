package com.venkatesh.zohousers.data.paging

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.venkatesh.zohousers.R
import com.venkatesh.zohousers.data.remote.model.Result
import com.venkatesh.zohousers.databinding.UserListItemBinding
import kotlin.math.log

class UsersPagingAdapter(var context:Context, var clickListener:()->Unit) :
    PagingDataAdapter<Result, UsersPagingAdapter.UsersViewHolder>(COMPARATOR) {

    inner class UsersViewHolder(private val binding:UserListItemBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(context: Context,data:Result?,clickListener: () -> Unit){
            Glide.with(context)
                .load(data!!.picture.large)
                .placeholder(AppCompatResources.getDrawable(context,R.drawable.ic_robot))
                .into(binding.ivProfile)
            Log.d("usersadapter", data.toString())

            binding.tvTitle.text = "${data.name.title} ${data.name.first} ${data.name.last}"
            binding.tvName.text = data.location.city
        }

    }

    override fun onBindViewHolder(holder: UsersPagingAdapter.UsersViewHolder, position: Int) {
        Log.d("useradapter", "onBindViewHolder: 1")
        getItem(position).let {
            holder.bind(context,it,clickListener)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsersViewHolder {
        val binding = UserListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UsersViewHolder(binding)
    }

    companion object{
        private val COMPARATOR = object : DiffUtil.ItemCallback<Result>(){
            override fun areItemsTheSame(
                oldItem: Result,
                newItem: Result
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Result,
                newItem: Result
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}