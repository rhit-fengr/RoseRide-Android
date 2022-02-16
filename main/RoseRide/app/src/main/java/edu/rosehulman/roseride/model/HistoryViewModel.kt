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
import kotlin.random.Random

class HistoryViewModel : ViewModel() {

    var histories = ArrayList<History>()
    var currentPos = 0

    val ref = Firebase.firestore.collection(History.COLLECTION_PATH)
    var subscriptions = HashMap<String, ListenerRegistration>()

    fun getHistoryAt(pos: Int) = histories[pos]
    fun getCurrentHistory() = getHistoryAt(currentPos)

    fun addListener(fragmentName: String, observer: () -> Unit) {
        addDriverListener(fragmentName, observer)
        addPassengerListener(fragmentName, observer)
    }

    fun addDriverListener(fragmentName: String, observer: () -> Unit) {
        Log.d(Constants.TAG, "Adding listener for $fragmentName")
        val subscription = ref.orderBy(Request.CREATED_KEY, Query.Direction.ASCENDING)
            .whereEqualTo("driver", Firebase.auth.uid)
            .addSnapshotListener{ snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                error?.let {
                    Log.d(Constants.TAG, "Error: $error")
                    return@addSnapshotListener
                }

                histories.clear()
                snapshot?.documents?.forEach{
                    histories.add(History.from(it))

                }

                observer()
            }

        subscriptions[fragmentName] = subscription
    }

    fun addPassengerListener(fragmentName: String, observer: () -> Unit) {
        Log.d(Constants.TAG, "Adding listener for $fragmentName")
        val subscription = ref.orderBy(History.CREATED_KEY, Query.Direction.ASCENDING)
            .whereArrayContains("passengers", Firebase.auth.uid!!)
            .addSnapshotListener{ snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                error?.let {
                    Log.d(Constants.TAG, "Error: $error")
                    return@addSnapshotListener
                }
                snapshot?.documents?.forEach{
                    histories.add(History.from(it))

                }

                observer()
            }

        subscriptions[fragmentName + "2" ] = subscription
    }

    fun removeListener(fragmentName: String){
        subscriptions[fragmentName]?.remove() // tells firebase to stop listening
        subscriptions[fragmentName + "2" ]?.remove()
        subscriptions.remove(fragmentName)
        subscriptions.remove(fragmentName + "2")// removes from map
    }

    fun addHistory(history: History?){
        val random = getRandom()
        val newHistory = history ?: History(
            "History$random",
            "zhangrj",
            "00:00:00",
            "2022-02-01",
            Address("200 N 7th St","Terre Haute", "47809", "IN"),
            listOf(),
            -1.0)
        ref.add(newHistory)
    }


    fun size() = histories.size

    private fun getRandom() = Random.nextInt(100)

    fun updateCurrentPos(adapterPosition: Int) {
        currentPos = adapterPosition
    }


}