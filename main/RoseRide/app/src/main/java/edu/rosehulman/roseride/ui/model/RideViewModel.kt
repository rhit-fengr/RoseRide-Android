package edu.rosehulman.roseride.ui.model

import androidx.lifecycle.ViewModel
import java.sql.Time
import kotlin.random.Random

class RideViewModel : ViewModel() {

    var rides = ArrayList<Ride>()
    var currentPos = 0

    fun getRideAt(pos: Int) = rides[pos]
    fun getCurrentRide() = getRideAt(currentPos)

//    val ref = Firebase.firestore.collection(Request.COLLECTION_PATH)
//    var subscriptions = HashMap<String, ListenerRegistration>()

//    fun addListener(fragmentName: String, observer: () -> Unit) {
//        Log.d(Constants.TAG, "Adding listener for $fragmentName")
//        val subscription = ref.orderBy(Request.CREATED_KEY, Query.Direction.ASCENDING)
//            .addSnapshotListener{ snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
//                error?.let {
//                    Log.d(Constants.TAG, "Error: $error")
//                    return@addSnapshotListener
//                }
//
//                requests.clear()
//                snapshot?.documents?.forEach{
//                    requests.add(Request.from(it))
//
//                }
//
//                observer()
//            }
//
//        subscriptions[fragmentName] = subscription
//    }

//    fun removeListener(fragmentName: String){
//        subscriptions[fragmentName]?.remove() // tells firebase to stop listening
//        subscriptions.remove(fragmentName) // removes from map
//    }

    fun addRide(ride: Ride?){
        val random = getRandom()
        val newRide = ride ?: Ride("Ride$random",
            User("Steven","812-223-7777", "fengr@rose-hulman.edu"),
            Time(0),
            Address("200 N 7th St","Terre Haute", "47809", "IN"),
            Time(0),
            Address("210 E Ohio St","Chicago", "60611", "IL"),
            listOf<User>(),
            1,
            false)
//        ref.add(newQuote)
        rides.add(newRide)
    }

    fun updateCurrentRide(title: String="", setOffTime: Time, pickUpAddr: Address, returnTime: Time,
                          addr: Address, passengers: List<User>, numOfSlots: Int, isSelected: Boolean){
        rides[currentPos].title = title
        rides[currentPos].setOffTime = setOffTime
        rides[currentPos].pickUpAddr = pickUpAddr
        rides[currentPos].returnTime = returnTime
        rides[currentPos].addr = addr
        rides[currentPos].passengers = passengers
        rides[currentPos].numOfSlots = numOfSlots
        rides[currentPos].isSelected = isSelected

//        ref.document(getCurrentQuote().id).set(getCurrentQuote())
        // or use .update() if only want to overwrite specific field(s)
    }

    fun removeCurrentRie(){
        rides.removeAt(currentPos)
//        ref.document(getCurrentRequest().id).delete()
        currentPos = 0
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