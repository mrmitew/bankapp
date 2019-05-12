package com.github.mrmitew.bankapp.features.auth.vo

/**
 * Created by Stefan Mitev on 12-5-19.
 */

// TODO: We didn't make a refresh token, just to keep things simpler.
data class Token(val accessToken: String,
                 val expireDate: Long)