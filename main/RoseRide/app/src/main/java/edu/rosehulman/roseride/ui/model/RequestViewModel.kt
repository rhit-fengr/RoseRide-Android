package edu.rosehulman.roseride.ui.model

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
import java.sql.Date
import java.sql.Time
import kotlin.random.Random

class RequestViewModel : ViewModel(){

    var requests = ArrayList<Request>()
    var currentPos = 0

    val ref = Firebase.firestore.collection(Request.COLLECTION_PATH)
    var subscriptions = HashMap<String, ListenerRegistration>()

    fun getRequestAt(pos: Int) = requests[pos]
    fun getCurrentRequest() = getRequestAt(currentPos)

    fun addAllListener(fragmentName: String, observer: () -> Unit) {
        Log.d(Constants.TAG, "Adding listener for $fragmentName")
        val subscription = ref.orderBy(Request.CREATED_KEY, Query.Direction.ASCENDING)
            .whereNotEqualTo("user", Firebase.auth.uid)
            .addSnapshotListener{ snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                error?.let {
                    Log.d(Constants.TAG, "Error: $error")
                    return@addSnapshotListener
                }

                requests.clear()
                snapshot?.documents?.forEach{
                    requests.add(Request.from(it))

                }

                observer()
            }

        subscriptions[fragmentName] = subscription
    }

    fun addOneListener(fragmentName: String, observer: () -> Unit) {
        Log.d(Constants.TAG, "Adding listener for $fragmentName")
        val subscription = ref.orderBy(Request.CREATED_KEY, Query.Direction.ASCENDING)
            .whereEqualTo("user", Firebase.auth.uid)
            .addSnapshotListener{ snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                error?.let {
                    Log.d(Constants.TAG, "Error: $error")
                    return@addSnapshotListener
                }

                requests.clear()
                snapshot?.documents?.forEach{
                    requests.add(Request.from(it))

                }

                observer()
            }

        subscriptions[fragmentName] = subscription
    }

    fun removeListener(fragmentName: String){
        subscriptions[fragmentName]?.remove() // tells firebase to stop listening
        subscriptions.remove(fragmentName) // removes from map
    }

    fun addRequest(request: Request?){
        val random = getRandom()
        val newRequest = request ?: Request(
            "Request$random",
            "zhangrj",
            "00:00:00",
            "2022-02-01",
            Address("200 N 7th St","Terre Haute", "47809", "IN"),
//            Time(0),
            1,
            Address("210 E Ohio St","Chicago", "60611", "IL"),
            -1.0,
            -1.0,
            false)
        ref.add(newRequest)
//        requests.add(newRequest)
    }

    fun updateCurrentRequest(
            title: String="",
            setOffTime: String,
            setOffDate: String,
            pickUpAddr: Address,
//            returnTime: Time,
            numOfPassengers: Int,
            destinationAddr: Address,
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
        requests[currentPos].minPrice = minPrice
        requests[currentPos].maxPrice = maxPrice

        ref.document(getCurrentRequest().id).set(getCurrentRequest())
        // or use .update() if only want to overwrite specific field(s)
    }

    fun removeCurrentRequest(){
//        requests.removeAt(currentPos)
        ref.document(getCurrentRequest().id).delete()
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