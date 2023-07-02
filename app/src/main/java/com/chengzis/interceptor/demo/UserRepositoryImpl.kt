package com.chengzis.interceptor.demo

class UserRepositoryImpl : HasInterceptorIUserRepository() {

    override fun onFindUserById(id: String): User {
       return User("chengzis2")
    }
}