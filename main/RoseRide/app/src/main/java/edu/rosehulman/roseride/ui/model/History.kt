package edu.rosehulman.roseride.ui.model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import edu.rosehulman.roseride.Constants
import java.util.Date
import java.sql.Time



data class History(
    var title: String="",
    var driver: String="",
    var setOffTime: String="",
    var setOffDate: String="",
    var destinationAddr: Address= Address(),
    var passengers: List<String> = listOf(),
    var costPerPerson: Double = 0.0) {

    @get:Exclude
    var id = ""

    @ServerTimestamp
    var created: Timestamp? = null

    override fun toString(): String {
        return if (title.isNotBlank()) "$title, driver: $driver" else ""
    }

    companion object {
        const val COLLECTION_PATH = "history"
        const val CREATED_KEY = "created"
        fun from(snapshot: DocumentSnapshot): History {
            val r = snapshot.toObject(History::class.java)!! // data only
            r.id = snapshot.id
            return r
        }
    }
}