package edu.rosehulman.roseride.model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import edu.rosehulman.roseride.Constants


data class Request(
    var title: String="",
    var user: String="",
    var setOffTime: String="",
    var setOffDate: String="",
    var pickUpAddr: Address= Address(),
    var numOfPassengers: Int=1,
    var destinationAddr: Address = Address(),
    var minPrice: Double=-1.0,
    var maxPrice: Double=-1.0,
    var sharable: Boolean = false,
    var isSelected: Boolean = false){

    @get:Exclude
    var id = ""

    @ServerTimestamp
    var created: Timestamp? = null

//    var setOffTS: Timestamp? = Timestamp(Timestamp(setOffDate).seconds+Timestamp(setOffTime).seconds,0)


    companion object {
        const val COLLECTION_PATH = "requests"
        const val CREATED_KEY = "created"
        fun from(snapshot: DocumentSnapshot):Request {
            val p = snapshot.toObject(Request::class.java)!! // data only
            p.id = snapshot.id
            Log.d(Constants.TAG, "Time: "+snapshot.get("setOffTime").toString())
            Log.d(Constants.TAG, "Date: "+snapshot.get("setOffDate").toString())
//            p.setOffTime = Time.valueOf(snapshot.get("setOffTime").toString())
//            p.setOffDate = Date.valueOf(snapshot.get("setOffDate").toString())
            return p
        }
    }

}