package edu.rosehulman.roseride.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.roseride.Constants
import edu.rosehulman.roseride.MainActivity.Companion.email
import edu.rosehulman.roseride.MainActivity.Companion.namae

class UserViewModel: ViewModel() {

    var user: User? = null

    var checkingUser = ""

    fun hasCompletedSetup() = user?.hasCompletedSetup?: false


    fun getOrMakeUser(observer: () -> Unit) {
        if (user != null) {
            //get
            observer()
        }
        else{
            //make
                Log.d("asdf", "${Firebase.auth.uid}")
            var ref = Firebase.firestore.collection(User.COLLECTION_PATH).document(Firebase.auth.uid!!)
            ref.get().addOnSuccessListener { snapshot: DocumentSnapshot ->
                if (snapshot.exists()) {
                    user = snapshot.toObject(User::class.java)
                }
                else{
                    user = User(
                        name=namae,
                        email=email)
                    ref.set(user!!)
                }
                observer()
            }
        }
    }

//    fun getOtherUser(theirUid: String){
//        val them = Firebase.firestore.collection("users").document(theirUid)
//        var usas = User()
//        them.get()
//            .addOnSuccessListener { document ->
//                val answer = document.data
//                usas = User(
//                    name = answer?.get("name") as String,
//                    email = answer?.get("email") as String,
//                    phone = answer?.get("phone") as String,
//                    storageUriString = answer?.get("storageUriString") as String,
//                    hasCompletedSetup = false
//                )
//                Log.d(Constants.TAG, "got user info: ${document.data}")
//            }
//            .addOnFailureListener { exception ->
//                Log.d(Constants.TAG, "get failed with ", exception)
//            }
//        checkingUser = usas
//    }

    fun update(newName: String, newPhone: String, newEmail: String, newStorageUriString: String, newHasCompletedSetup: Boolean) {
        if(user != null) {
            with(user!!) {
                name = newName
                phone = newPhone
                email = newEmail
                storageUriString = newStorageUriString
                hasCompletedSetup = newHasCompletedSetup
                var ref = Firebase.firestore.collection(User.COLLECTION_PATH).document(Firebase.auth.uid!!)
                ref.set(this)
            }
        }
    }

}