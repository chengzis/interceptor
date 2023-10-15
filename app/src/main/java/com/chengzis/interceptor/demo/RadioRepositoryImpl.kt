package com.chengzis.interceptor.demo

import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
import io.github.chengzis.hilt.ksp.AutoBinds
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Test1

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Test2

@AutoBinds(
    targets = [
        DefineRadioRepository::class,
        DefineRadioFavoriteRepository::class,
    ],
    installIn = ActivityComponent::class,
    qualifier = Test1::class
)
@ActivityScoped
class RadioRepositoryImpl @Inject constructor() : BaseDefineRadioFavoriteRepository() {


    override fun onAddFavorite(frequency: Int) {
        TODO("Not yet implemented")
    }

    override fun isFavorite(frequency: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun onAddFavorite2(frequency: Int) {
        TODO("Not yet implemented")
    }

}

@AutoBinds(
    targets = [
        DefineRadioRepository::class,
        DefineRadioFavoriteRepository::class,
    ],
    installIn = ActivityComponent::class,
    qualifier = Test2::class
)
@ActivityScoped
class RadioRepositoryImpl2 @Inject constructor() : BaseDefineRadioFavoriteRepository() {


    override fun onAddFavorite(frequency: Int) {
        TODO("Not yet implemented")
    }

    override fun isFavorite(frequency: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun onAddFavorite2(frequency: Int) {
        TODO("Not yet implemented")
    }

}