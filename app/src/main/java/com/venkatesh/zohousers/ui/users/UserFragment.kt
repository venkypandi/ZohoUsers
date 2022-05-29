package com.venkatesh.zohousers.ui.users

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.venkatesh.zohousers.MainActivity
import com.venkatesh.zohousers.data.paging.UserLoadingStateAdapter
import com.venkatesh.zohousers.data.paging.UsersPagingAdapter
import com.venkatesh.zohousers.data.remote.model.Result
import com.venkatesh.zohousers.databinding.FragmentUserBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
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
        binding.ivNavbar.setOnClickListener {
            MainActivity.mainActivity?.showNavDrawer()
        }

        binding.swipeRefresh.setOnRefreshListener {
            usersAdapter.refresh()
            binding.swipeRefresh.isRefreshing = false
        }


        var concatAdapter = usersAdapter.withLoadStateHeaderAndFooter(
            header = UserLoadingStateAdapter{usersAdapter.retry()},
            footer = UserLoadingStateAdapter{usersAdapter.retry()}
        )

        binding.rvUsers.adapter = concatAdapter
        setUpListObserver()
        binding.ivSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                filter(query!!)
                setUpObserver()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }

        })

        binding.ivSearch.setOnCloseListener(object: SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                setUpListObserver()
                return false
            }

        })

        binding.ivSearch.setOnSuggestionListener(object :SearchView.OnSuggestionListener{
            override fun onSuggestionSelect(position: Int): Boolean {

                return false

            }

            override fun onSuggestionClick(position: Int): Boolean {
                return false

            }

        })


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
        val extras = FragmentNavigatorExtras(
            view to data.email
        )
        val directions = UserFragmentDirections.actionNavHomeToUserDetailFragment(data.email)
        findNavController().navigate(directions,extras)
    }
    fun setUpListObserver(){
        userViewModel.result.observe(viewLifecycleOwner){
            if(it!=null){
                usersAdapter.submitData(lifecycle,it)
                (view?.parent as? ViewGroup)?.doOnPreDraw {
                    startPostponedEnterTransition()
                }

            }
        }
    }

    fun setUpObserver(){
        userViewModel.searchResult.observeOnce(viewLifecycleOwner) {
               if(it!=null){
                   usersAdapter.submitData(lifecycle,it)
               }
           }
    }

    private fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
    fun filter(text: String) {
        userViewModel.searchUsers(text)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
}