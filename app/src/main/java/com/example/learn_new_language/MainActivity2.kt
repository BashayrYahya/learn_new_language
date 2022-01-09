package com.example.learn_new_language


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit


const val APP_ID = "7933a2aa5882442d964f34df30bf6a2b"
const val WORK_ID = ""

class MainActivity2 : AppCompatActivity() {


    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var bottomNav :BottomNavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var fireAuth : FirebaseAuth
    lateinit var fireStore  : FirebaseFirestore


  //  @ExperimentalUnsignedTypes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        fireAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()



      setupRecurringWork()




      // button navigation
        navigationView = findViewById(R.id.navigation_view)
        navController = findNavController(R.id.fragmentContainerView2)
        bottomNav =findViewById(R.id.bottomNav)
        drawerLayout = findViewById(R.id.drawer_layout)
        bottomNav.setupWithNavController(navController)

        // navigation up button
        appBarConfiguration = AppBarConfiguration(navController.graph,drawerLayout)
        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)


        // drawer navigation
        NavigationUI.setupWithNavController(navigationView,navController)

      ActionBarDrawerToggle(this, drawerLayout, R.string.open_menu, R.string.close_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        navigationView.setNavigationItemSelectedListener {
            return@setNavigationItemSelectedListener SetNavigateDrower(it)

        }

      readNameOfUser()

    }

    private fun SetNavigateDrower(it: MenuItem): Boolean {
        it.isChecked = true
        drawerLayout.close()

        when (it.itemId) {
            R.id.profile_icon -> {
                navController.navigate(R.id.student_edit_profile_fragment)

            }
            R.id.logout_icon -> {
                fireAuth.signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.setting_icon -> {
                navController.navigate(R.id.settingFragment)
            }


        }; return true
    }


    // get the name of user an put it in draw nav
    private fun readNameOfUser() {
        val fireStore = FirebaseFirestore.getInstance()
        if (Firebase.auth.currentUser != null ) {
            fireStore.collection("Users")
                .document(Firebase.auth.currentUser!!.uid).get()
                .addOnSuccessListener { it ->
                    val userName = it.data
                  //  Log.e("fromProfile", "the value is ${value?.data}")
                    userName?.forEach {
                        when (it.key) {
                            "fullName" -> findViewById<TextView>(R.id.name_user).text = it.value.toString()

                        }

                    }

                }

        }

    }



    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        return NavigationUI.navigateUp(navController,appBarConfiguration)
    }

    private fun setupRecurringWork() {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<Worker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            WORK_ID,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest)

    }


    }



