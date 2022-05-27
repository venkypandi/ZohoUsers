package com.venkatesh.zohousers.ui.users

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.venkatesh.zohousers.R
import com.venkatesh.zohousers.databinding.FragmentUserBinding
import com.venkatesh.zohousers.databinding.FragmentUserDetailBinding
import com.venkatesh.zohousers.utils.DateUtils
import com.venkatesh.zohousers.utils.DateUtils.Companion.getDateWithServerTimeStamp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailFragment : Fragment() {

    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!
    private val userViewModel by viewModels<UserDetailViewModel>()
    private val args by navArgs<UserDetailFragmentArgs>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserDetailBinding.inflate(inflater,container,false)
        postponeEnterTransition()
        val animation = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementEnterTransition = animation
        userViewModel.getUserByEmail(args.data)
        binding.ivProfile.transitionName = args.data

        userViewModel.userDetails.observe(viewLifecycleOwner){
            if(it!=null){
                Log.d("userdetails", "onCreateView: ${it}")
                binding.apply {
                    tvName.text = "${it.name.first} ${it.name.last}"
                    tvCityName.text = "${it.location.city}, ${it.location.country}"
                    tvGender.text = it.gender
                    tvEmail.text = it.email
                    tvDob.text = it.dob.date.getDateWithServerTimeStamp()
                    tvPhone.text = it.phone
                }
            }
            Glide.with(requireContext())
                .load(it.picture.large)
                .listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }

                })
                .placeholder(AppCompatResources.getDrawable(requireContext(),R.drawable.ic_robot))
                .into(binding.ivProfile)


        }
        return _binding!!.root
    }

}