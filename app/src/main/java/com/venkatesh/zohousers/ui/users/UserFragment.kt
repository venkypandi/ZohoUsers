package com.venkatesh.zohousers.ui.users

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.venkatesh.zohousers.data.paging.UserLoadingStateAdapter
import com.venkatesh.zohousers.data.paging.UsersPagingAdapter
import com.venkatesh.zohousers.data.remote.model.Result
import com.venkatesh.zohousers.databinding.FragmentUserBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val userViewModel by viewModels<UserViewModel>()
    private lateinit var usersAdapter:UsersPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserBinding.inflate(inflater, container, false)
        postponeEnterTransition()
        usersAdapter=UsersPagingAdapter(requireContext()){ data,view->
                    listItemClicked(data,view)
                }
//        userViewModel.searchUsers("Sa")
        binding.swipeRefresh.setOnRefreshListener {
            usersAdapter.refresh()
            binding.swipeRefresh.isRefreshing = false
        }



        var concatAdapter = usersAdapter.withLoadStateHeaderAndFooter(
            header = UserLoadingStateAdapter{usersAdapter.retry()},
            footer = UserLoadingStateAdapter{usersAdapter.retry()}
        )

        binding.rvUsers.adapter = concatAdapter

//       viewLifecycleOwner.lifecycleScope.launch {
//           userViewModel.pagingFlow.collectLatest { usersAdapter.submitData(it)  }
//       }


        userViewModel.result.observe(viewLifecycleOwner){
            if(it!=null){
                Log.d("UserFragmentDebug", "load: 1")
                binding.rvUsers.setHasFixedSize(true)
                usersAdapter.submitData(lifecycle,it)
                (view?.parent as? ViewGroup)?.doOnPreDraw {
                    startPostponedEnterTransition()
                }

            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            usersAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty = loadState.refresh is LoadState.NotLoading && usersAdapter.itemCount == 0
                binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        requireContext(),
                        "Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        return _binding!!.root
    }

    private fun listItemClicked(data: Result,view: View) {
        Log.d("email", "listItemClicked: ${data.email}")
        val extras = FragmentNavigatorExtras(
            view to data.email
        )
        val directions = UserFragmentDirections.actionNavHomeToUserDetailFragment(data.email)
        findNavController().navigate(directions,extras)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}