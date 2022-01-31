package edu.rosehulman.roseride.ui.model

import android.util.Log
import androidx.lifecycle.ViewModel
import java.sql.Date
import java.sql.Time
import kotlin.random.Random

class RequestViewModel : ViewModel(){

    var requests = ArrayList<Request>()
    var currentPos = 0

    fun getRequestAt(pos: Int) = requests[pos]
    fun getCurrentRequest() = getRequestAt(currentPos)

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

    fun addRequest(request: Request?){
        val random = getRandom()
        val newRequest = request ?: Request(
            "Request$random",
            User("Steven","812-223-7777", "fengr@rose-hulman.edu"),
            Time(0),
            Date(0),
            Address("200 N 7th St","Terre Haute", "47809", "IN"),
//            Time(0),
            1,
            Address("210 E Ohio St","Chicago", "60611", "IL"),
            false,
            -1.0,
            -1.0,
            false)
//        ref.add(newQuote)
        requests.add(newRequest)
    }

    fun updateCurrentRequest(
            title: String="",
            setOffTime: Time,
            setOffDate: Date,
            pickUpAddr: Address,
//            returnTime: Time,
            numOfPassengers: Int,
            destinationAddr: Address,
            sharable: Boolean = false,
            minPrice: Double,
            maxPrice: Double)
        {
        requests[currentPos].title = title
        requests[currentPos].setOffTime = setOffTime
        requests[currentPos].setOffDate = setOffDate
        requests[currentPos].pickUpAddr = pickUpAddr
//        requests[currentPos].returnTime = returnTime
        requests[currentPos].numOfPassengers = numOfPassengers
        requests[currentPos].destinationAddr = destinationAddr
        requests[currentPos].sharable = sharable
        requests[currentPos].minPrice = minPrice
        requests[currentPos].maxPrice = maxPrice

//        ref.document(getCurrentQuote().id).set(getCurrentQuote())
        // or use .update() if only want to overwrite specific field(s)
    }

    fun removeCurrentRequest(){
        requests.removeAt(currentPos)
//        ref.document(getCurrentRequest().id).delete()
        currentPos = 0
    }

    fun size() = requests.size

    private fun getRandom() = Random.nextInt(100)

    fun updateCurrentPos(adapterPosition: Int) {
        currentPos = adapterPosition
    }

    fun toggleCurrentRequest(){
        requests[currentPos].isSelected = !requests[currentPos].isSelected
    }


}