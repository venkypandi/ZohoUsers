package com.venkatesh.zohousers.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.venkatesh.zohousers.R
import com.venkatesh.zohousers.data.remote.model.Result
import com.venkatesh.zohousers.databinding.WeatherListItemBinding

class UserWeatherAdapter(private val context: Context,
                         private val userList: List<Result>,
                         private val clickListener:(Result)->Unit):RecyclerView.Adapter<UserWeatherAdapter.UserWeatherViewHolder>() {

    inner class UserWeatherViewHolder(private val binding: WeatherListItemBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(context: Context,data:Result,clickListener: (Result) -> Unit){
            binding.apply {
                tvName.text = "${data.name.first}"
                tvCityName.text = data.location.city
                Glide.with(context)
                    .load(data.picture.medium)
                    .placeholder(AppCompatResources.getDrawable(context, R.drawable.ic_robot))
                    .into(ivProfile)
                cvUsers.setOnClickListener {
                    clickListener(data)
                }
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserWeatherAdapter.UserWeatherViewHolder {
        val binding = WeatherListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserWeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserWeatherAdapter.UserWeatherViewHolder, position: Int) {
        holder.bind(context,userList[position],clickListener)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}