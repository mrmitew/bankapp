package com.github.mrmitew.bankapp.features.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.github.mrmitew.bankapp.R
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val navController = findNavController(R.id.main_content)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.login, R.id.home, R.id.settings, R.id.accountList),
            drawerLayout
        )

        toolbar.setupWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
//        setSupportActionBar(toolbar)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.main_content).navigateUp()
}