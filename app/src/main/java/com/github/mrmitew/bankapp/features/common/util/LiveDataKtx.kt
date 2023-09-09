package com.github.mrmitew.bankapp.features.common.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Created by Stefan Mitev on 4-5-19.
 */

class SingleLiveData<T>(liveData: LiveData<T>) : MediatorLiveData<T>() {
    private var hasSetValue = false
    private val mediatorObserver = Observer<T> {
        synchronized(hasSetValue) {
            if (!hasSetValue) {
                hasSetValue = true
                this@SingleLiveData.value = it
            }
        }
    }

    init {
        if (liveData.value != null) {
            hasSetValue = true
            this.value = liveData.value
        } else {
            addSource(liveData, mediatorObserver)
        }
    }
}


fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T?> {
    val distinctLiveData = MediatorLiveData<T?>()
    distinctLiveData.addSource(this, object : Observer<T> {
        private var initialized = false
        private var lastObj: T? = null
        override fun onChanged(value: T) {
            if (!initialized) {
                initialized = true
                lastObj = value
                distinctLiveData.postValue(lastObj)
            } else if ((value == null && lastObj != null) || value != lastObj) {
                lastObj = value
                distinctLiveData.postValue(lastObj)
            }
        }
    })
    return distinctLiveData
}

fun <T> T.emitWith(stream: MutableLiveData<T>) = stream.postValue(this)