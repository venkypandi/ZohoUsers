package com.venkatesh.zohousers.data.paging

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.venkatesh.zohousers.R
import com.venkatesh.zohousers.data.remote.model.Result

class UsersPagingAdapter(var context:Context, var clickListener:()->Unit) :
    PagingDataAdapter<Result, UsersPagingAdapter.UsersViewHolder>(COMPARATOR) {

    inner class UsersViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        fun bind(context: Context,data:Result?,clickListener: () -> Unit){

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
    ): UsersPagingAdapter.UsersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_list_item,parent,false)
        return UsersViewHolder(view)
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