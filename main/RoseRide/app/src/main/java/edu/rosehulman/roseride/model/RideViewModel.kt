package edu.rosehulman.roseride.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.roseride.Constants
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.random.Random

class RideViewModel : ViewModel() {

    companion object{
        val ref2 = Firebase.firestore.collection(Ride.COLLECTION_PATH)
        lateinit var query2: Query
    }

    init {
        query2 = ref2.orderBy("title", Query.Direction.ASCENDING)
        Log.d(Constants.TAG, "Init Query2: ${query2}")
    }

    var rides = ArrayList<Ride>()
    var currentPos = 0

//    val ref = Firebase.firestore.collection(Ride.COLLECTION_PATH)
    val refHistory = Firebase.firestore.collection(History.COLLECTION_PATH)
    var subscriptions = HashMap<String, ListenerRegistration>()

    fun getRideAt(pos: Int) = rides[pos]
    fun getCurrentRide() = getRideAt(currentPos)

    fun addAllListener(fragmentName: String, observer: () -> Unit) {
        Log.d(Constants.TAG, "Adding listener for $fragmentName")
        Log.d(Constants.TAG, "Query2: ${query2}")
        val subscription = query2
//            .whereNotEqualTo("user", Firebase.auth.uid)
            .addSnapshotListener{ snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                error?.let {
                    Log.d(Constants.TAG, "Error: $error")
                    return@addSnapshotListener
                }

                rides.clear()
                snapshot?.documents?.forEach{
                    rides.add(Ride.from(it))
                }
                observer()
            }
        subscriptions[fragmentName] = subscription
    }


    fun addOneListener(fragmentName: String, observer: () -> Unit) {
        Log.d(Constants.TAG, "Adding listener for $fragmentName")
        val subscription = query2
            .whereEqualTo("driver", Firebase.auth.uid)
            .addSnapshotListener{ snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                error?.let {
                    Log.d(Constants.TAG, "Error: $error")
                    return@addSnapshotListener
                }
                rides.clear()
                snapshot?.documents?.forEach{
                    rides.add(Ride.from(it))

                }
                observer()
            }
        subscriptions[fragmentName] = subscription
    }

    fun removeListener(fragmentName: String){
        subscriptions[fragmentName]?.remove() // tells firebase to stop listening
        subscriptions.remove(fragmentName) // removes from map
    }

    fun addRide(ride: Ride?){
        val random = getRandom()
        val newRide = ride ?: Ride("Ride$random",
            "zhangrj",
            "00:00:00",
            "2022-02-01",
            Address("200 N 7th St","Terre Haute", "47809", "IN"),
//            "00:00:00",
            Address("210 E Ohio St","Chicago", "60611", "IL"),
            listOf(),
            -1.0,
            1,
            false,
            false)
        ref2.add(newRide)
//        rides.add(newRide)
    }

    fun updateCurrentRide(title: String="", setOffTime: String, setOffDate: String, pickUpAddr: Address,
                          addr: Address, passengers: List<String>, cost: Double = -1.0, numOfSlots: Int,
                          isSelected: Boolean, isSharable: Boolean){
        rides[currentPos].title = title
        rides[currentPos].setOffTime = setOffTime
        rides[currentPos].setOffDate = setOffDate
        rides[currentPos].pickUpAddr = pickUpAddr
//        rides[currentPos].returnTime = returnTime
        rides[currentPos].destinationAddr = addr
        rides[currentPos].passengers = passengers
        rides[currentPos].costPerPerson = cost
        rides[currentPos].numOfSlots = numOfSlots
        rides[currentPos].isSelected = isSelected
        rides[currentPos].sharable = isSharable
        ref2.document(getCurrentRide().id).set(getCurrentRide())
        // or use .update() if only want to overwrite specific field(s)
    }

    fun removeCurrentRide(){
//        rides.removeAt(currentPos)
        ref2.document(getCurrentRide().id).delete()
        currentPos = 0
    }

    fun removeRide(ride: Ride){
        ref2.document(ride.id).delete()
        currentPos = 0
    }

    fun removeUserFromRide() {
        rides[currentPos].passengers = rides[currentPos].passengers.filter {
            it != Firebase.auth.uid
        }
        ref.document(getCurrentRide().id).set(getCurrentRide())
    }

    fun size() = rides.size

    private fun getRandom() = Random.nextInt(100)

    fun updateCurrentPos(adapterPosition: Int) {
        currentPos = adapterPosition
    }

    fun toggleCurrentRide(){
        rides[currentPos].isSelected = !rides[currentPos].isSelected
    }


}