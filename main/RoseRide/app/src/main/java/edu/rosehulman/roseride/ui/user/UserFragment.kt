package edu.rosehulman.roseride.ui.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.rosehulman.roseride.Constants
import edu.rosehulman.roseride.MainActivity
import edu.rosehulman.roseride.R
import edu.rosehulman.roseride.databinding.FragmentUserBinding
import edu.rosehulman.roseride.model.UserViewModel


class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding
    private lateinit var userModel: UserViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root
        updateView()
        val logOutbtn = binding.logoutButton
        logOutbtn.setOnClickListener {
            MainActivity.checker=false
            Firebase.auth.signOut()
            findNavController().navigate(R.id.nav_splash)
        }
        binding.editButton.setOnClickListener {
            findNavController().navigate(R.id.nav_profile_edit)
        }
        return root
    }

    private fun updateView() {

        userModel.getOrMakeUser {
            with(userModel.user!!) {
                Log.d(Constants.TAG, "$this")
                binding.profileName.setText(name)
                binding.profilePhone.setText(phone)
                binding.profileEmail.setText(email)
                binding.centerImage.load(storageUriString){
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }
        }
    }

}