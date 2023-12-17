package ru.ns.messenger.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.ns.messenger.data.remote.MessengerApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun messengerApi(retrofit: Retrofit) = retrofit.create(MessengerApi::class.java)
}