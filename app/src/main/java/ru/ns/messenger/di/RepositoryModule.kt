package ru.ns.messenger.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.ns.messenger.api.MessengerApi
import ru.ns.messenger.db.repository.MessengerRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun messengerRepository(messengerApi: MessengerApi) = MessengerRepository(messengerApi)
}