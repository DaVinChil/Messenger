package ru.ns.messenger.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.Date

interface MessengerApi {
    @POST("chat/send")
    suspend fun sendMessage(@Body message: MessageDto)

    @GET("chat/messages")
    suspend fun getMessages(): List<Message>
}

data class MessageDto(val sender: UserDto, val message: String)
data class Message(val sender: UserDto, val message: String, val date: Date)
data class UserDto(val name: String)