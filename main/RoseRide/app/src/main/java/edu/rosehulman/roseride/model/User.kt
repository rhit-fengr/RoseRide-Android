package edu.rosehulman.roseride.model

import com.google.firebase.firestore.DocumentSnapshot

data class User(var name: String="",
                var phone: String="",
                var email: String="",
                var storageUriString: String = "",
                var hasCompletedSetup: Boolean = false) {
    override fun toString(): String {
        return if (name.isNotBlank()) "$name, phone: $phone, email addr: $email" else ""
    }

    companion object {
        const val COLLECTION_PATH = "users"
        fun from(snapshot: DocumentSnapshot):User {
            val r = snapshot.toObject(User::class.java)!! // data only
            return r
        }
    }
}