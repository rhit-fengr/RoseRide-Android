package edu.rosehulman.roseride.ui.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import edu.rosehulman.roseride.Constants
import edu.rosehulman.roseride.R
import edu.rosehulman.roseride.databinding.FragmentUserBinding
import edu.rosehulman.roseride.databinding.FragmentUserOtherBinding
import edu.rosehulman.roseride.model.User
import edu.rosehulman.roseride.model.UserViewModel


class UserOtherFragment : Fragment() {

    private lateinit var binding: FragmentUserOtherBinding
    private lateinit var userModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        binding = FragmentUserOtherBinding.inflate(inflater, container, false)
        val root: View = binding.root
        updateView()
        return root
    }

    private fun updateView() {
        val docRef = Firebase.firestore.collection("users").document(userModel.checkingUser)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val user = documentSnapshot.toObject<User>()
            binding.profileName.text = user?.name
            binding.profileEmail.text = user?.email
            binding.profilePhone.text = user?.phone
            if(user?.storageUriString != "") {
                binding.centerImage.load(user?.storageUriString) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }
        }

    }

}