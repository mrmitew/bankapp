package com.github.mrmitew.bankapp.features.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.mrmitew.bankapp.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar
            .setupWithNavController(
                findNavController(R.id.main_content),
                findViewById<DrawerLayout>(R.id.drawer_layout)
            )

        setSupportActionBar(toolbar)
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.main_content).navigateUp()
}