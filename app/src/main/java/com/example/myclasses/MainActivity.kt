package com.example.myclasses

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myclasses.database.Settings
import com.example.myclasses.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var settings: Settings
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pref = getSharedPreferences("settings", MODE_PRIVATE)
        settings = Settings(pref)

        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
        } catch (e: Exception) {
            Log.e("inflateProb", "onCreateView", e)
            throw e
        }
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_schedule,
                R.id.nav_settings,
                R.id.lessonListFragment,
                R.id.teacherListFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val moveToLesson = intent.getBooleanExtra("move_to_lesson", false)
        if (moveToLesson) {
            val lessonId = intent.getLongExtra("lesson_id", 0)
            navController.navigateUp()
            val args = Bundle()
            args.putLong("lessonId", lessonId)
            navController.navigate(R.id.lessonDetailsFragment, args)
        }

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.session_reminders)
            val des = getString(R.string.session_reminders_des)
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(
                getString(R.string.session_reminders_id),
                name,
                importance
            ).apply {
                description = des
            }

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}