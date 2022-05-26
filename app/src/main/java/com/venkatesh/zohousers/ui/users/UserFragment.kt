package com.venkatesh.zohousers.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.venkatesh.zohousers.data.paging.UsersPagingAdapter
import com.venkatesh.zohousers.databinding.FragmentUserBinding

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val userViewModel by viewModels<UserViewModel>()
    lateinit var usersAdapter:UsersPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserBinding.inflate(inflater, container, false)

        userViewModel.result.observe(viewLifecycleOwner){
            if(it!=null){
                usersAdapter= UsersPagingAdapter(requireActivity()){

                }.apply { submitData(lifecycle,it) }
            }
        }

        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}