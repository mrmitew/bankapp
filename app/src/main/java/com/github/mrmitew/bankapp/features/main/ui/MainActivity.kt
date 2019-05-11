package com.github.mrmitew.bankapp.features.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.mrmitew.bankapp.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Toolbar>(R.id.toolbar)
            .setupWithNavController(findNavController(R.id.main_content))
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.main_content).navigateUp()
}