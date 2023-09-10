package com.github.mrmitew.bankapp

import android.app.Application

/**
 * Created by Stefan Mitev on 4-5-19.
 */

class AndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DependencyInjection.with(this)
    }
}
