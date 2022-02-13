package edu.rosehulman.roseride.ui.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.sql.Date
import java.sql.Time

data class Ride(
    var title: String="",
    var driver: String="",
    var setOffTime: String="",
    var setOffDate: String="",
    var pickUpAddr: Address= Address(),
//    var returnTime: String="",
    var destinationAddr: Address= Address(),
    var passengers: List<String> = listOf(),
    var costPerPerson: Double = 0.0,
    var numOfSlots: Int=1,
    var sharable: Boolean = false,
    var isSelected: Boolean = false) {

    @get:Exclude
    var id = ""

    @ServerTimestamp
    var created: Timestamp? = null

    override fun toString(): String {
        return if (title.isNotBlank()) "$title, driver: $driver" else ""
    }

    companion object {
        const val COLLECTION_PATH = "rides"
        const val CREATED_KEY = "created"
        fun from(snapshot: DocumentSnapshot):Ride {
            val r = snapshot.toObject(Ride::class.java)!! // data only
            r.id = snapshot.id
            return r
        }
    }
}