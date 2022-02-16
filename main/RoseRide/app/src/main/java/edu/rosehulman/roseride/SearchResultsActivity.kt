package edu.rosehulman.roseride

import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.Query
import edu.rosehulman.roseride.model.Request
import edu.rosehulman.roseride.model.RequestViewModel.Companion.query
import edu.rosehulman.roseride.model.RequestViewModel.Companion.ref
import edu.rosehulman.roseride.model.Ride
import edu.rosehulman.roseride.model.RideViewModel.Companion.query2
import edu.rosehulman.roseride.model.RideViewModel.Companion.ref2
import edu.rosehulman.roseride.ui.requestList.RequestListFragment
import edu.rosehulman.roseride.ui.rideList.RideListFragment


class SearchResultsActivity : Activity() {
    lateinit var navController: NavController
    var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(Constants.TAG,"search bar created ")

        navController = MainActivity.navController
        id = navController.currentDestination!!.id
        handleIntent(intent)
        finish()
    }

    override fun onNewIntent(intent: Intent) {
        //...
        Log.d(Constants.TAG,"search bar on new intent ")
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        Log.d(Constants.TAG,"intent action: " + intent.action)
        if (Intent.ACTION_SEARCH == intent.action) {
            val qString = intent.getStringExtra(SearchManager.QUERY)
            //use the query to search your data somehow
            Log.d(Constants.TAG,"search: " + qString.toString())

            if(id==R.id.nav_request) {
                query = if (qString==""||qString==null) ref.orderBy(Request.CREATED_KEY, Query.Direction.ASCENDING)
                else ref.whereGreaterThanOrEqualTo("title",qString.uppercase())
                    .whereLessThanOrEqualTo("title",qString.lowercase()+ "\uf8ff").orderBy("title", Query.Direction.ASCENDING)

                val requestListFragment = fragmentManager.findFragmentByTag("RequestListFragment") as RequestListFragment?
                requestListFragment?.adapter?.removeListener("RequestListFragment")
                requestListFragment?.checkOne()

                MainActivity.bottom_nav_view.findViewById<View>(R.id.nav_profile).performClick()
//                MainActivity.bottom_nav_view.findViewById<View>(R.id.nav_request).performClick()
//                navController.navigate(R.id.nav_request)

            }

            if(id==R.id.nav_ride) {
                query2 = if (qString==""||qString==null) ref2.orderBy(Ride.CREATED_KEY, Query.Direction.ASCENDING)
                else ref2.whereGreaterThanOrEqualTo("title",qString.uppercase())
                    .whereLessThanOrEqualTo("title",qString.lowercase()+ "\uf8ff").orderBy("title", Query.Direction.ASCENDING)
//                navController.navigateUp()
//                navController.navigate(R.id.nav_ride)


                val rideListFragment = fragmentManager.findFragmentByTag("RideListFragment") as RideListFragment?
                rideListFragment?.adapter?.removeListener("RideListFragment")
                rideListFragment?.checkOne()

                MainActivity.bottom_nav_view.findViewById<View>(R.id.nav_profile).performClick()
            }





        }
    }
}