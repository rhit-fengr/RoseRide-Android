package edu.rosehulman.roseride

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.rosehulman.roseride.databinding.ActivityMainBinding
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseUser

import androidx.annotation.NonNull
import androidx.lifecycle.ViewModelProvider

import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import edu.rosehulman.rosefire.Rosefire
import edu.rosehulman.roseride.ui.model.UserViewModel


class MainActivity : AppCompatActivity() {

    private val REGISTRY_TOKEN = "e015dafd-140f-4ea7-8b7c-c9a0f26c223f"
    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: AuthStateListener? = null
    private var checker: Boolean = false
    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val rosefireResult = Rosefire.getSignInResultFromIntent(data)
            if (rosefireResult.isSuccessful) {
                Firebase.auth.signInWithCustomToken(rosefireResult.token)
                Log.d(Constants.TAG, "Username: ${rosefireResult.username}")
                Log.d(Constants.TAG, "Name: ${rosefireResult.name}")
                Log.d(Constants.TAG, "Email: ${rosefireResult.email}")
                Log.d(Constants.TAG, "Group: ${rosefireResult.group}")
            } else {
                Log.d(Constants.TAG, "Rosefire failed")
            }
        }
        checker = true
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var navController: NavController

    companion object {
        var driverMode: Boolean = false
    }

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth!!.removeAuthStateListener(mAuthListener!!)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

//        binding.appBarMain.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        // set up rosefire
//        val loginButton: View = findViewById(R.id.rosefire_login)
        mAuth = FirebaseAuth.getInstance()
        mAuthListener = AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            Log.d(Constants.TAG,"user: ${user?.uid}")
            if(user==null && !checker){
                val signInIntent = Rosefire.getSignInIntent(this, REGISTRY_TOKEN);
                    resultLauncher.launch(signInIntent)
            }
            val userModel = ViewModelProvider(this).get(UserViewModel::class.java)
            userModel.getOrMakeUser {
                if (!userModel.hasCompletedSetup()) {
                    navController.navigate(R.id.nav_profile_edit)
                }
            }
//            val username = user?.uid ?: "null"
//            loginButton.setVisibility(if (user != null) View.GONE else View.VISIBLE)
        }

//        loginButton.setOnClickListener {
//                    val signInIntent = Rosefire.getSignInIntent(this, REGISTRY_TOKEN);
//                    resultLauncher.launch(signInIntent)
//        }


        drawerLayout = binding.drawerLayout
        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        setupDrawerMenuItems()

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_ride, R.id.nav_request, R.id.nav_profile
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



       val bottom_nav_view:BottomNavigationView = findViewById(R.id.bottom_nav_view)
        bottom_nav_view.setupWithNavController(navController)

    }

    private fun setupDrawerMenuItems() {
        // ref:
        // https://stackoverflow.com/questions/58680195/navigation-drawer-item-click-listener-not-working
        navView.menu!!.findItem(R.id.nav_switch_mode).setOnMenuItemClickListener { menuItem: MenuItem? ->
            //write your implementation here
            //to close the navigation drawer
            driverMode = !driverMode
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            val my_posts = navView.menu!!.findItem(R.id.nav_my_posts)

            if(driverMode) {
                menuItem!!.title="Switch to Passenger Mode"
                my_posts.title="My Rides"
                Toast.makeText(
                    applicationContext,
                    "Switched to Driver Mode!!",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                menuItem!!.title="Switch to Driver Mode"
                my_posts.title="My Requests"
                Toast.makeText(
                    applicationContext,
                    "Passenger Mode!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            navController.navigate(R.id.nav_profile)
            true
        }

//        navView.menu!!.findItem(R.id.nav_my_posts).setOnMenuItemClickListener { menuItem: MenuItem? ->
//            //write your implementation here
//            //to close the navigation drawer
//
//            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                drawerLayout.closeDrawer(GravityCompat.START)
//            }
//
//            if(driverMode){
//                navController.navigate(R.id.nav_ride)
//            }else navController.navigate(R.id.nav_request)
//
//            true
//        }

        navView.menu!!.findItem(R.id.nav_history).setOnMenuItemClickListener { menuItem: MenuItem? ->
            navController.navigate(R.id.navigation_history)
            true
        }

        // https://stackoverflow.com/questions/4960259/current-location-in-google-maps-using-only-a-url
        navView.menu!!.findItem(R.id.nav_location).setOnMenuItemClickListener { menuItem: MenuItem? ->
            //write your implementation here
            //to close the navigation drawer

            val uri: Uri = Uri.parse("http://maps.google.com/maps/api/staticmap?center=48.00,9.00" +
                    "&zoom=14&size=512x512" +
                    "&maptype=roadmap&markers=color:blue|48.0,9.0" +
                    "&sensor=true")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)

            Toast.makeText(
                applicationContext,
                "View Current Location in Map",
                Toast.LENGTH_SHORT
            ).show()

            navController.navigate(R.id.nav_profile)
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}