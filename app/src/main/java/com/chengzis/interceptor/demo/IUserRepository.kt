package com.chengzis.interceptor.demo

import io.github.chengzis.interceptor.AddInterceptor

data class User(val username:String)

@AddInterceptor
interface IUserRepository {

    fun findUserById(id: String) : User
}