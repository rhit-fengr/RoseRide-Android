package edu.rosehulman.roseride.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.rosehulman.roseride.databinding.FragmentUserBinding


class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding

    // This property is only valid between onCreateView and
    // onDestroyView.


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root
        updateView()
        val logOutbtn = binding.logoutButton
        logOutbtn.setOnClickListener {
            Firebase.auth.signOut()
        }
        return root
    }

    private fun updateView() {
        binding.centerImage.load("https://media-exp1.licdn.com/dms/image/C4E03AQHYevMgxmPPMw/profile-displayphoto-shrink_200_200/0/1603517211239?e=1647475200&v=beta&t=LZFiFKxY_7fBwUkRMAqvqPRBtWCP5FKc4qT6OtoqUl0"){
            crossfade(true)
            transformations(CircleCropTransformation())
        }
    }

}