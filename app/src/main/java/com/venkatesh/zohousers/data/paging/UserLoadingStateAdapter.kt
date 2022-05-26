package com.venkatesh.zohousers.data.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.venkatesh.zohousers.R
import com.venkatesh.zohousers.databinding.LayoutLoadStateBinding

class UserLoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<UserLoadingStateAdapter.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(
        private val binding: LayoutLoadStateBinding,
        private val retryCallback:()->Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener{ retryCallback.invoke() }
        }

        fun bind(loadState: LoadState) {
            with(binding) {
                progressBar.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState is LoadState.Error
                errorMsg.isVisible =
                    !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
                errorMsg.text = (loadState as? LoadState.Error)?.error?.message
            }
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState)=
        LoadStateViewHolder(
            LayoutLoadStateBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.layout_load_state,parent,false)),
            retry
        )
}