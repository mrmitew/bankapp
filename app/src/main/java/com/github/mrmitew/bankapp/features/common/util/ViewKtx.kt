package com.github.mrmitew.bankapp.features.common.util

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.actor

/**
 * Created by Stefan Mitev on 10-5-19.
 */

/**
 * Click listener for an Android [View] using a coroutine actor.
 * Using this approach, the [action] will not be executed multiple times if user clicks the view
 * while the [action] hasn't finished executing.
 *
 * This happens because the actor's mailbox is backed by a [kotlinx.coroutines.channels.RendezvousChannel],
 * whose offer operation succeeds only when the receive is active.
 */
@OptIn(ObsoleteCoroutinesApi::class)
fun View.onClick(lifecycleOwner: LifecycleOwner, action: suspend (View) -> Unit) {
    val eventActor = lifecycleOwner.lifecycleScope.actor<View>(Dispatchers.Main) {
        for (event in channel) action(event)
    }
    setOnClickListener {
        eventActor.trySend(it)
    }
}