package com.vpr42.marketplacegateway.exception

class UserNotFoundException(
    user: String,
    e: Throwable? = null
) : Exception("Authentication exception: User $user not found", e)
