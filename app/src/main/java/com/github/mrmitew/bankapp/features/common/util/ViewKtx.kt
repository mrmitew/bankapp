package com.github.mrmitew.bankapp.features.common.util

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.actor

/**
 * Created by Stefan Mitev on 10-5-19.
 */

fun View.onClick(lifecycleOwner: LifecycleOwner, action: suspend (View) -> Unit) {
    val eventActor = lifecycleOwner.lifecycleScope.actor<View>(Dispatchers.Main) {
        for (event in channel) action(event)
    }
    setOnClickListener {
        eventActor.offer(it)
    }
}
