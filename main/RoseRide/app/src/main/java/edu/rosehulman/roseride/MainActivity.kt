package edu.rosehulman.roseride

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

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    companion object {
        var driverMode: Boolean = false
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

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        // ref:
        // https://stackoverflow.com/questions/58680195/navigation-drawer-item-click-listener-not-working
        navView.menu!!.findItem(R.id.nav_switch_mode).setOnMenuItemClickListener { menuItem: MenuItem? ->
            //write your implementation here
            //to close the navigation drawer
            driverMode = !driverMode
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            if(driverMode) {
                Toast.makeText(
                    applicationContext,
                    "Switched to Driver Mode!!",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                Toast.makeText(
                    applicationContext,
                    "Passenger Mode!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            navController.navigate(R.id.nav_profile)
            true
        }

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