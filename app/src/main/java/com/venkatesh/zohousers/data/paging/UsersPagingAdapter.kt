package com.venkatesh.zohousers.data.paging

import android.content.Context
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

class UsersPagingAdapter(var context:Context, var clickListener:(Result,View)->Unit) :
    PagingDataAdapter<Result, UsersPagingAdapter.UsersViewHolder>(COMPARATOR) {

    inner class UsersViewHolder(private val binding:UserListItemBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(context: Context,data:Result?,clickListener: (Result,View) -> Unit){
            Glide.with(context)
                .load(data!!.picture.large)
                .placeholder(AppCompatResources.getDrawable(context,R.drawable.ic_robot))
                .into(binding.ivProfile)
            binding.ivProfile.transitionName = data.email

            binding.tvTitle.text = "${data.name.title} ${data.name.first} ${data.name.last}"
            binding.tvName.text = data.location.city
            binding.cvUsers.setOnClickListener {
                clickListener(data,binding.ivProfile)
            }
        }

    }

    override fun onBindViewHolder(holder: UsersPagingAdapter.UsersViewHolder, position: Int) {
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