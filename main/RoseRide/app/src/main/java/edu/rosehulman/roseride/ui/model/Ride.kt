package edu.rosehulman.roseride.ui.model

import java.sql.Time

data class Ride(var title: String="", var driver: User, var addr: Address, var setOffTime: Time, var pickUpAddr: Address, var returnTime: Time, var passengers: List<User>, var numOfSlots: Int=1, var isSelected: Boolean = false) {

//    @get:Exclude
//    var id = ""

//    @ServerTimestamp
//    var created: Timestamp? = null

    override fun toString(): String {
        return if (title.isNotBlank()) "'$title', driver: '$driver'" else ""
    }

//    companion object {
//        const val COLLECTION_PATH = "quotes"
//        const val CREATED_KEY = "created"
//        fun from(snapshot: DocumentSnapshot):MovieQuote {
//            val mq = snapshot.toObject(MovieQuote::class.java)!! // data only
//            mq.id = snapshot.id
//            return mq
//        }
//    }
}