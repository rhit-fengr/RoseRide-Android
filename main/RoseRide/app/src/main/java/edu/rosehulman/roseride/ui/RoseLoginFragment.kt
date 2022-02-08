package edu.rosehulman.roseride.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.rosehulman.roseride.databinding.FragmentRoseLoginBinding
import androidx.fragment.app.Fragment


//import edu.rosehulman.rosefire.Rosefire

class RoseLoginFragment : Fragment() {
    private lateinit var binding: FragmentRoseLoginBinding

//    private val REGISTRY_TOKEN = "e015dafd-140f-4ea7-8b7c-c9a0f26c223f"
//    private var mAuth: FirebaseAuth? = null
//    private var mAuthListener: AuthStateListener? = null
//    private var resultLauncher = registerForActivityResult(
//    ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val data: Intent? = result.data
//            val rosefireResult = Rosefire.getSignInResultFromIntent(data)
//            if (rosefireResult.isSuccessful) {
//                Firebase.auth.signInWithCustomToken(rosefireResult.token)
//                Log.d(Constants.TAG, "Username: ${rosefireResult.username}")
//                Log.d(Constants.TAG, "Name: ${rosefireResult.name}")
//                Log.d(Constants.TAG, "Email: ${rosefireResult.email}")
//                Log.d(Constants.TAG, "Group: ${rosefireResult.group}")
//            } else {
//                Log.d(Constants.TAG, "Rosefire failed")
//            }
//        }
//    }


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

//        val loginButton: View = binding.rosefireLogin
//        mAuth = FirebaseAuth.getInstance()
//        mAuthListener = AuthStateListener { firebaseAuth ->
//            val user = firebaseAuth.currentUser
////            val username = user?.uid ?: "null"
//            loginButton.setVisibility(if (user != null) View.GONE else View.VISIBLE)
//        }
//        loginButton.setOnClickListener(this)

        return binding.root
    }

}