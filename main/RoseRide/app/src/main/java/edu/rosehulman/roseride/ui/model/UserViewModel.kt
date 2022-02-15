package edu.rosehulman.roseride.ui.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserViewModel: ViewModel() {


    var user: User? = null

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
                    user = User(name=Firebase.auth.currentUser!!.displayName!!)
                    ref.set(user!!)
                }
                observer()
            }
        }
    }

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