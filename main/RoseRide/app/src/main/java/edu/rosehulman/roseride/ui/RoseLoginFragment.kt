package edu.rosehulman.roseride.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.rosehulman.roseride.databinding.FragmentRoseLoginBinding

//import edu.rosehulman.rosefire.Rosefire

class RoseLoginFragment : Fragment() {
    private lateinit var binding: FragmentRoseLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoseLoginBinding.inflate(inflater, container, false)
//        binding.rosefireLogin.setOnClickListener {
//            val signInIntent = Rosefire.getSignInIntent(this, "e015dafd-140f-4ea7-8b7c-c9a0f26c223f");
//            resultLauncher.launch(signInIntent)
//        }
        return binding.root
    }
}