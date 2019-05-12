package com.github.mrmitew.bankapp.features.auth

import com.github.mrmitew.bankapp.features.auth.vo.Token

/**
 * Service that knows how to get us a token.
 * Will not over-complicate things with refresh tokens. We'll use the same function for both
 * fetching a new one and also refreshing.
 */
interface AuthService {
    suspend fun getUserToken(appToken: String): Token
}