package com.vpr42.marketplacegateway.repository

import com.vpr42.marketplace.jooq.tables.Users.Companion.USERS
import com.vpr42.marketplace.jooq.tables.pojos.Users
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class UserRepository(
    private val dsl: DSLContext
) {
    fun findByEmail(email: String) = dsl
        .selectFrom(USERS)
        .where(USERS.EMAIL.eq(email))
        .fetchOneInto(Users::class.java)
}
